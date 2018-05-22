package com.kwietniewski.clientcebs;

import static com.kwietniewski.clientcebs.RegisterController.user;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.apache.http.client.HttpResponseException;
import org.json.JSONException;

public class DetailController implements Initializable {
    
    // Layouts
    private final static String booking = "/fxml/Booking.fxml";
    
    @FXML
    private TextField name;
    
    @FXML
    private TextField email;
    
    @FXML
    private TextField password1;
    
    @FXML
    private Label error;
    
    MainApp model = new MainApp();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    @FXML 
    public void handleMouseClick(MouseEvent arg0) throws IOException, JSONException {
        if(name.getText().equals("") && email.getText().equals("") && password1.getText().equals("")){
            System.out.println("TextField empty");
            model.currentCustomer();
            name.setText(model.CurrentUsername);
            email.setText(model.CurrentUseremail);
        }
        else{
            System.out.println("TextField populated");
        }
        
    
    }

    @FXML
    private void updateButton(ActionEvent event) throws JSONException, IOException{
        try{
            model.updateDataHandler(name.getText(), email.getText(), password1.getText(), 1, user);
            error.setText("User data updated");
        }
        catch (HttpResponseException ex){
            error.setText("Password is incorrect");
        }
        
        
    }

    
    @FXML
    private void backButton(ActionEvent event) throws IOException {
        model.changeScene(booking);
    }
    
}
