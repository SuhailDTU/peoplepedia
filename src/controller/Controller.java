package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import model.Categories;
import model.CategoryHandler;
import model.Conversion;
import model.Person;

import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Controller {

    @FXML
    public ListView<Categories> listvieww;
    public ListView<Person> listvieww2;
    public Label nameLabel;
    public VBox urlbox;
    public Button AddEntryButton;


    CategoryHandler catagoryHandler = new CategoryHandler();
    Conversion converter = new Conversion();

    ArrayList<Categories> categoriesList = new ArrayList<Categories>();
    ObservableList<Categories> categoriesObservableList;
    ObservableList<Person> personArraylist;



    public Controller(){
        converter.ConvertFromHexencodedFileToCommafile("src/encodedfile.txt", "src\\testpeoplepedia.txt");//convert from hex to comma seperated file "src/testpeoplepedia.txt"
        categoriesList = catagoryHandler.readincatagories("src/testpeoplepedia.txt"); //use converted file
        File listingfile = new File("src/testpeoplepedia.txt");// delete file after use
        listingfile.delete();




        categoriesObservableList = FXCollections.observableArrayList(categoriesList); //read in from commasepererated file and convert to Observable arraylist of catagories

        // testing if read in correct

        for (Categories catagory: categoriesList) {
            ArrayList<Person> people = catagory.getPeople();
            System.out.println("---"+catagory.getCatagoryname());
            for (Person person: people) {
                System.out.println("-"+person.getName());
                ArrayList<String> urls = person.getUrls();
                for (String url: urls) {
                    System.out.println(url);
                }
            }
        }
    }

    @FXML
    public void initialize(){
        listvieww.setItems(categoriesObservableList);// put observalble list in listview

        //we can only select one item at a time
        listvieww.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listvieww2.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        //set to cells shows catagory/person name
        listvieww.setCellFactory(new Callback<ListView<Categories>, ListCell<Categories>>() { //changes what is displayed in listview when containing objects
            @Override
            public ListCell<Categories> call(ListView<Categories> categoriesListView) {
                 ListCell<Categories>  categoriesListCell = new ListCell<Categories>(){
                    @Override
                    public void updateItem(Categories categories, boolean empty){
                        super.updateItem(categories, empty);
                        if (categories != null){
                            setText(categories.getCatagoryname());
                        }else{
                            setText("");
                        }

                    }
                 };

                 return categoriesListCell;

            }
        });
        listvieww2.setCellFactory(new Callback<ListView<Person>, ListCell<Person>>() {
            @Override
            public ListCell<Person> call(ListView<Person> personListView) {
                ListCell<Person> personListCell = new ListCell<Person>(){
                    @Override
                    public void updateItem(Person person, boolean empty){
                        super.updateItem(person, empty);
                        if (person != null){
                            setText(person.getName());
                        }else{
                            setText("");

                        }

                    }

                };
                return personListCell;
            }

        });

        updatePersonListcellAndData(); //set listeners on both listviews

    }

    public void updatePersonListcellAndData(){
        //we add a listener to the listview so that everytime a change happens it runs this code

        listvieww.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Categories>() {
            @Override
            public void changed(ObservableValue<? extends Categories> observableValue, Categories categories, Categories newcategory) {
                //we get the people array from the current selcted catagory and make an observable list

                if(newcategory != null) {
                    //we clear the listview and populate it with new items
                    personArraylist = FXCollections.observableArrayList(newcategory.getPeople());
                    listvieww2.getItems().clear();
                    listvieww2.setItems(personArraylist);
                    listvieww2.getSelectionModel().select(0);
                    listvieww2.getFocusModel().focus(0);
                }
            }
        });
        listvieww2.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Person>() {
            @Override
            public void changed(ObservableValue<? extends Person> observableValue, Person person, Person newPerson) {
                if (newPerson != null) {
                    createVboxData(newPerson);
                }
            }
        });

        listvieww.getSelectionModel().select(0);//set default to first item category listview


    }

    public void createVboxData(Person person){

        ArrayList<String> urlList = person.getUrls();

        nameLabel.setText(person.getName());

        urlbox.getChildren().clear();
        for (int i = 0; i < urlList.size(); i++){
            urlbox.getChildren().add(new Label(urlList.get(i)));

        }





    }


    public void acessNewEntryScreen(ActionEvent actionEvent) {
        Stage stage = (Stage) nameLabel.getScene().getWindow();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("../controller/view/AddEntryScreen.fxml"));

            stage.setScene(new Scene(root, 800, 500));
            stage.setTitle("Entry Adder");
            stage.show();
        }catch (IOException ioException){
            ioException.printStackTrace();
        }



    }

    public void edit(ActionEvent actionEvent) {
        Stage stage = (Stage) nameLabel.getScene().getWindow();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../controller/view/EditScreen.fxml"));
            Parent root = fxmlLoader.load();
            EditScreen editScreen = fxmlLoader.getController();

;
            //set as default values in textfield when editing entry
            editScreen.setCategorytextfield(listvieww.getSelectionModel().getSelectedItem().getCatagoryname());
            editScreen.setNameTextField(listvieww2.getSelectionModel().getSelectedItem().getName());
            editScreen.setUrlTextArea(listvieww2.getSelectionModel().getSelectedItem().getUrlCommaseperated());

            //pass to controller inorder to keep track of what old entry to delete when the new edited one is added
            editScreen.setCategory(listvieww.getSelectionModel().getSelectedItem().getCatagoryname());
            editScreen.setName(listvieww2.getSelectionModel().getSelectedItem().getName());

            //catagoryHandler.deleteEntry(listvieww.getSelectionModel().getSelectedItem().getCatagoryname(), listvieww2.getSelectionModel().getSelectedItem().getName());


            stage.setScene(new Scene(root, 800, 500));
            stage.setTitle("Edit");
            stage.show();
        }catch (IOException ioException){
            ioException.printStackTrace();
        }


    }
}



