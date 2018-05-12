package com.kwietniewski.clientcebs;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class DetailController implements Initializable {
    
    // Layouts
    private final static String booking = "/fxml/Booking.fxml";
    
    MainApp model = new MainApp();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    
    
    @FXML
    private void backButton(ActionEvent event) throws IOException {
        model.changeScene(booking);
    }
    
}
