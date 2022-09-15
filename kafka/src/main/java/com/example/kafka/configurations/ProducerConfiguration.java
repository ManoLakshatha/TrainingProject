package com.example.kafka.configurations;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;



import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class ProducerConfiguration {
	 
    // initialize socket and input output streams
    private Socket socket            = null;
    private BufferedReader  input   = null;
    private BufferedWriter  out     = null;
    // constructor to put ip address and port
    public void Client1(String address, int port) throws JSONException
    {
        // establish a connection
        try
        {
            socket = new Socket(address, port);
            System.out.println("Connected");
            // takes input from terminal
            input  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // sends output to the socket
            out    = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));


        }
        catch(UnknownHostException u)
        {
            System.out.println(u);
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
        System.out.println("Reading bytes now...");
        // string to read message from input
        String line = "";



       // keep reading until "Over" is input
        while (!line.equals("Over"))
        {
            System.out.println("Inside...");



           try
            {
                 System.out.println("Inside here..."+line);
                line = input.readLine();
                System.out.println("Inside line..."+line);
                JSONArray array = new JSONArray(line);  
//               System.out.println("Inside...");
                for(int i=0; i < array.length(); i++)   
                {  
                   System.out.println("Inside line for..."+line);
                JSONObject object = array.getJSONObject(i);  
                System.out.println(object);  
                
            //    DBObject obj = (DBObject) JSON.parse(object.toString());
              
                KafkaTemplate<String, String> kafkaTemplate = kafkaTemplate();
                kafkaTemplate.send("XYZ", object.toString());
                System.out.println("sent data  ..."+object.toString());
   //             Document doc = Document.parse(object.toString());
   //             coll.insertOne(doc);
        //        System.out.println(object.getString("Name"));  
                }
              
//                Document doc = new Document("name", line);                                                               
       //         Document doc = new Document("name", object);
        //        coll.insertOne(doc);



               // out.writeUTF(line);
                System.out.println(line);
            }
            catch(IOException i)
            {
                System.out.println(i);
            }
        }
        // close the connection
        try
        {
            input.close();
            out.close();
            socket.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }
    public static void main(String args[]) throws JSONException
    {
   //     Client client = new Client("52.15.210.112", 12345);
         
        ProducerConfiguration produce = new ProducerConfiguration();
        produce.Client1("localhost", 12345);
    }
    
  
    public KafkaTemplate<String,String> kafkaTemplate(){
        return new KafkaTemplate<>(producerConfig());
    }
    
    
    public ProducerFactory<String,String> producerConfig(){
        
        Map<String,Object> config = new HashMap<>();
        
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
        
    }
    
}