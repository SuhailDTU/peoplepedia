package controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.CategoryHandler;
import model.Conversion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class AddEntryScreen {

    @FXML
    public TextField Categorytextfield;
    @FXML
    public TextField nameTextField;
    @FXML
    public TextField UrlText;
    @FXML
    public Button addEntryButton;
    @FXML
    public ListView<String> UrlListview;

    @FXML
    public Button cancelButton;

    Conversion converter = new Conversion();
    CategoryHandler categoryHandler = new CategoryHandler();

    //Reference to Scene and controller of the mainscreen.
    //this is used to return to the current instance of the mainscreen instead of creating a new one
    Scene mainScene;
    Controller mainScreenController;

    String listingFileName;
    String encodedFileName;

    ArrayList<String> urlList = new ArrayList<String>();


    public AddEntryScreen() {
    }
    
    public void initialize(){
    }

    public void setMainScreenController(Controller mainScreenController) {
        this.mainScreenController = mainScreenController;
    }

    public void setMainScene(Scene mainScene) {
        this.mainScene = mainScene;
    }

    public void setListingFileName(String listingFileName) {
        this.listingFileName = listingFileName;
    }

    public void setEncodedFileName(String encodedFileName) {
        this.encodedFileName = encodedFileName;
    }

    public void addToListView(String addition){
        urlList.add(addition);//add to array list
        UrlListview.getItems().clear(); //clear listview
        UrlListview.setItems(FXCollections.observableArrayList(urlList)); //set updated arraylist as listview
    }

    public void removeFromListView(String removal){
        urlList.remove(removal);//add to array list
        UrlListview.getItems().clear(); //clear listview
        UrlListview.setItems(FXCollections.observableArrayList(urlList)); //set updated arraylist as listview
    }

    public void clearListView(){
        urlList.clear();
        UrlListview.getItems().clear(); //clear listview
        UrlListview.setItems(FXCollections.observableArrayList(urlList)); //set updated arraylist as listview
    }

    public void finishEntry(ActionEvent actionEvent) {
        //only add entry if name and Catagory not empty
        if(!(nameTextField.getText().isEmpty()) && !(Categorytextfield.getText().isEmpty()) ) {

            /*
            //split the urls by delimiter and put in arraylist
            String[] urlTextArray = urlTextArea.getText().split(";");
            ArrayList<String> urlTextArraylist;

            //if the field was empty give empty arraylist instead of arraylist of lenght 1 with empty string
            if (urlTextArray.length == 1 && urlTextArray[0].isEmpty()) {
                urlTextArraylist = new ArrayList<String>();

            } else { //proceed as normal
                urlTextArraylist = new ArrayList<String>(Arrays.asList(urlTextArray));
            }
            */

            //use method to entry add to file
            categoryHandler.addEntry(Categorytextfield.getText(), nameTextField.getText(), urlList,listingFileName,encodedFileName);

            //update lists to show changes
            mainScreenController.refreshListView();
        }

        //get stage
        Stage stage = (Stage) nameTextField.getScene().getWindow();;
        stage.setTitle("Peoplepedia");
        stage.setScene(mainScene); //use reference of main scene inorder to return to main screen

        stage.show(); // show stage

    }

    public void cancelEntry(ActionEvent actionEvent) {
        //get stage
        Stage stage = (Stage) nameTextField.getScene().getWindow();
        stage.setTitle("Peoplepedia");
        stage.setScene(mainScene); //use reference of main scene inorder to return to main screen

        stage.show(); // show stage

    }
    public void removeAction(ActionEvent actionEvent) {
        removeFromListView(UrlListview.getSelectionModel().getSelectedItem());
    }

    public void addAction(ActionEvent actionEvent) {
        addToListView(UrlText.getText());
        UrlText.clear();
    }
}
