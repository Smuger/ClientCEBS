package com.kwietniewski.clientcebs;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.apache.http.client.HttpResponseException;

public class LoginController implements Initializable {
    
    @FXML
    private TextField email, password;
    
    @FXML
    private Text error;
    
    // Model
    MainApp model = new MainApp();
    
    // Layouts
    private final static String register = "/fxml/Register.fxml";
    private final static String browse = "/fxml/Browse.fxml";
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    private void registerButton(ActionEvent event) throws IOException{
        model.changeScene(register);
    }
    
    @FXML
    private void loginButton(ActionEvent event) throws IOException {
        System.out.println(email.getText()+ password.getText());
        int code = model.loginDataHandler(email.getText(), password.getText());
   
        if(code == 200){
            model.changeScene(browse);
        }
        else if (code == 404){
            error.setText("No internet connection");
        }
        else{
         
        error.setText("Wrong credentials");

        }
    }
}
