package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.CategoryHandler;
import model.Conversion;

import java.util.ArrayList;
import java.util.Arrays;

public class EditScreen {

    //To be deleted as part of edit
    public String category;
    public String name;
    public String commaSeperatedUrls;

    @FXML
    public TextField Categorytextfield;
    @FXML
    public TextField nameTextField;
    @FXML
    public TextArea urlTextArea;
    @FXML
    public Button EditEntryButton;
    @FXML
    public Button DeleteEntryButton;

    Conversion converter = new Conversion();
    CategoryHandler categoryHandler = new CategoryHandler();

    //Reference to Scene and controller of the mainscreen.
    //this is used to return to the current instance of the mainscreen instead of creating a new one
    private Scene mainScene;
    private Controller mainScreenController;

    public void setMainScene(Scene mainScene) {
        this.mainScene = mainScene;
    }

    public void setMainScreenController(Controller mainScreenController) {
        this.mainScreenController = mainScreenController;
    }

    public EditScreen() {
    }

    public void initialize(){
    }

    public void setCategorytextfield(String catagoryText) {
        this.Categorytextfield.setText(catagoryText);
    }

    public void setNameTextField(String nameText) {
        this.nameTextField.setText(nameText);
    }

    public void setUrlTextArea(String urlText) {
        this.urlTextArea.setText(urlText);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCommaSeperatedUrls(String commaSeperatedUrls) {
        this.commaSeperatedUrls = commaSeperatedUrls;
    }

    //this function deletes an entry and adds a new edited one inorder to simulate editing it in realtime. It then returns us to main screen
    public void finishEntry(ActionEvent actionEvent) {
        //delete old entry
        categoryHandler.deleteEntry(category, name, commaSeperatedUrls);

        //split the urls by delimiter and put in arraylist
        String[] urlTextArray = urlTextArea.getText().split(";");
        ArrayList<String> urlTextArraylist;

        //if the field was empty give empty arraylist instead of arraylist of length 1 with empty string
        if (urlTextArray.length == 1 && urlTextArray[0].isEmpty()){
            urlTextArraylist = new ArrayList<String>();
        }
        else{ //proceed as normal
            urlTextArraylist = new ArrayList<String>(Arrays.asList(urlTextArray));
        }

        //add new entry
        categoryHandler.addEntry(Categorytextfield.getText(), nameTextField.getText(), urlTextArraylist );

        //refresh main screen to reflect changes
        mainScreenController.refreshListView();

        //use references to main screen inorder to return to main screen
        Stage stage = (Stage) nameTextField.getScene().getWindow();
        stage.setTitle("Peoplepedia");
        stage.setScene(mainScene); //set scene with correct root node to stage
        stage.show(); // show stage

    }

    //this function deletes the entry and promtly returns to the mains screen
    public void DeleteEntry(ActionEvent actionEvent) {
        //delete entry
        categoryHandler.deleteEntry(category, name, commaSeperatedUrls);

        //refresh main screen to reflect changes
        mainScreenController.refreshListView();

        //use references to main screen inorder to return to main screen
        Stage stage = (Stage) nameTextField.getScene().getWindow();
        stage.setTitle("Peoplepedia");
        stage.setScene(mainScene); //create scene with parent node and set the scene on the stage
        stage.show(); // show stage

    }
}
