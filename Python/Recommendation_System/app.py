from flask import Flask, request, jsonify
from sentence_transformers import SentenceTransformer
import numpy as np
import faiss
from db import fetch_user, fetch_content_by_ids, insert_user, update_content_popularity
import logging
import json

app = Flask(__name__)
logging.basicConfig(level=logging.DEBUG)

# Set up Sentence-Transformers model and force it to use CPU
model = SentenceTransformer('all-MiniLM-L6-v2', device='cpu')

# Load faiss index and content ID map
try:
    index = faiss.read_index("faiss_index.bin")
    with open("content_id_map.json", "r") as f:
        content_id_map = json.load(f)
except Exception as e:
    logging.error(f"Error loading Faiss index or content ID map: {e}")
    raise e

# Utility function to generate embeddings
def generate_embedding(text):
    logging.debug(f"Generating embedding for text: {text}")
    return model.encode(text)

# Route to get recommendations
@app.route('/recommend', methods=['POST'])
def recommend():
    try:
        user_data = request.json
        logging.debug(f"Received user data: {user_data}")
        user_text = user_data.get('text', '')
        user_id = user_data.get('user_id')
        
        # Generate embedding from user text if provided
        user_embedding = None
        if user_text:
            user_embedding = generate_embedding(user_text).reshape(1, -1)
            logging.debug(f"Generated embedding from user text: {user_embedding}")
        
        # Fetch user profile and generate embedding from preferences if user_id is provided
        if user_id and user_embedding is None:
            user_profile = fetch_user(user_id)
            logging.debug(f"Fetched user profile: {user_profile}")
            if user_profile:
                user_preferences = user_profile.get('profile', {}).get('preferences', [])
                if user_preferences:
                    user_embedding = generate_embedding(' '.join(user_preferences)).reshape(1, -1)
                    logging.debug(f"Generated embedding from user preferences: {user_embedding}")
        
        if user_embedding is None:
            logging.debug("No valid text or preferences provided for recommendation.")
            return jsonify({"error": "No valid text or preferences provided for recommendation."}), 400
        
        # Query faiss index
        D, I = index.search(user_embedding, k=10)
        logging.debug(f"Faiss index search results - Distances: {D}, Indices: {I}")
        recommended_ids = [content_id_map[str(i)] for i in I[0] if i != -1]  # Convert Faiss IDs to content_ids
        logging.debug(f"Recommended IDs from Faiss: {recommended_ids}")
        
        # Fetch recommended content
        recommended_content = fetch_content_by_ids(recommended_ids)
        logging.debug(f"Fetched recommended content: {recommended_content}")
        
        # Ensure all data is native Python types and remove embeddings
        for content in recommended_content:
            content['popularity'] = int(content.get('popularity', 0))
            if 'embedding' in content:
                del content['embedding']
        
        # Sort by popularity
        recommended_content.sort(key=lambda x: x.get('popularity', 0), reverse=True)
        
        # Update content popularity based on interactions
        update_content_popularity(user_id, recommended_ids)
        
        return jsonify(recommended_content[:5])  # Return top 5 recommendations
    except Exception as e:
        logging.error(f"Error in /recommend endpoint: {e}")
        return jsonify({"error": "An error occurred during processing."}), 500

# Route to insert user data
@app.route('/insert_user', methods=['POST'])
def insert_user_route():
    try:
        user_data = request.json
        logging.debug(f"Received user data for insertion: {user_data}")
        user_id = user_data.get('user_id')
        profile = user_data.get('profile', {})
        interactions = user_data.get('interactions', {})
        
        if not user_id:
            return jsonify({"error": "user_id is required"}), 400

        user_document = {
            "user_id": user_id,
            "profile": profile,
            "interactions": interactions
        }
        
        insert_user(user_document)
        
        return jsonify({"message": "User inserted successfully"}), 201
    except Exception as e:
        logging.error(f"Error in /insert_user endpoint: {e}")
        return jsonify({"error": "An error occurred during processing."}), 500

if __name__ == '__main__':
    app.run(debug=True)