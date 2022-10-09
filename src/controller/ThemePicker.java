package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.EaseOfUse;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ThemePicker {
    @FXML
    public ListView<String> hexview1;
    @FXML
    public ListView<String> hexview2;
    @FXML
    public TextField hexfield1;
    @FXML
    public TextField hexfield2;
    @FXML
    public Circle display1;
    @FXML
    public Circle display2;

    //Arraylists for creation of observable lists for listviews
    ArrayList<String> hexColorList1 = new ArrayList<String>();
    ArrayList<String> hexColorList2 = new ArrayList<String>();

    //main scene references for returning and changing values
    private Controller mainController;
    private Scene mainScene;

    //for convinient functions
    EaseOfUse easeOfUse = new EaseOfUse();

    //these are used to get color of default choices
    @FXML
    public RadioButton colorChoice1;
    @FXML
    public RadioButton colorChoice2;
    @FXML
    public RadioButton colorChoice3;
    @FXML
    public Circle choice1c1;
    @FXML
    public Circle choice1c2;
    @FXML
    public Circle choice2c1;
    @FXML
    public Circle choice2c2;
    @FXML
    public Circle choice3c1;
    @FXML
    public Circle choice3c2;

    public ThemePicker(){

    }
    public void initialize(){
        //bind all radiobuttons together in same toggle group
        ToggleGroup toggleGroup = new ToggleGroup();
        colorChoice1.setToggleGroup(toggleGroup);
        colorChoice2.setToggleGroup(toggleGroup);
        colorChoice3.setToggleGroup(toggleGroup);

        hexview1.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String prev, String now) {
                if(now != null) {
                    display1.setFill(Color.web(now));
                }
            }
        });
        hexview2.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String prev, String now) {
                if(now != null) {
                    display2.setFill(Color.web(now));
                }
            }
        });

        if(Files.exists(Paths.get(easeOfUse.getDirPath()+"/themeSettingsv2.txt"))){
            //read data from savefile if it exists
            readData();
        }else {
            //add all the default values if it doesn't
            hexColorList1.add("#c06c84");
            hexColorList1.add("#1e5635");
            hexColorList1.add("#03254c");

            hexColorList2.add("#f67280");
            hexColorList2.add("#a4de02");
            hexColorList2.add("#2a9df4");

        }
        //update Listview and select first value
        updateListView(hexview1, hexColorList1);
        updateListView(hexview2, hexColorList2);
        hexview1.getSelectionModel().selectFirst();
        hexview2.getSelectionModel().selectFirst();


    }

    public void updateListView(ListView listView, ArrayList arrayList){
            listView.getItems().clear();
            listView.setItems(FXCollections.observableArrayList(arrayList));
    }

    public void setMainController(Controller mainController) {
        this.mainController = mainController;
    }

    public void setMainScene(Scene mainScene) {
        this.mainScene = mainScene;
    }

    public void addToHexView1(ActionEvent actionEvent) {
        hexColorList1.add(hexfield1.getText());
        hexfield1.clear();
        updateListView(hexview1, hexColorList1);
    }

    public void addToHexView2(ActionEvent actionEvent) {
        hexColorList2.add(hexfield2.getText());
        hexfield2.clear();
        updateListView(hexview2, hexColorList2);
    }

    public void removeFromHexView1(ActionEvent actionEvent) {
    }

    public void removeFromHexView2(ActionEvent actionEvent) {
    }

    public void returnAndSave(ActionEvent actionEvent) {
        //write the contents of the custom color lists to a file
        saveData();

        //save chosen color
        EaseOfUse.writeToFile(hexview1.getSelectionModel().getSelectedItem()+"\n"+hexview2.getSelectionModel().getSelectedItem(), easeOfUse.getDirPath()+"/themeSettingsv2Chosen.txt");
        //set chosen Theme
        mainController.setTheme(hexview1.getSelectionModel().getSelectedItem(), hexview2.getSelectionModel().getSelectedItem());

        //return
        Stage stage = (Stage)hexfield1.getScene().getWindow();
        stage.setTitle("Peoplepedia");
        stage.setScene(mainScene);
        stage.show();


    }
    public void saveData(){
        String customlist1 = "";
        String customlist2 = "";
        String finalData;
        for (int i = 0; i < hexColorList1.size(); i++ ){
            customlist1 = customlist1 + hexColorList1.get(i) + "\n";
        }
        for (int i = 0; i < hexColorList2.size(); i++ ){
            customlist2 = customlist2 + hexColorList2.get(i) + "\n";
        }
        finalData = customlist1 + "-\n" + customlist2;
        EaseOfUse.writeToFile(finalData, easeOfUse.getDirPath() + "/themeSettingsv2.txt");
    }
    public void readData(){
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(easeOfUse.getDirPath() + "/themeSettingsv2.txt"));) {
            String line;
            while ((line = bufferedReader.readLine())!=null){
                if (line.matches("-")){
                    break;
                }
                hexColorList1.add(line);
            }
            while ((line = bufferedReader.readLine())!=null){
                hexColorList2.add(line);
            }

        }catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    public void setAsDefaultColor(ActionEvent actionEvent) {
        String colorHex1 = "";
        String colorHex2 = "";
        //depending on button selected work with a different color set
        if(colorChoice1.isSelected()){
            //make paint into hex
            colorHex1 = EaseOfUse.getHexFromPaint(choice1c1.getFill());
            colorHex2 = EaseOfUse.getHexFromPaint(choice1c2.getFill());
        }else if(colorChoice2.isSelected()){
            //make paint into hex
            colorHex1 = EaseOfUse.getHexFromPaint(choice2c1.getFill());
            colorHex2 = EaseOfUse.getHexFromPaint(choice2c2.getFill());
        }else if(colorChoice3.isSelected()){
            //make paint into hex
            colorHex1 = EaseOfUse.getHexFromPaint(choice3c1.getFill());
            colorHex2 = EaseOfUse.getHexFromPaint(choice3c2.getFill());
        }

        //check if colors are contained in list, if not add them. it check for both uppercase and lowercase
        if(!hexColorList1.contains(colorHex1) && !hexColorList1.contains(colorHex1.toLowerCase())){
            hexColorList1.add(colorHex1);
        }
        if (!hexColorList2.contains(colorHex2) && !hexColorList2.contains(colorHex2.toLowerCase())){
            hexColorList2.add(colorHex2);
        }
        //refresh list
        updateListView(hexview1, hexColorList1);
        updateListView(hexview2, hexColorList2);

        //select added colors
        hexview1.getSelectionModel().select((hexColorList1.indexOf(colorHex1) == -1) ? hexColorList1.indexOf(colorHex1.toLowerCase()): hexColorList1.indexOf(colorHex1));
        hexview2.getSelectionModel().select((hexColorList2.indexOf(colorHex2) == -1) ? hexColorList2.indexOf(colorHex2.toLowerCase()): hexColorList2.indexOf(colorHex2) );

        //i used a ternary operator
        //(statement) ? return this if true : return this if false
        //used when setting values depending on a variable

    }
}


