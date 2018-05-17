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
import org.apache.http.client.methods.HttpDelete;
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
    public static JSONArray JSONResultBooking;
    public static int id;
    public static int exID;
    public static String role;
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
 
    }
    
    public static void rememberResultJSONBooking(JSONArray localJSONREsult){
        MainApp.JSONResultBooking = localJSONREsult;
 
    }
    
    public static ArrayList nameOfAllExcursions() throws JSONException{
        //ArrayList<JSONObject> contentsAsJsonObjects = new ArrayList<JSONObject>();
        ArrayList<String> results = new ArrayList<String>();
        
       
        int jsonLenght = JSONResult.length();
        
        for(int i=0; i<jsonLenght; i++) 
        { 
        JSONObject json = JSONResult.getJSONObject(i);
        results.add(json.getString("name")); 
        }
       System.out.println("ALL NAMES ADDED");
        
        return results;
    }
    
    public static ArrayList nameOfBookedExcursions() throws JSONException{
        ArrayList<String> results = new ArrayList<String>();
        String simpleExName;
        int jsonLenght = JSONResultBooking.length();
        System.out.println("Lenght of json: " + jsonLenght);
        for(int i=0; i<jsonLenght; i++) 
        { 
        JSONObject json = JSONResultBooking.getJSONObject(i);
        simpleExName = (json.getJSONObject("trips").getJSONObject("excursion").getString("name"));
                
        results.add(simpleExName); 
        }
        System.out.println(results);
   
        return results;
    }
    
    public CloseableHttpClient connect(){
        if (client == null){
            PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
            CloseableHttpClient client = HttpClients.custom()
            .setConnectionManager(connManager).build();
                       rememberClient(client); 
            System.out.println("CLIENT CREATED " + client);
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
        
        connect();
        postJSON(url, json);
     
    }
        
    public String currentCustomer() throws IOException, JSONException{
        String jsonResponse = getJSON("http://localhost:8181/api/customers/current?");
        JSONObject json = new JSONObject(jsonResponse);
        
        MainApp.id = json.getInt("id");
        System.out.println("CURRENT USER ID: " + id);
        MainApp.role = json.getString("role");
       
        return role;
        //roleHandle();
        /*
        MainApp.role = json.getString("role");
        System.out.println(role);*/
    }
    /*
    public void roleHandle() throws IOException, JSONException{
        String jsonResponseRole = getJSON("http://localhost:8181/api/customers/fineOne?"+"id="+id);
        JSONObject json = new JSONObject(jsonResponseRole);
        MainApp.id = json.getInt("id");
        
    }
    */
    public int currentExcursion(String name) throws IOException, JSONException{
       
        name = name.replaceAll(" ", "+");
        String jsonResponse = getJSON("http://localhost:8181/api/excursions/findAll?" + "word="+name);

        jsonResponse = jsonResponse.replace("[", "").replace("]", "");

        JSONObject json = new JSONObject(jsonResponse);
        MainApp.exID = json.getInt("id");
        
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
        String responseString = getJSON(url);

        JSONArray localJSONREsult = new JSONArray(responseString);
        rememberResultJSON(localJSONREsult);
        //resultController.populateListView();
    }
    
    public void searchBookingHandler() throws IOException, UnsupportedEncodingException, JSONException{
 
        String url = "http://localhost:8181/api/bookings/findAllByCustomerId?"+"id="+id;
        String responseString = getJSON(url);
        System.out.println("BOOKING RESULTS: " + responseString);
        JSONArray localJSONREsult = new JSONArray(responseString);
        rememberResultJSONBooking(localJSONREsult);
        //resultController.populateListView();
    }
    
    public int book(String name, int seats, LocalDate date) throws JSONException, IOException{
       
        currentExcursion(name);
        JSONObject json = new JSONObject();
        JSONObject trip = new JSONObject();
        trip.put("excursionId", exID);
        trip.put("date", date);
        json.put("customerId", id);
        json.put("seats", seats);
        json.put("trip", trip);
        String url = "http://localhost:8181/api/bookings/create?";
       
        
        String reponse = postJSON(url, json);
       /*
        if (reponse != "ok")
        {
            return 1;
        }
*/
        return 0;
    }
    
    public void delete(String name, LocalDate date) throws IOException, UnsupportedEncodingException, JSONException{
        currentExcursion(name);
        String url = "http://localhost:8181/api/trips/findOneByExcursionIdAndDate?"+"excursionId="+exID+"&"+"date="+date;
       
        String jsonResponse = getJSON(url);
        JSONObject json = new JSONObject(jsonResponse);
        int tripID = json.getInt("id");
       
        String url2 = "http://localhost:8181/api/trips/delete?"+"id="+tripID;
        deletePARAM(url2);
        

    }
    
    // Data transfer
    
    public String postJSON(String url, JSONObject json) throws IOException{
        // POST
        
        System.out.println("DATA TO SEND");
        System.out.println(json.toString());
        
        HttpPost httpPost = new HttpPost(url);
        System.out.println("HTTP POST ESTABLISHED");
        
        StringEntity params = new StringEntity(json.toString());
        System.out.print("DATA TRANSFER ");
        
        httpPost.addHeader("content-type", "application/json");
        httpPost.setEntity(params);
        System.out.println("| DATA TYPE JSON");
        
        HttpResponse response = client.execute(httpPost);
        System.out.println(response);
        System.out.println("RESPONSE RECEIVED");
        
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("STATUS CODE: " + statusCode);
        
        String responseString = new BasicResponseHandler().handleResponse(response);
        System.out.println("CONVERT RESPONSE TO STRING");
      
        return responseString;
    }
    
    public String getJSON(String url) throws UnsupportedEncodingException, IOException, JSONException{
        // GET
        
        System.out.println("DATA TO SEND IN URL");
        
        HttpGet HttpGet = new HttpGet(url);
        System.out.println("HTTP GET ESTABLISHED");

        //HttpGet.addHeader("content-type", "application/json");
        
        HttpResponse response = client.execute(HttpGet);
        //System.out.println(response);
        System.out.println("RESPONSE RECEIVED");
        
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("STATUS CODE: " + statusCode);
        
        String responseString = new BasicResponseHandler().handleResponse(response);
        System.out.println("CONVERT RESPONSE TO STRING");
           
        return responseString;
    }
    
    public HttpResponse postPARAM(String url) throws IOException{
        
        HttpPost httpPost = new HttpPost(url);
        System.out.println("HTTP POST ESTABLISHED");
        
        System.out.print("DATA TRANSFER ");
        System.out.println("| DATA TYPE JSON");
        
        HttpResponse response = client.execute(httpPost);
        System.out.println("RESPONSE RECEIVED");
        
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("STATUS CODE: " + statusCode);
        
        return response;
    }
    
    public HttpResponse getPARAM(String url) throws IOException{
        HttpGet HttpGet = new HttpGet(url);
        System.out.println("HTTP GET ESTABLISHED");
        
        System.out.print("DATA TRANSFER ");
        System.out.println("| DATA TYPE JSON");
        
        HttpResponse response = client.execute(HttpGet);
        System.out.println("RESPONSE RECEIVED");
        
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("STATUS CODE: " + statusCode);
      
        return response;
    }
    
    public HttpResponse deletePARAM(String url) throws IOException{
        HttpDelete HttpDelete = new HttpDelete(url);
        System.out.println("HTTP DELETE ESTABLISHED");
        
        System.out.print("DATA TRANSFER ");
        System.out.println("| DATA TYPE JSON");
        
        HttpResponse response = client.execute(HttpDelete);
        System.out.println("RESPONSE RECEIVED");
        
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("STATUS CODE: " + statusCode);
      
        return response;
    }
    
    public void disconnect() throws IOException{
        client.close();
        rememberClient(null);
        
    }
}
