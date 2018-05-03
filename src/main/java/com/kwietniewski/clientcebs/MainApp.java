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
import static org.apache.http.HttpHeaders.USER_AGENT;


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
    public void register(String name, String email, String password, int cabineNumber, String role ) throws Exception{
        
    }
    
    public int login(String email, String password) throws IOException{
        String url = "http://localhost:8181/api/customers/login?";
        post(url, email, password);
        return 1;
    }
   
    public void post(String url, String email, String password) throws MalformedURLException, ProtocolException, IOException{
        
	URL obj = new URL(url);
	HttpURLConnection con = (HttpURLConnection) obj.openConnection();

	//add reuqest header
	con.setRequestMethod("POST");
	con.setRequestProperty("User-Agent", USER_AGENT);
	con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

	String urlParameters = "email="+email+"&password="+password;
		
	// Send post request
	con.setDoOutput(true);
	DataOutputStream wr = new DataOutputStream(con.getOutputStream());
	wr.writeBytes(urlParameters);
	wr.flush();
	wr.close();

	int responseCode = con.getResponseCode();
	System.out.println("\nSending 'POST' request to URL : " + url);
	System.out.println("Post parameters : " + urlParameters);
	System.out.println("Response Code : " + responseCode);

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
    }
    
    public static void main(String[] args) {
        launch(args);
    }

}
