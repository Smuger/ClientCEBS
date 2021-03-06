package com.kwietniewski.clientcebs;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;


public class RegisterController implements Initializable {
    
    // Layouts
    private final static String login = "/fxml/Login.fxml";
    public static String user = "USER";
    
    @FXML
    private TextField passwd1, passwd2, name, email;
    @FXML
    private Text error;

    
    MainApp model = new MainApp();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    public void adminHandler(ActionEvent event){
        if (user == "USER"){
            user = "ADMIN";
        }
        else{
            user = "USER";
        }
    }
    
    @FXML
    private void backButton(ActionEvent event) throws IOException{
        model.changeScene(login);
    }
    
    @FXML
    private void registerButton(ActionEvent event) throws IOException{
        if (passwd1.getText().equals(passwd2.getText())){
            try{
            
            model.registerDataHandler(name.getText(), email.getText(), passwd1.getText(), 1, user);
            
            model.changeScene(login);
        }
        catch (Exception ex){
           System.out.println(ex);
            error.setText("One of textfields is empty");
          
        }
        }
        else {
            error.setText("password do not match");
        }
        
    }
}
