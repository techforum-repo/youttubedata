import faiss
import numpy as np
import json
from pymongo import MongoClient

# Load faiss index
index = faiss.read_index("faiss_index.bin")

# Log the content of the Faiss index
print("Total number of entries in Faiss index:", index.ntotal)
print("Dimension of embeddings:", index.d)

# Retrieve the first 10 entries
D, I = index.search(np.zeros((1, index.d), dtype=np.float32), k=10)
print("First 10 distances:", D)
print("First 10 indices:", I)

# Load content_id_map
with open("content_id_map.json", "r") as f:
    content_id_map = json.load(f)

print("Content ID Map (first 10):", {i: content_id_map[str(i)] for i in range(10)})

# Initialize MongoDB client
client = MongoClient("mongodb+srv://cmscontent:Cmscontent123!@cluster0.1qriowr.mongodb.net/")
db = client['cms_content']
content_collection = db['content_embeddings']

# Fetch and print the embeddings from MongoDB
content_ids = [content_id_map[str(i)] for i in range(10)]
contents = list(content_collection.find({"content_id": {"$in": content_ids}}))

print("MongoDB Content (first 10):")
for content in contents:
    print("content_id:", content['content_id'])
    if isinstance(content['embedding'][0], dict):
        embedding = [float(x['$numberDouble']) for x in content['embedding']]
    else:
        embedding = [float(x) for x in content['embedding']]
    print("embedding from MongoDB:", embedding[:5], "...")  # print only the first 5 elements for brevity
    print("popularity:", content['popularity'])
    print("text:", content['text'])
    print("title:", content['title'])
    print()

# Compare embeddings in Faiss with MongoDB
print("Comparing embeddings from Faiss and MongoDB for the first 10 content items:")
for i in range(10):
    faiss_index = int(I[0][i])  # Ensure faiss_index is of type int
    mongo_content_id = content_id_map[str(faiss_index)]
    mongo_content = next((item for item in contents if item['content_id'] == mongo_content_id), None)
    
    if mongo_content:
        if isinstance(mongo_content['embedding'][0], dict):
            embedding_mongo = [float(x['$numberDouble']) for x in mongo_content['embedding']]
        else:
            embedding_mongo = [float(x) for x in mongo_content['embedding']]
        embedding_faiss = np.zeros(index.d, dtype=np.float32)  # Create an array to hold the reconstructed embedding
        index.reconstruct(faiss_index, embedding_faiss)
        print(f"Comparison for content_id {mongo_content_id}:")
        print("Embedding from MongoDB (first 5 elements):", embedding_mongo[:5], "...")
        print("Embedding from Faiss (first 5 elements):", embedding_faiss[:5], "...")
        print("Embeddings match:", np.allclose(embedding_mongo, embedding_faiss))
    else:
        print(f"No matching content found in MongoDB for Faiss index {faiss_index}")
    print()
