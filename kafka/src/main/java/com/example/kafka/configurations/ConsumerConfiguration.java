package com.example.kafka.configurations;


import org.apache.kafka.clients.consumer.ConsumerConfig;  
import org.apache.kafka.clients.consumer.ConsumerRecord;  
import org.apache.kafka.clients.consumer.ConsumerRecords;  
import org.apache.kafka.clients.consumer.KafkaConsumer;  
import org.apache.kafka.common.serialization.StringDeserializer;
import org.bson.Document;

import org.json.JSONException;

import org.slf4j.Logger;  
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Component;



import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


import java.time.Duration;  
import java.util.Arrays;  
  
import java.util.Properties;  
@Component  
public class ConsumerConfiguration {
	   private static MongoCollection<Document> coll = null;
//	    private MongoClient mongo = null;
	    public static void main(String[] args) throws JSONException {
	        try
	        {
	            
//	      MongoClient mongo = new MongoClient("localhost" , 27017);  
	  //     MongoClient mongo = new MongoClient("localhost" , 27017);
	       MongoClient mongoClient = MongoClients.create("mongodb+srv://Mano:Test@123@cluster0.k4bsfww.mongodb.net/?retryWrites=true&w=majority");
	       MongoDatabase db = mongoClient.getDatabase("Scmxpert");  
//	      MongoCollection<Document> coll = db.getCollection("DeviceDataStream");
	       coll = db.getCollection("Devices");
	       System.out.println("Mongo connection estabished  ......");
	       
	        }
	    
	    catch (Exception e) {
	        // handle server down or failed query here.
	    }
	        Logger logger= LoggerFactory.getLogger(ConsumerConfiguration.class.getName());  
	        String bootstrapServers="127.0.0.1:9092";  
	        String grp_id="myGroup";  
	        String topic="XYZ";
	      
	        //Creating consumer properties  
	        Properties properties=new Properties();  
	        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);  
	        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,   StringDeserializer.class.getName());  
	        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());  
	        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG,grp_id);  
	        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");  
	        try (//creating consumer  
			KafkaConsumer<String,String> consumer = new KafkaConsumer<String,String>(properties)) {
				//	        Consumer consume = new Consumer();
				//	       consume.coll();
				//	       consume.coll = null;
					        //Subscribing  
					                consumer.subscribe(Arrays.asList(topic));  
					        //polling  
					        while(true){  
					            ConsumerRecords<String,String> records=consumer.poll(Duration.ofMillis(100));
					            for(ConsumerRecord<String,String> record: records){  
					                
					                logger.info("Key: "+ record.key() + ", Value:" +record.value());  
					                logger.info("Partition:" + record.partition()+",Offset:"+record.offset());            
					                Document doc = Document.parse(record.value());
					                coll.insertOne(doc);
					               
					            }
					            
					  
					  
					        }
			}  
	                  
	    }  
}