package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Categories;
import model.CategoryHandler;
import model.Conversion;
import model.Person;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Controller {
    //filename constants

    public final String encodedFileName = "src/encodedfile.txt";
    public final String encryptedEncodedFileName = "src/encryptedEncodedFile.txt";
    public final String listingFileName = "src/testpeoplepedia.txt";

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

    Stack<ChromeDriver> ChromeDriverStack;//for implementing multiple windows

    WebDriver chromeDriver;//for implementing singular windows
    ChromeOptions chromeOptions;

    //references to this roots scene and controller
    //this is passed to other screens so they can return here
    Scene mainScene;
    Controller mainScreenController;

    //references to addscreen's scene and controller, this is stored inorder to reuse objects.
    Scene editScene;
    EditScreen editScreenController;

    //references to editscreeens scene and controller, this is stored inorder to reuse objects.
    Scene addScene;
    AddEntryScreen addScreenController;

    public Controller(){
        converter.ConvertFromHexencodedFileToCommafile(encodedFileName, listingFileName);//convert from hex to comma seperated file "src/testpeoplepedia.txt"
        categoriesList = catagoryHandler.readincatagories(listingFileName); //use converted file
        File listingfile = new File(listingFileName);// delete file after use
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

    public void setChromeOptions(ChromeOptions chromeOptions) {
        this.chromeOptions = chromeOptions;
    }

    public void setChromeDriverStack(Stack<ChromeDriver> chromeDriverStack) {
        ChromeDriverStack = chromeDriverStack;
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
                    //createVboxData(newPerson);
                    createHyperlinkbox(newPerson);
                }
            }
        });

        listvieww.getSelectionModel().select(0);//set default to first item category listview


    }

    public void refreshListView(){
        //create new observable lists and give references to catagory list
        converter.ConvertFromHexencodedFileToCommafile(encodedFileName, listingFileName);//convert from hex to comma seperated file "src/testpeoplepedia.txt"
        categoriesList = catagoryHandler.readincatagories(listingFileName); //use converted file
        categoriesObservableList = FXCollections.observableArrayList(categoriesList);
        File listingfile = new File(listingFileName);// delete file after use
        listingfile.delete();

        //empty both listsviews
        listvieww.getItems().clear();
        listvieww2.getItems().clear();

        //set items from observable arrays in catagory listview
        listvieww.setItems(categoriesObservableList);
        //select first item
        listvieww.getSelectionModel().selectFirst();
    }

    public void createVboxData(Person person){

        ArrayList<String> urlList = person.getUrls();

        nameLabel.setText(person.getName());

        urlbox.getChildren().clear();
        for (int i = 0; i < urlList.size(); i++){
            urlbox.getChildren().add(new Label(urlList.get(i)));

        }
    }

    public void createHyperlinkbox(Person person){
        ArrayList<String> urlList = person.getUrls();

        nameLabel.setText(person.getName());

        urlbox.getChildren().clear();
        for (int i = 0; i < urlList.size(); i++){
            Hyperlink hyperlink = new Hyperlink(urlList.get(i));
            //we set a handler on each link to clicking it opens the link
            hyperlink.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {


                    //multiple window model
                    //every event handling creates a new chromedriver, uses it to show the url and then adds it to the stack
                    ChromeDriver newChromedriver = new ChromeDriver(chromeOptions);
                    newChromedriver.get(hyperlink.getText());
                    ChromeDriverStack.add(newChromedriver);
                }
            });
            urlbox.getChildren().add(hyperlink);
        }
    }


    public void acessNewEntryScreen(ActionEvent actionEvent) {
        Stage stage = (Stage) nameLabel.getScene().getWindow();

        if (addScene == null || addScreenController == null) {//if there is no instance create one and save references
            System.out.println("created new addscene instance");
            try {
                //load add screen root node and controller
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../controller/view/AddEntryScreen.fxml"));
                Parent root = fxmlLoader.load();
                addScreenController = fxmlLoader.getController();

                //pass reference of main screen to AddEntryScreen
                addScreenController.setMainScreenController(mainScreenController);
                addScreenController.setMainScene(mainScene);

                //create scene using root node
                addScene = new Scene(root, 800, 500); //save scene for reusesal
                //set scene
                stage.setScene(addScene);
                stage.setTitle("Entry Adder");
                stage.show();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }else { //use instance if it exists

            System.out.println("reused addscene instance");

            //clear text fields for blank entry
            addScreenController.urlTextArea.clear();
            addScreenController.Categorytextfield.clear();
            addScreenController.nameTextField.clear();
            //set title and pass scene to stage
            stage.setScene(addScene);
            stage.setTitle("Entry Adder");
            stage.show();
        }
    }

    public void edit(ActionEvent actionEvent) {
        Stage stage = (Stage) nameLabel.getScene().getWindow();

        if(editScene == null || editScreenController == null) {
            try { //create instance of editor incase there is no reference
                System.out.println("created new editor instance");
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../controller/view/EditScreen.fxml"));
                Parent root = fxmlLoader.load();
                editScreenController = fxmlLoader.getController();


                //set as default values in textfield when editing entry
                editScreenController.setCategorytextfield(listvieww.getSelectionModel().getSelectedItem().getCatagoryname());
                editScreenController.setNameTextField(listvieww2.getSelectionModel().getSelectedItem().getName());
                editScreenController.setUrlTextArea(listvieww2.getSelectionModel().getSelectedItem().getUrlCommaseperated());

                //pass to controller inorder to keep track of what old entry to delete when the new edited one is added
                editScreenController.setCategory(listvieww.getSelectionModel().getSelectedItem().getCatagoryname());
                editScreenController.setName(listvieww2.getSelectionModel().getSelectedItem().getName());
                editScreenController.setCommaSeperatedUrls(listvieww2.getSelectionModel().getSelectedItem().getUrlCommaseperated());

                //pass main screeen references to edit screen for returning
                editScreenController.setMainScreenController(mainScreenController);
                editScreenController.setMainScene(mainScene);

                //create new scene and save reference
                editScene = new Scene(root, 800, 500);

                stage.setScene(editScene);
                stage.setTitle("Editor");
                stage.show();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        }else { //reuse instance of editor
            System.out.println("reused editor instance");
            //set as default values in textfield when editing entry
            editScreenController.setCategorytextfield(listvieww.getSelectionModel().getSelectedItem().getCatagoryname());
            editScreenController.setNameTextField(listvieww2.getSelectionModel().getSelectedItem().getName());
            editScreenController.setUrlTextArea(listvieww2.getSelectionModel().getSelectedItem().getUrlCommaseperated());

            //pass to controller inorder to keep track of what old entry to delete when the new edited one is added
            editScreenController.setCategory(listvieww.getSelectionModel().getSelectedItem().getCatagoryname());
            editScreenController.setName(listvieww2.getSelectionModel().getSelectedItem().getName());
            editScreenController.setCommaSeperatedUrls(listvieww2.getSelectionModel().getSelectedItem().getUrlCommaseperated());

            //use reference of screen to reuse instnce of editor
            stage.setScene(editScene);
            stage.setTitle("Editor");
            stage.show();
        }
    }

    public void setMainScreenController(Controller mainScreenController) {
        this.mainScreenController = mainScreenController;
    }

    public void setMainScene(Scene mainScene) {
        this.mainScene = mainScene;
    }
}



