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

import javax.xml.catalog.Catalog;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public AddEntryScreen() {
    }
    
    public void initialize(){
        
        
    }

    public void finishEntry(ActionEvent actionEvent) {
        //split the urls by delimiter and put in arraylist
        String[] urlTextArray = urlTextArea.getText().split(";");
        ArrayList<String> urlTextArraylist = new ArrayList<String>(Arrays.asList(urlTextArray));

        //use method to entry add to file
        categoryHandler.addEntry(Categorytextfield.getText(), nameTextField.getText(), urlTextArraylist);


        //get stage
        Stage stage = (Stage) nameTextField.getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../controller/view/sample.fxml")); //load parent node from fxml
            stage.setTitle("Peoplepedia");
            stage.setScene(new Scene(root, 800, 500)); //create scene with parent node and set the scene on the stage
            stage.show(); // show stage
        }catch (IOException ioException){
            ioException.printStackTrace();
        }

    }
}
