package com.kwietniewski.clientcebs;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLEngineResult;
import static org.apache.http.HttpHeaders.USER_AGENT;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sun.misc.IOUtils;


public class MainApp extends Application {
    
    public static Stage window;

    @Override
    public void start(Stage window) throws Exception {
        
        this.window = window;
        
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        
        Scene scene = new Scene(root);
        //scene.getStylesheets().add("/styles/login.css");
        
        window.setTitle("JavaFX and Maven");
        window.setScene(scene);
        window.show();
    }
       
    public void changeScene (String fxml) throws IOException{
        try{
            
            Parent newroot = FXMLLoader.load(getClass().getResource(fxml));
            window.getScene().setRoot(newroot);
            
        }
        catch(Exception ex){
            System.err.println(fxml);
            System.err.println(ex);
        }
    }
    public int register(String name, String email, String password, int cabineNumber, String role ) throws Exception{
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("email", email);  
        json.put("password", password);   
        json.put("cabineNumber", cabineNumber);  
        json.put("role", role);   
        HttpPost request = new HttpPost("http://localhost:8181/api/customers/register?");
        
        return postJSON(request, json);
    }
    
    public int login(String email, String password) throws IOException{
        String url = "http://localhost:8181/api/customers/login?";
        String urlParameters = "email="+email+"&password="+password;
        
        return postPARAM(url, urlParameters);
    }
    
    public int search(String phrase) throws ProtocolException, IOException{
        String url = "http://localhost:8181/api/excursions/findOne?";
        String urlParameters = "id="+phrase;
        return getPARAM(url, urlParameters);
    }
    
    public int postJSON(HttpPost request, JSONObject json) throws JSONException, IOException{
         

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        try {
        StringEntity params = new StringEntity(json.toString());
        request.addHeader("content-type", "application/json");
        request.setEntity(params);
        HttpResponse response = httpClient.execute(request);
        
        int statusCode = response.getStatusLine().getStatusCode();
    
        System.out.println("\nSending 'POST' JSON request to URL : " + request.toString());
	System.out.println("Post parameters : " + json.toString());
        System.out.println("Response Code : " + statusCode);
        System.out.println("Response value: " + response.toString());
        return statusCode;
        } 
        catch (Exception ex) {
            System.err.println(ex);
            // handle exception here
        } 
        finally {
        httpClient.close();
        }
        return 0;
    }
   
    public int postPARAM(String url, String urlParameters) throws MalformedURLException, ProtocolException, IOException{
        
	URL obj = new URL(url);
	HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        
	//add reuqest header
	con.setRequestMethod("POST");
	con.setRequestProperty("User-Agent", USER_AGENT);
	con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		
	// Send post request
	con.setDoOutput(true);
	DataOutputStream wr = new DataOutputStream(con.getOutputStream());
	wr.writeBytes(urlParameters);
	wr.flush();
	wr.close();

	int responseCode = con.getResponseCode();
	System.out.println("\nSending " + "GET" + " request to URL : " + url);
	System.out.println("Response Code : " + responseCode);
        System.out.println("Post parameters : " + urlParameters);

	BufferedReader in = new BufferedReader(
	        new InputStreamReader(con.getInputStream()));
	String inputLine;
	StringBuffer response = new StringBuffer();

	while ((inputLine = in.readLine()) != null) {
		response.append(inputLine);
	}
	in.close();
		
	//print result
	System.out.println(response.toString());
        System.out.println(responseCode);
        return responseCode;
    }
    
    public int getPARAM(String url, String urlParameters) throws MalformedURLException, ProtocolException, IOException{
        
	URL obj = new URL(url);
	HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        
	//add reuqest header
	con.setRequestMethod("GET");
	con.setRequestProperty("User-Agent", USER_AGENT);
	con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		
	// Send post request
	con.setDoOutput(true);
	DataOutputStream wr = new DataOutputStream(con.getOutputStream());
	wr.writeBytes(urlParameters);
	wr.flush();
	wr.close();

	int responseCode = con.getResponseCode();
	System.out.println("\nSending " + "GET" + " request to URL : " + url);
	System.out.println("Response Code : " + responseCode);
        System.out.println("Post parameters : " + urlParameters);

	BufferedReader in = new BufferedReader(
	        new InputStreamReader(con.getInputStream()));
	String inputLine;
	StringBuffer response = new StringBuffer();

	while ((inputLine = in.readLine()) != null) {
		response.append(inputLine);
	}
	in.close();
		
	//print result
	System.out.println(response.toString());
        System.out.println(responseCode);
        return responseCode;
    }
    
    public static void main(String[] args) {
        launch(args);
    }

}
