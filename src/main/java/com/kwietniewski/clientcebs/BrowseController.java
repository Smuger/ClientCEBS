package com.kwietniewski.clientcebs;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

public class BrowseController implements Initializable {
    
    // Layouts
    private final static String booking = "/fxml/Booking.fxml";
    private final static String login = "/fxml/Login.fxml";
    private final static String result = "/fxml/Result.fxml";
    
    @FXML
    private TextField search;
    
    MainApp model = new MainApp();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    private void bookingButton(ActionEvent event) throws IOException{
        model.changeScene(booking);
    }
    
    @FXML
    private void logoutButton(ActionEvent event) throws IOException{
        model.changeScene(login);
    }
    
    @FXML
    private void searchButton(ActionEvent event) throws IOException{
        String phrase = search.getText().toString();
        System.out.println("Search phrase: " + phrase);
        model.search(phrase);
        model.changeScene(result);
    }
}
