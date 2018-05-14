package com.kwietniewski.clientcebs;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.sound.midi.SysexMessage;
import jdk.nashorn.internal.objects.Global;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainApp extends Application{
    public static Stage window;
    public static CloseableHttpClient client;
    public static JSONArray JSONResult;
    public static int id;
    public static int exID;
    //ResultController resultController = new ResultController();
    // Window change handler
    
    public void start(Stage window) throws IOException{
        this.window = window;
        
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        
        Scene scene = new Scene(root);
        
        window.setTitle("JavaFX and Maven");
        window.setScene(scene);
        window.show();
        
    }
    
    public void changeScene(String fxml) throws IOException{
        Parent newroot = FXMLLoader.load(getClass().getResource(fxml));
        window.getScene().setRoot(newroot);
        
    }
    
    
    
    // Establishing connections
    
    public static void rememberClient(CloseableHttpClient localClient){
        MainApp.client = localClient;
        
    }
    
    public static void rememberResultJSON(JSONArray localJSONREsult){
        MainApp.JSONResult = localJSONREsult;
        System.out.print("JSON sending succes");
 
    }
    
    public static ArrayList nameOfAllExcursions() throws JSONException{
        //ArrayList<JSONObject> contentsAsJsonObjects = new ArrayList<JSONObject>();
        ArrayList<String> results = new ArrayList<String>();
        
        System.out.println(JSONResult.length());
        int jsonLenght = JSONResult.length();
        System.out.println("Lenght of json: " + jsonLenght);
        for(int i=0; i<jsonLenght; i++) 
        { 
        JSONObject json = JSONResult.getJSONObject(i);
        results.add(json.getString("name")); 
        }
        System.out.println(results);
        
        return results;
    }
    
    public CloseableHttpClient connect(){
        if (client == null){
            PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
            CloseableHttpClient client = HttpClients.custom()
            .setConnectionManager(connManager).build();
            System.out.println("New connection: " + client);
            rememberClient(client); 
        }
        
        else{
            System.err.println("User already login");
        }
        
        return client;
    }
    
    // Data handler
    
    public void registerDataHandler(String name, String email, String password, int cabineNumber, String role) throws JSONException, IOException{
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("email", email);  
        json.put("password", password);   
        json.put("cabineNumber", cabineNumber);  
        json.put("role", role);   
        String url = "http://localhost:8181/api/customers/register?";
        System.out.println("Register JSON created, sending to server");
        connect();
        postJSON(url, json);
     
    }
        
    public void currentCustomer() throws IOException, JSONException{
        String jsonResponse = getJSON("http://localhost:8181/api/customers/current?");
        JSONObject json = new JSONObject(jsonResponse);
        MainApp.id = json.getInt("id");
        System.out.println(id);
    }
    
    public int currentExcursion(String name) throws IOException, JSONException{
        System.out.println(name);
        name = name.replaceAll(" ", "+");
        String jsonResponse = getJSON("http://localhost:8181/api/excursions/findAll?" + "word="+name);

        jsonResponse = jsonResponse.replace("[", "").replace("]", "");

        JSONObject json = new JSONObject(jsonResponse);
        MainApp.exID = json.getInt("id");
        System.out.println(exID);
        return exID;
    }
    
    public void loginDataHandler(String email, String password) throws IOException{
        connect();
        String urlParameters = "http://localhost:8181/api/customers/login?" + "email="+email+"&password="+password;
        postPARAM(urlParameters);

    }
    
    public void searchDataHandler(String phrase) throws IOException, UnsupportedEncodingException, JSONException{
        phrase = phrase.replaceAll(" ", "+");
        String url = "http://localhost:8181/api/excursions/findAll?"+"word="+phrase;
        getJSON(url);
        //resultController.populateListView();
    }
    
    public void JSONArrayHandler(String responseString) throws JSONException{
        JSONArray localJSONREsult = new JSONArray(responseString);
        //JSONObject localJSONREsult = new JSONObject(responseString);
        System.out.println("JSON to save: " + localJSONREsult.toString());
        rememberResultJSON(localJSONREsult);
        System.out.println("JSON manipulation success");
    }
    
    public int book(String name, int seats, LocalDate date) throws JSONException, IOException{
        System.out.println("BOOKING STARTS: " + name + " " + seats + " " + date);
        currentExcursion(name);
        JSONObject json = new JSONObject();
        JSONObject trip = new JSONObject();
        trip.put("excursionId", exID);
        trip.put("date", date);
        json.put("customerId", id);
        json.put("seats", seats);
        json.put("trip", trip);
        String url = "http://localhost:8181/api/bookings/create?";
        System.out.println(json.toString());
        
        String reponse = postJSON(url, json);
        System.out.println("RESPONSE" + reponse);
        if (reponse == "error")
        {
            return 1;
        }
        return 0;
    }
    
    // Data transfer
    
    public String postJSON(String url, JSONObject json) throws IOException{
        // POST
        System.out.println("as a client: " + client);
        HttpPost httpPost = new HttpPost(url);
        System.out.println(httpPost);
        StringEntity params = new StringEntity(json.toString());
        System.out.println(params);
        httpPost.addHeader("content-type", "application/json");
        httpPost.setEntity(params);
        HttpResponse response = client.execute(httpPost);
        System.out.println(response);
        try{
            String responseString = new BasicResponseHandler().handleResponse(response);
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("\nSending 'POST' JSON request to URL : " + httpPost.toString());
            System.out.println("Post parameters : " + json.toString());
            System.out.println("Response Code : " + statusCode);
            System.out.println("Response value: " + response.toString());
            System.out.println("Response body: " + responseString);
       
        return responseString;
        }
        catch(HttpResponseException ex)
        {
            System.err.println("Double booking");
        }
        return "error";
    }
    
    public String getJSON(String url) throws UnsupportedEncodingException, IOException, JSONException{
        // GET
        HttpGet httpGet = new HttpGet(url);

        httpGet.addHeader("content-type", "application/json");

        HttpResponse response = client.execute(httpGet);
        
        int statusCode = response.getStatusLine().getStatusCode();
        String responseString = new BasicResponseHandler().handleResponse(response);

        System.out.println("\nSending 'POST' JSON request to URL : " + httpGet.toString());
        System.out.println("Response Code : " + statusCode);
        System.out.println("Response value: " + response.toString());
        System.out.println("Response body: " + responseString);
        System.out.println("Client: " + client);
        
        if(responseString.contains ("[")){
        JSONArrayHandler(responseString);
        }
        return responseString;
    }
    
    public HttpResponse postPARAM(String url) throws IOException{
        HttpPost httpPost = new HttpPost(url);
        System.out.println(httpPost);
        System.out.println(client);
        HttpResponse response = client.execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);
        
        return response;
    }
    
    public HttpResponse getPARAM(String url) throws IOException{
        HttpGet HttpGet = new HttpGet(url);
        HttpResponse response = client.execute(HttpGet);
        int statusCode = response.getStatusLine().getStatusCode();
      
        return response;
    }
    
    public void disconnect() throws IOException{
        client.close();
        rememberClient(null);
        
    }
}
