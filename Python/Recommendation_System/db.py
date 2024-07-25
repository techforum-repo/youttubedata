from pymongo import MongoClient, UpdateOne
import logging

# Initialize MongoDB client
client = MongoClient("<<Mongodb connect string>>")
db = client['cms_content']
content_collection = db['content_embeddings']
user_collection = db['user_profiles']

def insert_content(content_data):
    # Use update_one with upsert=True to avoid duplicates and update if exists
    content_collection.update_one(
        {'content_id': content_data['content_id']},
        {'$set': content_data},
        upsert=True
    )

def fetch_content_by_ids(content_ids):
    logging.debug(f"Fetching content by IDs: {content_ids}")
    contents = list(content_collection.find({"content_id": {"$in": content_ids}}))
    for content in contents:
        # Convert ObjectId to string
        content['_id'] = str(content['_id'])

        # Handle popularity type
        if isinstance(content['popularity'], dict):
            content['popularity'] = int(content['popularity']['$numberInt'])
        else:
            content['popularity'] = int(content['popularity'])

        # Convert MongoDB-specific data types to standard Python types
        if isinstance(content['embedding'][0], dict):
            content['embedding'] = [float(x['$numberDouble']) for x in content['embedding']]
        else:
            content['embedding'] = [float(x) for x in content['embedding']]
    return contents

def fetch_user(user_id):
    logging.debug(f"Fetching user by ID: {user_id}")
    return user_collection.find_one({"user_id": user_id})

def insert_user(user_data):
    logging.debug(f"Inserting/updating user data: {user_data}")
    user_collection.update_one(
        {'user_id': user_data['user_id']},
        {'$set': user_data},
        upsert=True
    )

def update_content_popularity(user_id, content_ids):
    user = fetch_user(user_id)
    if user:
        interactions = user.get('interactions', {})
        viewed_content = interactions.get('viewed', [])
        liked_content = interactions.get('liked', [])

        for content_id in content_ids:
            if content_id in viewed_content:
                content_collection.update_one(
                    {'content_id': content_id},
                    {'$inc': {'popularity': 1}}
                )
            if content_id in liked_content:
                content_collection.update_one(
                    {'content_id': content_id},
                    {'$inc': {'popularity': 2}}
                )