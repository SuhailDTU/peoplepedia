package controller;

import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Paint;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import model.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Stack;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.crypto.Cipher;

public class Controller {
    //filename constants

    public void setEncodedFileName(String encodedFileName) {
        this.encodedFileName = encodedFileName;
    }

    public void setEncryptedEncodedFileName(String encryptedEncodedFileName) {
        this.encryptedEncodedFileName = encryptedEncodedFileName;
    }

    public String encodedFileName = getDirPath()+"/encodedfile.txt";
    public String encryptedEncodedFileName = "src/encryptedEncodedFile.txt";
    public String listingFileName = getDirPath()+"/testpeoplepedia.txt";

    @FXML
    public ListView<Categories> listvieww;
    public ListView<Person> listvieww2;
    public Label nameLabel;
    public VBox urlbox;
    public Button AddEntryButton;
    public GridPane showDataPane;
    public BorderPane borderPaneOuter;
    public RadioButton chromeRadioButton;
    public RadioButton windowWebviewRadioButton;
    public Label select1;// labels for latest selected people
    public Label select2;
    public Label select3;
    public Label peopleLabel;
    public Label topBannerLabel;
    public Label botBanner;
    public Label catagoryLabel;

    //current screen
    int screenNum = 1;

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
    //references to editscreeens scene and controller, this is stored inorder to reuse objects.
    Scene themePickerScene;
    ThemePicker themePickerController;

    //for showing websites
    WebView webView = new WebView();
    WebEngine webEngine = webView.getEngine();

    MediaView mediaView = new MediaView();

    MusicPlayer musicPlayer = new MusicPlayer();
    @FXML
    Button musicButton;
    @FXML
    Slider volumeSlider;


    //keeps track of recently selected people
    ArrayList<String[]> lastSelectedPeople = new ArrayList<>();
    Label[] selectArray;
    //colorChoices
    Paint paintChoice1;
    Paint paintChoice2;
    String hexColor1;
    String hexColor2;
    //keeps track of recently pressed keys
    ArrayList<String> keylog = new ArrayList<String>();
    //current reload link, this is where the current link given to webengine is stored incase the webengine fails
    String reloadLink;
    //references to the loading screen
    Scene loadingScene;
    LoadingScreenController loadingScreenController;

    public Controller(){
        webEngine.load("https://www.google.com");

        converter.ConvertFromHexencodedFileToCommafile(encodedFileName, listingFileName);//convert from hex to comma seperated file "src/testpeoplepedia.txt"
        categoriesList = catagoryHandler.readincatagories(listingFileName); //use converted file
        File listingfile = new File(listingFileName);// delete file after use
        listingfile.delete();




        categoriesObservableList = FXCollections.observableArrayList(categoriesList); //read in from commasepererated file and convert to Observable arraylist of catagories

        // testing if read in correct
/*
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
*/
    }
    public String getDirPath(){
        File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        File jarDirPath = jarFile.getParentFile();
        return jarDirPath.getPath();
    }
    public void setChromeOptions(ChromeOptions chromeOptions) {
        this.chromeOptions = chromeOptions;
    }

    public void setChromeDriverStack(Stack<ChromeDriver> chromeDriverStack) {
        ChromeDriverStack = chromeDriverStack;
    }

    //Sets the theme of the mainscreen and the loginScreen
    //uses color object but set as the static type of its parent Paint
    public void setTheme(Paint paint1, Paint paint2) {
        paintChoice1 = paint1;
        paintChoice2 = paint2;

        String hex1 = EaseOfUse.getHexFromPaint(paintChoice1);
        String hex2 = EaseOfUse.getHexFromPaint(paintChoice2);

        //set color1
        nameLabel.setStyle(" -fx-background-color: "+hex1);
        peopleLabel.setStyle(" -fx-background-color: "+hex1);
        catagoryLabel.setStyle(" -fx-background-color: "+hex1);
        select1.setStyle(" -fx-background-color: "+hex1);
        select2.setStyle(" -fx-background-color: "+hex1);
        select3.setStyle(" -fx-background-color: "+hex1);
        loadingScreenController.changeLabelColor(hex1, hex2);
        //set color2
        topBannerLabel.setStyle(" -fx-background-color: "+hex2);
        botBanner.setStyle(" -fx-background-color: "+hex2);

    }
    //Sets the theme of the mainscreen and the loginScreen
    //uses hexcolor
    public void setTheme(String hexColor1, String hexColor2) {

        this.hexColor1 = hexColor1;
        this.hexColor2 = hexColor2;

        //set color1
        nameLabel.setStyle(" -fx-background-color: "+hexColor1);
        peopleLabel.setStyle(" -fx-background-color: "+hexColor1);
        catagoryLabel.setStyle(" -fx-background-color: "+hexColor1);
        select1.setStyle(" -fx-background-color: "+hexColor1);
        select2.setStyle(" -fx-background-color: "+hexColor1);
        select3.setStyle(" -fx-background-color: "+hexColor1);
        loadingScreenController.changeLabelColor(hexColor1, hexColor2);
        //set color2
        topBannerLabel.setStyle(" -fx-background-color: "+hexColor2);
        botBanner.setStyle(" -fx-background-color: "+hexColor2);

    }
    @FXML
    public void initialize(){

        //musicPlayer.startTrack();



        // put observalble list in listview
        listvieww.setItems(categoriesObservableList);

        //we can only select one item at a time
        listvieww.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listvieww2.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        //sets up the cellfactory on the listviews
        setupCellFactoryOnListviews();

        //put references of the "last selected" labels in array for easy selection
        selectArray = new Label[] {select1, select2, select3};

        //set listeners on both listviews
        updatePersonListcellAndData();

    }

    //sets the cellfactory of listview to display the catagory names of its objects in the cells
    //set the cellfactory of listview to display the person names of its objects in the cells
    public void setupCellFactoryOnListviews(){
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

        volumeSlider.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                changeVolume();
            }
        });
    }


    public void playCurrentTrack(ActionEvent actionEvent){
        if(musicPlayer.playState == PlayState.playing){
            musicPlayer.pauseTrack();
        }else {
            musicPlayer.playChosenTrack();
        }
    }

    public void changeVolume(){
        System.out.println(volumeSlider.getValue());
        musicPlayer.setVolume((float) volumeSlider.getValue());
    }
    //sets listeners on both listviews
    //listener on listview changes contents of listview2 depending on selected item
    //listener on listview2 changes contents of hyperlinkbox depending on selected item
    //also sets listeners/eventhandlers on other relevant buttons such as the buttons that change the browsertype and last selected labels
    //along with listener on webview that reloads search if it fails
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
                    borderPaneOuter.setCenter(showDataPane);//set urlbox and header as center if it has been removed
                    createHyperLinkBox(newPerson);
                    webEngine.load(""); //stop video


                    //This part display the previously selected people
                    lastSelectedPeople.add(new String[] {Integer.toString(listvieww.getSelectionModel().getSelectedIndex()),Integer.toString(listvieww2.getSelectionModel().getSelectedIndex()), newPerson.getName()});//add to end

                    if( 3 < lastSelectedPeople.size() ){// if it gets longer than 3 remove from start
                        lastSelectedPeople.remove(0);
                    }

                    for(int i = 0; i < lastSelectedPeople.size(); i++){ //update list with names
                        selectArray[i].setText(lastSelectedPeople.get(i)[2]);
                    }


                }
            }
        });

        listvieww.getSelectionModel().select(0);//set default to first item category listview

        //Put eventhandler on radiobuttons that places urlbox back when settings altered
        //the buttons change between using embedded webview browser, seperate webview browser or using a seperate chrome browser
        chromeRadioButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                borderPaneOuter.setCenter(showDataPane);//set urlbox and header as center if it has been removed
                webEngine.load(""); //close current video

            }
        });
        windowWebviewRadioButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                borderPaneOuter.setCenter(showDataPane);//set urlbox and header as center if it has been removed
                webEngine.load("");//close current video
            }
        });


        //place eventhandlers on last selected people so it will go to that item when clicked
        select1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(lastSelectedPeople.size() >= 1) {
                    listvieww.getSelectionModel().select(Integer.parseInt(lastSelectedPeople.get(0)[0]));
                    //delay settingn listview 2 a bit due to listview 1's listener automatically setting listview 2 select to 0
                    PauseTransition pauseTransition = new PauseTransition(Duration.millis(100));
                    pauseTransition.setOnFinished(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            listvieww2.getSelectionModel().select(Integer.parseInt(lastSelectedPeople.get(0)[1]));
                        }
                    });
                    pauseTransition.play();
                }
            }
        });
        select2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(lastSelectedPeople.size() >= 2) {
                    listvieww.getSelectionModel().select(Integer.parseInt(lastSelectedPeople.get(1)[0]));
                    //delay settingn listview 2 a bit due to listview 1's listener automatically setting listview 2 select to 0
                    PauseTransition pauseTransition = new PauseTransition(Duration.millis(100));
                    pauseTransition.setOnFinished(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            listvieww2.getSelectionModel().select(Integer.parseInt(lastSelectedPeople.get(1)[1]));
                        }
                    });
                    pauseTransition.play();
                }
            }
        });
        select3.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(lastSelectedPeople.size() >= 3) {
                    listvieww.getSelectionModel().select(Integer.parseInt(lastSelectedPeople.get(2)[0]));
                    //delay settingn listview 2 a bit due to listview 1's listener automatically setting listview 2 select to 0
                    PauseTransition pauseTransition = new PauseTransition(Duration.millis(100));
                    pauseTransition.setOnFinished(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            listvieww2.getSelectionModel().select(Integer.parseInt(lastSelectedPeople.get(2)[1]));
                        }
                    });
                    pauseTransition.play();
                }
            }
        });

        webEngine.setOnStatusChanged(new EventHandler<WebEvent<String>>() {
            @Override
            public void handle(WebEvent<String> stringWebEvent) {
                //if something cancels the loading of the video do it again
                if(webEngine.getLoadWorker().getState() == Worker.State.CANCELLED){
                    System.out.println("webengine status: "+stringWebEvent.toString() + "\n" + webEngine.getLoadWorker().getState().toString() );
                    webEngine.load(reloadLink);
                }else if(webEngine.getLoadWorker().getState() == Worker.State.SUCCEEDED){
                    System.out.println("webengine status: "+stringWebEvent.toString() + "\n" + webEngine.getLoadWorker().getState().toString() );
                }
            }
        });
        listvieww2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                borderPaneOuter.setCenter(showDataPane);
            }
        });
    }

    //reloads contents of the listviews from the encoded file we are reading from.
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
        //put urlbox back in place
        urlbox.getChildren().clear();
        borderPaneOuter.setCenter(showDataPane);

        //set items from observable arrays in catagory listview
        listvieww.setItems(categoriesObservableList);
        //select first item
        listvieww.getSelectionModel().selectFirst();
    }
    //reloads contents of the listviews from the encoded file we are reading from.
    public void refreshListView(String encodedFileName){
        //create new observable lists and give references to catagory list
        converter.ConvertFromHexencodedFileToCommafile(encodedFileName, listingFileName);//convert from hex to comma seperated file "src/testpeoplepedia.txt"
        categoriesList = catagoryHandler.readincatagories(listingFileName); //use converted file
        categoriesObservableList = FXCollections.observableArrayList(categoriesList);
        File listingfile = new File(listingFileName);// delete file after use
        listingfile.delete();

        //empty both listsviews
        listvieww.getItems().clear();
        listvieww2.getItems().clear();
        //put urlbox back in place
        urlbox.getChildren().clear();
        borderPaneOuter.setCenter(showDataPane);

        //set items from observable arrays in catagory listview
        listvieww.setItems(categoriesObservableList);
        //select first item
        listvieww.getSelectionModel().selectFirst();
    }

    public void setupKeyLogOnScene(){
        mainScene.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                //add new keypress to list
                keylog.add(keyEvent.getCharacter());
                //only keep last 6 key presses
                if(keylog.size() > 6){
                    keylog.remove(0);
                }
                //for debugging to test if buttons are detected correctly
                //System.out.println("new string: "+ keylog.toString());


                //check whether the last 6 keypresses match the desired phrase
                String inputtedString = "";
                String passCode = "";

                //convert passcode file and read from it
                String key = EaseOfUse.readFromfile(getDirPath()+"/pass.txt");
                Conversion.encryptOrDecryptFilejar(key,"passHid2.txt",getDirPath()+"/pass2.txt", Cipher.DECRYPT_MODE);
                String password = EaseOfUse.readFromfile(getDirPath()+"/pass2.txt");

                //delete file so it doesnt exist in plain text
                try {
                    Files.deleteIfExists(Paths.get(getDirPath()+"/pass2.txt"));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                //check whether input matches
                for(int i = 0; i < keylog.size(); i++){
                    inputtedString = inputtedString + keylog.get(i);
                }
                if(inputtedString.matches(password)){
                    //create new instance of main controller but with another encryped file as input
                    Stage stage = (Stage)listvieww.getScene().getWindow();


                        //original idea was creating a new instance of the main screen with a different encryptedEncodedFile

                        /*
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/controller/view/sample.fxml"));
                        Parent root = fxmlLoader.load();
                        Controller maincontroller2 = fxmlLoader.getController();
                        Scene scene2 = new Scene(root, 800 , 500);

                        //pass chromedriver and options to second main screen
                        maincontroller2.setChromeOptions(chromeOptions);
                        maincontroller2.setChromeDriverStack(ChromeDriverStack);
                        //decrypt second encoded file and make sure it gets deleted on exit
                        Conversion.encryptOrDecryptFile(key, getDirPath()+"/encryptedEncodedFile2.txt",getDirPath()+"/encodedfile2.txt", Cipher.DECRYPT_MODE );
                        Conversion.deleteFileOnExit(getDirPath()+"/encodedfile2.txt");

                        //pass reference scene and controller to controller, this is used when returning to the scene;
                        maincontroller2.setMainScreenController(maincontroller2);
                        maincontroller2.setMainScene(scene2);
                        //pass other encrypted file name to be used instead and refresh listview
                        maincontroller2.setEncodedFileName(getDirPath()+"/encodedfile2.txt");
                        maincontroller2.setEncryptedEncodedFileName(getDirPath()+"/encryptedEncodedFile2.txt");
                        maincontroller2.refreshListView();

                        //set saved selection of color as theme
                        BufferedReader bufferedReader = new BufferedReader(new FileReader(getDirPath() + "/themeSettingsv2Chosen.txt"));
                        String color1 = bufferedReader.readLine();
                        String color2 = bufferedReader.readLine();
                        bufferedReader.close();
                        maincontroller2.setTheme(color1, color2);

                        //finish setting up by placing node in scene and stage
                        stage.setScene(scene2);
                        stage.setTitle("Peoplepedia mirage");
                        stage.show();
                        */

                        //simpler solution that doesnt require a separate screen and only reloads contents

                        //reset las selected people now that we are working with a new dataset
                        resetLastSelectedLabels();

                        //place loading screen in scene
                        stage.setScene(loadingScene);
                        stage.show();
                        loadingScreenController.playAnimation();

                        //wait a second before changing to content so we can see the loading screen
                        //then change to new content
                        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1.5));
                        pauseTransition.setOnFinished(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                loadingScreenController.resetLoadingBar();
                                stage.setScene(mainScene);
                                stage.show();
                                if(screenNum == 1){
                                    System.out.println("The veil is gone");
                                    //decrypt inputfile
                                    File secondFile = new File(getDirPath()+"/encodedfile2.txt");
                                    if(!secondFile.exists()){
                                        Conversion.encryptOrDecryptFile(key, getDirPath()+"/encryptedEncodedFile2.txt",getDirPath()+"/encodedfile2.txt", Cipher.DECRYPT_MODE );
                                        //Conversion.deleteFileOnExit(getDirPath()+"/encodedfile2.txt");
                                        secondFile.deleteOnExit();
                                    }
                                    //set new input file and reload
                                    setEncodedFileName(getDirPath()+"/encodedfile2.txt");
                                    setEncryptedEncodedFileName(getDirPath()+"/encryptedEncodedFile2.txt");
                                    refreshListView();
                                    screenNum = 2;
                                    stage.setTitle("Peoplepedia mirage");
                                    //Conversion.encryptOrDecryptFile(key, getDirPath()+"/encodedfile.txt",getDirPath()+"/encryptedEncodedFile.txt", Cipher.ENCRYPT_MODE);
                                }else{
                                    System.out.println("The veil has been reinstated");
                                    setEncodedFileName(getDirPath()+"/encodedfile.txt");
                                    setEncryptedEncodedFileName(getDirPath()+"/encryptedEncodedFile.txt");
                                    refreshListView();
                                    screenNum = 1;
                                    stage.setTitle("Peoplepedia");
                                    //Conversion.encryptOrDecryptFile(key, getDirPath()+"/encodedfile2.txt",getDirPath()+"/encryptedEncodedFile2.txt", Cipher.ENCRYPT_MODE);
                                }
                            }
                        });
                        pauseTransition.play();





                }

            }
        });


    }

    //this will play in the window intead of in a browser window
    public void createHyperLinkBox(Person person){
        ArrayList<String> urlList = person.getUrls();

        nameLabel.setText(person.getName());

        urlbox.getChildren().clear();
        for (int i = 0; i < urlList.size(); i++){
            Hyperlink hyperlink = new Hyperlink(urlList.get(i));
            //we set a handler on each link to clicking it opens the link


            hyperlink.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {

                    //notice the priority if chromedriver selected the button for the other setting does not matter
                    if (chromeRadioButton.selectedProperty().get() == true){ //chromedriver
                        //multiple window model
                        //every event handling creates a new chromedriver, uses it to show the url and then adds it to the stack
                        ChromeDriver newChromedriver = new ChromeDriver(chromeOptions);
                        newChromedriver.get(correctLinkText(hyperlink));
                        ChromeDriverStack.add(newChromedriver);
                    }
                    else if (windowWebviewRadioButton.selectedProperty().get() == true) { //webview in seperate window
                        try {
                            Stage newStage = new Stage();
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/controller/view/WebScreen.fxml"));
                            Parent root = fxmlLoader.load();
                            WebScreenController webScreenController = fxmlLoader.getController();
                            webScreenController.setNameLabel(listvieww2.getSelectionModel().getSelectedItem().getName());
                            webScreenController.startVideo(correctLinkText(hyperlink));

                            //attach handler to stage that closes content after we close window. this will stop the video
                            newStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                                @Override
                                public void handle(WindowEvent windowEvent) {
                                    webScreenController.startVideo("");
                                }
                            });

                            //place root node in scene then in stage and show
                            newStage.setScene(new Scene(root,800, 500));
                            newStage.show();

                        }catch (IOException ioException){

                        }
                    }
                    else if (windowWebviewRadioButton.selectedProperty().get() == false && chromeRadioButton.selectedProperty().get() == false) { //webview embedded
                       //If you want to play online mp3/mp4 files using a mediaplayer and mediaview instead of a webengine and webview
                        // webview and its engine webengine can get and display videofiles just as well as any other online resource
                        //so this is usually not needed

                        /*if(hyperlink.getText().contains("mp3") || hyperlink.getText().contains("mp4")){ //if link contains mp3 play file instead of showing site
                            //create media form link
                            Media media = new Media(hyperlink.getText());
                            //pas media to player and viewer
                            MediaPlayer mediaPlayer = new MediaPlayer(media);
                            mediaView.setMediaPlayer(mediaPlayer);
                            mediaPlayer.setAutoPlay(true);

                            //put in anchorpane
                            AnchorPane anchorPane = new AnchorPane();
                            Pane pane = new Pane();
                            pane.getChildren().add(mediaView);
                            anchorPane.getChildren().add(pane);

                            //set anchors for mediaview
                            AnchorPane.setTopAnchor(pane, 0.0);
                            AnchorPane.setRightAnchor(pane, 0.0);
                            AnchorPane.setLeftAnchor(pane, 0.0);
                            AnchorPane.setBottomAnchor(pane, 0.0);

                            //set to borderpane centers current size
                            borderPaneOuter.setCenter(anchorPane);
                            mediaView.fitWidthProperty().bind(pane.widthProperty());
                            mediaView.fitHeightProperty().bind(pane.heightProperty());


                            //set it so the mediaplayer can stop/start
                            mainScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                                @Override
                                public void handle(KeyEvent keyEvent) {
                                    System.out.println("handling key event");
                                    if(keyEvent.getCode() == KeyCode.SPACE || keyEvent.getCode() == KeyCode.S ){
                                        System.out.println("stopping");
                                        mediaView.getMediaPlayer().pause();
                                    }else if (keyEvent.getCode() == KeyCode.P) {
                                        System.out.println("starting");
                                        mediaView.getMediaPlayer().play();
                                    }
                                }
                            });

                        }else {
                            webEngine.load(hyperlink.getText());
                            //set webview in center of app
                            borderPaneOuter.setCenter(webView);
                        }*/

                        reloadLink = correctLinkText(hyperlink);//incase we have to realod save it
                        webEngine.load(reloadLink);

                        //set webview in center of app
                        borderPaneOuter.setCenter(webView);



                    }

                }
            });
            urlbox.getChildren().add(hyperlink);
        }
    }


    public void acessNewEntryScreen(ActionEvent actionEvent) {
        Stage stage = (Stage) listvieww.getScene().getWindow();

        if (addScene == null || addScreenController == null) {//if there is no instance create one and save references
            System.out.println("created new addscene instance");
            try {
                //load add screen root node and controller
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/controller/view/AddEntryScreen.fxml"));
                Parent root = fxmlLoader.load();
                addScreenController = fxmlLoader.getController();

                //pass reference of main screen to AddEntryScreen
                addScreenController.setMainScreenController(mainScreenController);
                addScreenController.setMainScene(mainScene);

                //give filename references
                addScreenController.setEncodedFileName(encodedFileName);
                addScreenController.setListingFileName(listingFileName);

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
            addScreenController.Categorytextfield.clear();
            addScreenController.nameTextField.clear();
            addScreenController.clearListView();
            addScreenController.UrlText.clear();
            //set title and pass scene to stage
            stage.setScene(addScene);
            stage.setTitle("Entry Adder");
            stage.show();
        }
    }

    private String getCategoryIdentifierForItem(Person person, ArrayList<Categories> categories){
       ArrayList<Categories> personCate = new ArrayList<>();
        for (Categories category : categories) {
            if(category.getPeople().contains(person)){
                personCate.add(category);
            }
        }

        String returnVal = personCate.get(0).getCatagoryname();
        for(int i = 1; i < personCate.size(); i++){
            if(personCate.get(i).getPeople().contains(person)) {
                returnVal = returnVal + "#" + personCate.get(i).getCatagoryname();
            }
        }

        return  returnVal;
    }

    public void edit(ActionEvent actionEvent) {
        Stage stage = (Stage) listvieww.getScene().getWindow();

        if(editScene == null || editScreenController == null) {
            try { //create instance of editor incase there is no reference
                System.out.println("created new editor instance");
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/controller/view/EditScreen.fxml"));
                Parent root = fxmlLoader.load();
                editScreenController = fxmlLoader.getController();


                //set as default values in textfield when editing entry
                editScreenController.setCategorytextfield(getCategoryIdentifierForItem(listvieww2.getSelectionModel().getSelectedItem(),categoriesList));
                editScreenController.setNameTextField(listvieww2.getSelectionModel().getSelectedItem().getName());
                editScreenController.setUrlList(listvieww2.getSelectionModel().getSelectedItem().getUrls());
                editScreenController.setupListViewData();

                //pass to controller inorder to keep track of what old entry to delete when the new edited one is added
                editScreenController.setCategory(getCategoryIdentifierForItem(listvieww2.getSelectionModel().getSelectedItem(),categoriesList));
                editScreenController.setName(listvieww2.getSelectionModel().getSelectedItem().getName());
                editScreenController.setCommaSeperatedUrls(listvieww2.getSelectionModel().getSelectedItem().getUrlCommaseperated());

                //pass main screeen references to edit screen for returning
                editScreenController.setMainScreenController(mainScreenController);
                editScreenController.setMainScene(mainScene);

                //pass file name references
                editScreenController.setEncodedFileName(encodedFileName);
                editScreenController.setListingFileName(listingFileName);

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
            editScreenController.setCategorytextfield(getCategoryIdentifierForItem(listvieww2.getSelectionModel().getSelectedItem(),categoriesList));
            editScreenController.setNameTextField(listvieww2.getSelectionModel().getSelectedItem().getName());
            editScreenController.setUrlList(listvieww2.getSelectionModel().getSelectedItem().getUrls());
            editScreenController.setupListViewData();

            //pass to controller inorder to keep track of what old entry to delete when the new edited one is added
            editScreenController.setCategory(getCategoryIdentifierForItem(listvieww2.getSelectionModel().getSelectedItem(),categoriesList));
            editScreenController.setName(listvieww2.getSelectionModel().getSelectedItem().getName());
            editScreenController.setCommaSeperatedUrls(listvieww2.getSelectionModel().getSelectedItem().getUrlCommaseperated());
            editScreenController.urlTextField.clear();

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

    public void accessThemeMenu(ActionEvent actionEvent) {
        if (themePickerController == null || themePickerScene == null) {
            try {
                Stage stage = (Stage) listvieww.getScene().getWindow();
                System.out.println("creating new themePicker instance ");
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/controller/view/themePicker.fxml"));
                Parent root = fxmlLoader.load();
                //create scene
                themePickerScene = new Scene(root, 800, 500);

                //get controller
                themePickerController = fxmlLoader.getController();

                //set references for returning
                themePickerController.setMainController(mainScreenController);
                themePickerController.setMainScene(mainScene);

                //set scene on stage
                stage.setTitle("Theme Settings");
                stage.setScene(themePickerScene);
                stage.show();




            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }else{
            Stage stage = (Stage) listvieww.getScene().getWindow();
            System.out.println("reusing themePicker instance");
            stage.setTitle("Theme Settings");

            stage.setScene(themePickerScene);
            stage.show();
        }

    }
    public String correctLinkText(Hyperlink link){
        //check what the link starts with and alter accordingly to use in websearch
        String hyperlinkText = link.getText();
        if(!hyperlinkText.startsWith("https://") && !hyperlinkText.startsWith("www.")){
            hyperlinkText = "https://www." + hyperlinkText;
        }else if (hyperlinkText.startsWith("www.")){
            hyperlinkText = "https://" + hyperlinkText;
        }
        return hyperlinkText;
    }

    public void createInstanceOfLoadingScreen(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/controller/view/loadingScreen.fxml"));
            Parent root = fxmlLoader.load();
            loadingScene = new Scene(root, 800, 500);
            loadingScreenController = fxmlLoader.getController();

        }catch (IOException ioException){
            ioException.printStackTrace();
        }


    }
    //empties the lastselected people list
    //this is for when switching between datasets
    public void resetLastSelectedLabels(){
        lastSelectedPeople.clear();
        for (int i = 0; i < selectArray.length; i++){
            selectArray[i].setText("*");
        }
    }
}



