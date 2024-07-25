import requests
from nltk.tokenize import word_tokenize
from nltk.corpus import stopwords
from nltk.stem import WordNetLemmatizer
from sentence_transformers import SentenceTransformer
import faiss
import numpy as np
from db import insert_content
import nltk
import json

# Ensure NLTK data is downloaded
nltk.download('punkt')
nltk.download('stopwords')
nltk.download('wordnet')

# Set up AEM and model configurations
AEM_BASE_URL = "http://localhost:4502"
AEM_AUTH = ('admin', 'admin')
AEM_CONTENT_PATH = "/content/wknd/us/en"
AEM_QUERY_URL = f"{AEM_BASE_URL}/bin/querybuilder.json?path={AEM_CONTENT_PATH}&type=cq%3aPage&p.properties=jcr%3acontent/jcr%3adescription&p.limit=-1"
MODEL_NAME = 'all-MiniLM-L6-v2'

# Initialize Sentence Transformer model
model = SentenceTransformer(MODEL_NAME, device='cpu')

def fetch_aem_content():
    response = requests.get(AEM_QUERY_URL, auth=AEM_AUTH)
    response.raise_for_status()
    return response.json()

def preprocess_text(text):
    tokens = word_tokenize(text)
    tokens = [word for word in tokens if word.isalnum()]
    tokens = [word for word in tokens if word not in stopwords.words('english')]
    lemmatizer = WordNetLemmatizer()
    tokens = [lemmatizer.lemmatize(word) for word in tokens]
    return ' '.join(tokens)

def generate_embedding(text):
    return model.encode(text)

def insert_content_with_embedding(content):
    for item in content:
        processed_text = preprocess_text(item['text'])
        embedding = generate_embedding(processed_text)
        content_data = {
            'content_id': item['path'],
            'title': item['title'],
            'text': item['text'],
            'embedding': embedding.tolist(),
            'popularity': 0  # Initialize popularity to 0
        }
        insert_content(content_data)
        item['embedding'] = embedding

def main():
    aem_data = fetch_aem_content()
    content = [
        {
            'path': hit['path'],
            'title': hit['title'],
            'text': hit['excerpt'],
            'popularity': 0  # Initialize popularity to 0
        }
        for hit in aem_data['hits']
    ]
    insert_content_with_embedding(content)

    # Index embeddings using faiss
    embeddings = np.array([item['embedding'] for item in content])
    index = faiss.IndexFlatL2(embeddings.shape[1])
    index.add(embeddings)
    faiss.write_index(index, "faiss_index.bin")

    # Save content ID map
    content_id_map = {str(i): item['path'] for i, item in enumerate(content)}
    with open("content_id_map.json", "w") as f:
        json.dump(content_id_map, f)

if __name__ == "__main__":
    main()