package com.kwietniewski.clientcebs;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.json.JSONException;
import org.json.JSONObject;

public class MainApp extends Application{
    public static Stage window;
    public static CloseableHttpClient client;
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
    
    public void loginDataHandler(String email, String password) throws IOException{
        connect();
        String urlParameters = "http://localhost:8181/api/customers/login?" + "email="+email+"&password="+password;
        postPARAM(urlParameters);

    }
    
    public void searchDataHandler(String phrase) throws IOException{
        String url = "http://localhost:8181/api/excursions/findAll?"+"word="+phrase;
        getJSON(url);
        
    }
    
    // Data transfer
    
    public HttpResponse postJSON(String url, JSONObject json) throws IOException{
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
        //HttpResponse response = httpClient.execute(httpPost);
        
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("\nSending 'POST' JSON request to URL : " + httpPost.toString());
	System.out.println("Post parameters : " + json.toString());
        System.out.println("Response Code : " + statusCode);
        System.out.println("Response value: " + response.toString());
        
        return response;
    }
    
    public HttpResponse getJSON(String url) throws UnsupportedEncodingException, IOException{
        // GET
        HttpGet httpGet = new HttpGet(url);
        //StringEntity params = new StringEntity(json.toString());
        httpGet.addHeader("content-type", "application/json");
        //httpGet.setEntity(params);
        HttpResponse response = client.execute(httpGet);
        
        int statusCode = response.getStatusLine().getStatusCode();
        String responseString = new BasicResponseHandler().handleResponse(response);
        System.out.println("\nSending 'POST' JSON request to URL : " + httpGet.toString());
        System.out.println("Response Code : " + statusCode);
        System.out.println("Response value: " + response.toString());
        System.out.println("Response body: " + responseString);
        System.out.println("Client " + client);
        return response;
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
