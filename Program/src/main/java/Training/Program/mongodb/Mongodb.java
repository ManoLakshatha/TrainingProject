package Training.Program.mongodb;

import java.util.Vector;

import org.bson.Document;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Mongodb {

	 private static final String uri = "mongodb://localhost:27017";
	    private static final String database_name = "ProjectStores";
	    private static final String users_collection = "Users";
	    private static final String shipments_collection = "Shipments";
	    private static final String devices_collection = "Devices";

	    private static final MongoDatabase database;


	    static {
	        MongoClient mongoClient = MongoClients.create(uri);
	        database = mongoClient.getDatabase(database_name);
	    }

	    private static final MongoCollection<Document> users = database.getCollection(users_collection);
	    private static final MongoCollection<Document> shipments = database.getCollection(shipments_collection);
	    private static final MongoCollection<Document> devices = database.getCollection(devices_collection);

	    private static String myHash(String s){
	        return Integer.toHexString(s.hashCode());
	    }

	    public static void addUser(String username, String password){
	        Document document = new Document();
	        document.append("_id", username);
	        document.append("Password", myHash(password));
	        users.insertOne(document);
	    }

	    public static void addShipment(String username, String si_no,String container_no,
	            String desc,
	             String route,
	             String goods,
	             String device,
	             String date,
	             String po_no,
	             String delivery_no,
	             String ndc_no,
	            String batch_id,
	             String serial_no){
	        Document document = new Document();
	        document.append("username", username);
	        document.append("si_no", si_no);
	        document.append("container_no",container_no);
	        document.append("desc", desc);
	        document.append("route", route);
	        document.append("goods", goods);
	        document.append("device", device);
	        document.append("date", date);
	        document.append("po_no", po_no);
	        document.append("delivery_no", delivery_no);
	        document.append("ndc_no", ndc_no);
	        document.append("batch_id", batch_id);
	        document.append("serial_no", serial_no);
	        shipments.insertOne(document);
	    }

	    public static boolean isUsernameAvailable(String username) throws Exception {
	        for (Document document : users.find(new Document("_id", username))) {
	            if (document.get("_id").equals(username))
	                throw new Exception("Username is already taken!");
	        }
	        return true;
	    }

	    public static boolean authenticateUser(String username, String password) throws Exception {
	        for (Document document : users.find(new Document("_id", username))) {
	            if (document.get("Password").equals(myHash(password)))
	                return true;
	            else
	                throw new Exception("Incorrect Password!");
	        }
	        throw new Exception("User not found!");
	    }

	    public static Vector<String> getDeviceIDs(){
	        Vector<String> devices = new Vector<>();
	        devices.add("00001");
	        devices.add("00002");
	        devices.add("00003");
	        devices.add("00004");
	        return devices;
	    }

	    public static Vector<Document> getDevices() {
	        Vector<Document> devices = new Vector<>();
	        for (Document document : Mongodb.devices.find())
	            devices.add(document);
	        return devices;
	    }

	}

