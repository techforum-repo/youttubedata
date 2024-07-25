## SetUp

- Create Python Virtual Environment - python -m venv venv
- Activate Virutal Env - venv\Scripts\activate
- Install the dependencies - pip install -r requirements.txt

## Test

### Fetch and Store AEM Content

Run the fetch_content.py Script:

Ensure your MongoDB server is running. Then execute the following command to fetch and store AEM content:

    python fetch_content.py

This script will:

- Fetch content from AEM.
- Preprocess the content.
- Generate embeddings using the all-MiniLM-L6-v2 model.
- Store the content with embeddings in MongoDB.
- Create a Faiss index for efficient similarity search.

### Start the Flask Application

Run the app.py Script:

Start the Flask application using the following command:

    python app.py

This will start the Flask server on the default port (5000), and you'll be able to access the endpoints.

### Test the API Endpoints

#### Insert User Data:

Use the following cURL command to insert user data:

```
curl -X POST http://127.0.0.1:5000/insert_user -H "Content-Type: application/json" -d '{
"user_id": "user1",
"profile": {
"age": 25,
"location": "New York",
"preferences": ["technology", "sports"]
},
"interactions": {
"viewed": ["/content/wknd/us/en/about-us", "/content/wknd/us/en/adventures/surf-camp-costa-rica"],
"liked": ["/content/wknd/us/en/adventures/surf-camp-costa-rica"]
}
}'
```

#### Fetch Recommendations:

With User Text:

```
curl -X POST http://127.0.0.1:5000/recommend -H "Content-Type: application/json" -d '{
"text": "Looking for adventure travel experiences"
}'
```

With User ID:

```
curl -X POST http://127.0.0.1:5000/recommend -H "Content-Type: application/json" -d '{
"user_id": "user1"
}'
```

With Both User Text and User ID:

```
curl -X POST http://127.0.0.1:5000/recommend -H "Content-Type: application/json" -d '{
"text": "Looking for adventure travel experiences",
"user_id": "user1"
}'
```
