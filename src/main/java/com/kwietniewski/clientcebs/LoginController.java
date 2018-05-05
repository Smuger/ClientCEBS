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
    private void loginButton(ActionEvent event) throws Exception{
        // TO DO login fail handler
        
        try {
            model.login(email.getText(), password.getText());
            model.changeScene(browse);
        }
        catch(Exception ex){
            error.setText("Server Down");
        }
    }
}
