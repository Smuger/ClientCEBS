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
    private final static String booking = "/fxml/Booking.fxml";

    
    MainApp model = new MainApp();
    
    @FXML
    private ListView listView;
    
    @FXML
    private Button refresh;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try{
            System.out.println("REFRESH STARTS");
            model.currentCustomer();
            System.out.println("CHECK CURRENT CUSTOMER");
            clearListView();
            System.out.println("CLEAN LIST VIEW");
            model.searchBookingHandler();
            System.out.println("NEW BOOKING SEARCH");
            ArrayList allBookedExcursionsNames = model.nameOfBookedExcursions();
            System.out.println("GET NAMES");
            populateListView(allBookedExcursionsNames); 
            System.out.println("POPULATE LIST VIEW");
        }
        catch(Exception ex){
            System.err.println("Refresh fail");
        }
        
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
    private void deleteButton(ActionEvent event) throws JSONException, IOException{
        try{
            String name = listView.getSelectionModel().getSelectedItem().toString();
            System.out.println("DELETE THIS TRIP FROM MY BOOKING LIST " + name);
            model.deleteBooking(name);
            clearListView();
            System.out.println("REFRESH STARTS");
            model.currentCustomer();
            System.out.println("CHECK CURRENT CUSTOMER");
            clearListView();
            System.out.println("CLEAN LIST VIEW");
            model.searchBookingHandler();
            System.out.println("NEW BOOKING SEARCH");
            ArrayList allBookedExcursionsNames = model.nameOfBookedExcursions();
            System.out.println("GET NAMES");
            populateListView(allBookedExcursionsNames); 
            System.out.println("POPULATE LIST VIEW");
            
         

        }
        catch(Exception ex){
            model.changeScene(browse);
            model.changeScene(booking);
            System.err.println(listView.getSelectionModel().getSelectedItem().toString() + " " + ex);
        }

    }
    
    private void populateListView(ArrayList allExcursionsNames) throws JSONException{
        listView.getItems().addAll(allExcursionsNames);
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }
    
    private void clearListView(){
        listView.getItems().clear();
    }
}
