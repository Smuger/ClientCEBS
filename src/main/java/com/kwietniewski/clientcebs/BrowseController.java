package com.kwietniewski.clientcebs;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.json.JSONException;
import java.lang.Math;
import java.time.LocalDate;
import java.util.ArrayList;
import org.apache.http.HttpException;
import org.apache.http.client.HttpResponseException;


public class BrowseController implements Initializable {
    
    // Layouts
    private final static String booking = "/fxml/Booking.fxml";
    private final static String login = "/fxml/Login.fxml";
    
    public static int seats = 1;
    
    @FXML
    private ListView listView;
    
    @FXML
    private Label nameOfExcursion;
    
    @FXML
    private Label labelSlider;
    
    @FXML
    private Label error;
    
    @FXML
    private DatePicker datePicker;
    
    @FXML
    private Button book;
    
    @FXML
    private Button logout;
    
    @FXML
    private Button myBookings;
    
    @FXML
    private Button delete;
    
    @FXML
    private TextField search;
    
    @FXML
    private Slider slider;

    @FXML 
    public void handleMouseClick(MouseEvent arg0) throws IOException, JSONException {
        try{
            nameOfExcursion.setText(listView.getSelectionModel().getSelectedItem().toString());
            nameOfExcursion.setVisible(true);
            datePicker.setVisible(true);
            book.setVisible(true);
            slider.setVisible(true);
            labelSlider.setVisible(true);
        }
        
        catch(NullPointerException ex){
            error.setText("No excursion selected");
            error.setVisible(true);
        }
        
        String role = model.currentCustomer();
        if (role.equals("ADMIN")){

                delete.setVisible(true);
            }
        
    }
    
    @FXML
    public void newTickedValue(MouseEvent event){
        String ticketsNumber = Double.toString(slider.getValue());
        ticketsNumber = ticketsNumber.replace(".0" , "");
      
        labelSlider.setText("Number of tickets: " + ticketsNumber);
        seats = Integer.parseInt(ticketsNumber);
        
    }
    
    MainApp model = new MainApp();
    //ResultController resultController = new ResultController();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }
    
    @FXML
    private void bookingButton(ActionEvent event) throws IOException{
        model.changeScene(booking);
    }
    
    @FXML
    private void logoutButton(ActionEvent event) throws IOException{
        System.out.println("LOGOUT");
        model.changeScene(login);
        model.disconnect();
        listView.setVisible(false);
        nameOfExcursion.setVisible(false);
        datePicker.setVisible(false);
        book.setVisible(false);
    }
    
    @FXML
    private void backButton(ActionEvent event) throws IOException{
        listView.setVisible(false);
        nameOfExcursion.setVisible(false);
        datePicker.setVisible(false);
        book.setVisible(false);
        logout.setVisible(true);
        myBookings.setVisible(true);
    }
    
    @FXML
    private void searchButton(ActionEvent event) throws IOException, ProtocolException, MalformedURLException, JSONException{
        String phrase = search.getText().toString();
        
        model.searchDataHandler(phrase);
        System.out.println("SEARCH FOR: " + phrase);
        
        listView.setVisible(true);
        error.setVisible(false);
        
        clearListView();
        
        System.out.println("GET NAMES OF EXCURSIONS");
        ArrayList allExcursionsNames = model.nameOfAllExcursions();
        populateListView(allExcursionsNames);
        System.out.println("LISTVIEW POPULATED");
    }
    
    
    
    @FXML
    private void bookButton(ActionEvent event) throws JSONException, IOException{
        String name = listView.getSelectionModel().getSelectedItem().toString();
        LocalDate date = datePicker.getValue();
       
        if (date.isAfter(LocalDate.now())){
            int returnValue = model.book(name, seats, date);
            listView.setVisible(false);
            nameOfExcursion.setVisible(false);
            datePicker.setVisible(false);
            book.setVisible(false);
            slider.setVisible(false);
            labelSlider.setVisible(false);
            error.setVisible(true);
            delete.setVisible(false);

            if (returnValue == 1)
            {
                error.setText("This account has book this trip already");
                error.setVisible(true);

            }
            error.setText("Booking success");
            error.setVisible(true);
        }
        else{
            error.setText("Date not available");
            error.setVisible(true);
        }

        
    }
    
    @FXML
    private void deleteHandle(ActionEvent event) throws IOException, UnsupportedEncodingException, JSONException{
        String name = listView.getSelectionModel().getSelectedItem().toString();
        LocalDate date = datePicker.getValue();
       
        model.delete(name, date);
  
    }
    
    private void populateListView(ArrayList allExcursionsNames) throws JSONException{
        listView.getItems().addAll(allExcursionsNames);
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }
    
    private void clearListView(){
        listView.getItems().clear();
    }
}
