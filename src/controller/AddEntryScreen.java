package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    public TextArea urlTextArea;
    @FXML
    public Button addEntryButton;

    Conversion converter = new Conversion();
    CategoryHandler categoryHandler = new CategoryHandler();

    //Reference to Scene and controller of the mainscreen.
    //this is used to return to the current instance of the mainscreen instead of creating a new one
    Scene mainScene;
    Controller mainScreenController;

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

    public void finishEntry(ActionEvent actionEvent) {
        //only add entry if name and Catagory not empty
        if(!(nameTextField.getText().isEmpty()) && !(Categorytextfield.getText().isEmpty()) ) {
            //split the urls by delimiter and put in arraylist
            String[] urlTextArray = urlTextArea.getText().split(";");
            ArrayList<String> urlTextArraylist;

            //if the field was empty give empty arraylist instead of arraylist of lenght 1 with empty string
            if (urlTextArray.length == 1 && urlTextArray[0].isEmpty()) {
                urlTextArraylist = new ArrayList<String>();

            } else { //proceed as normal
                urlTextArraylist = new ArrayList<String>(Arrays.asList(urlTextArray));
            }


            //use method to entry add to file
            categoryHandler.addEntry(Categorytextfield.getText(), nameTextField.getText(), urlTextArraylist);

            //update lists to show changes
            mainScreenController.refreshListView();
        }

        //get stage
        Stage stage = (Stage) nameTextField.getScene().getWindow();;
        stage.setTitle("Peoplepedia");
        stage.setScene(mainScene); //use reference of main scene inorder to return to main screen

        stage.show(); // show stage

    }
}
