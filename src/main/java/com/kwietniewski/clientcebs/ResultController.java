package com.kwietniewski.clientcebs;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class ResultController implements Initializable {
    
    // Layouts
    private final static String browse = "/fxml/Browse.fxml";
    
    MainApp model = new MainApp();
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    private void backButton(ActionEvent event) throws IOException{
        model.changeScene(browse);
    }
    
}
