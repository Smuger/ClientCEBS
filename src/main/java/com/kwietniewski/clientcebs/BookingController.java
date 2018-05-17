package com.kwietniewski.clientcebs;

import static com.kwietniewski.clientcebs.BrowseController.seats;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import org.json.JSONException;


public class BookingController implements Initializable {
    
    // Layouts
    private final static String browse = "/fxml/Browse.fxml";
    private final static String detail = "/fxml/Detail.fxml";
    
    MainApp model = new MainApp();
    
    @FXML
    private ListView listView;
    
    @FXML
    private Button refresh;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    private void backButton(ActionEvent event) throws IOException{
        model.changeScene(browse);
    }
    
    @FXML
    private void detailButton(ActionEvent event) throws IOException{
        model.changeScene(detail);
    }
    
    @FXML
    private void refreshButton(ActionEvent event) throws JSONException, IOException{
        try{
            model.currentCustomer();
        }
        catch(Exception ex){
            System.err.println("ERROR WHILE UPDATING CURRENT CUSTOMER " + ex);
        }
        clearListView();
        model.searchBookingHandler();
        ArrayList allBookedExcursionsNames = model.nameOfBookedExcursions();
        populateListView(allBookedExcursionsNames);  
    }
    
    @FXML
    private void deleteButton(ActionEvent event) throws JSONException, IOException{
        String name = listView.getSelectionModel().getSelectedItem().toString();
        model.deleteBooking(name);
    }
    
    private void populateListView(ArrayList allExcursionsNames) throws JSONException{
        listView.getItems().addAll(allExcursionsNames);
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }
    
    private void clearListView(){
        listView.getItems().clear();
    }
}
