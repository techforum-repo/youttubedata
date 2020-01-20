mport org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.types.ObjectId;

public class MongoDBAtlasClient {
	
	public static void main(String[] args) {
		
		//System.setProperty("jdk.tls.trustNameService", "true");
		
		MongoClientURI uri = new MongoClientURI(
			    "mongodb+srv://<user name>:<password>@<cluster hostname>/test?retryWrites=true&w=majority&connectTimeoutMS=30000&socketTimeoutMS=30000");
			
		try(MongoClient mongoClient = new MongoClient(uri))
			{
				MongoDatabase database = mongoClient.getDatabase("test");			
				MongoCollection<Document> collection = database.getCollection("test");
				Document query = new Document("_id", new ObjectId("5e234fe121fcf183e83ddce2"));
		        Document result = collection.find(query).iterator().next();

		        System.out.println("Test3: "+result.getString("test3"));
			}		
	}


}
