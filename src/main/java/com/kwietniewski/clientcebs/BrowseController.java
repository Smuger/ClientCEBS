package com.kwietniewski.clientcebs;

import java.io.IOException;
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


public class BrowseController implements Initializable {
    
    // Layouts
    private final static String booking = "/fxml/Booking.fxml";
    private final static String login = "/fxml/Login.fxml";
    
    public static int seats;
    
    @FXML
    private ListView listView;
    
    @FXML
    private Label nameOfExcursion;
    
    @FXML
    private Label labelSlider;
    
    @FXML
    private DatePicker datePicker;
    
    @FXML
    private Button book;
    
    @FXML
    private Button logout;
    
    @FXML
    private Button myBookings;
    
    @FXML
    private TextField search;
    
    @FXML
    private Slider slider;

    
    @FXML 
    public void handleMouseClick(MouseEvent arg0) {
        nameOfExcursion.setText(listView.getSelectionModel().getSelectedItem().toString());
        nameOfExcursion.setVisible(true);
        datePicker.setVisible(true);
        book.setVisible(true);
        slider.setVisible(true);
        labelSlider.setVisible(true);
        
    }
    
    @FXML
    public void newTickedValue(MouseEvent event){
        String ticketsNumber = Double.toString(slider.getValue());
        ticketsNumber = ticketsNumber.replace(".0" , "");
        System.out.println(ticketsNumber);
        labelSlider.setText("Number of tickets: " + ticketsNumber);
        seats = Integer.parseInt(ticketsNumber);
        
    }
    
    MainApp model = new MainApp();
    //ResultController resultController = new ResultController();
    
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
        System.out.println("Search phrase: " + phrase);
        model.searchDataHandler(phrase);
        /*System.out.print("Populating List View");
        resultController.populateListView();
        System.out.print("Populating List View ended");*/
        listView.setVisible(true);
        model.currentCustomer();
        clearListView();
        populateListView();
        
    }
    
    @FXML
    private void bookButton(ActionEvent event) throws JSONException, IOException{
        String name = listView.getSelectionModel().getSelectedItem().toString();
        LocalDate date = datePicker.getValue();
        System.out.print("DATA: " + date);
        model.book(name, seats, Long.MIN_VALUE, date);
        
    }
    
    private void populateListView() throws JSONException{
        listView.getItems().addAll(model.nameOfAllExcursions());
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }
    
    private void clearListView(){
        listView.getItems().clear();
    }
}
