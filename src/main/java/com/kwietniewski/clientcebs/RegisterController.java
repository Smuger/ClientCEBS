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
    private void backButton(ActionEvent event) throws IOException{
        model.changeScene(login);
    }
    
    @FXML
    private void registerButton(ActionEvent event) throws IOException{
        try{
            model.register(name.getText(), email.getText(), passwd1.getText(), 1, "USER");
        }
        catch (Exception ex){
            System.err.println(ex);
            error.setText("Textfield empty");
        }
    }
}
