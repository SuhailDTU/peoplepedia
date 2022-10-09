package controller;

import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import javax.crypto.Cipher;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javafx.util.Duration;
import model.Conversion;
import model.EaseOfUse;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class LoginController {


    @FXML
    public Line line1;

    @FXML
    public Line line2;

    @FXML
    public Circle confirmationCircle;


    RotateTransition rt1;
    RotateTransition rt2;


    @FXML
    public TextField textField;
    @FXML
    public Label passwordState;
    @FXML
    public Button enterButton;

    public Conversion converter;

    public ChromeOptions chromeOptions;
    public Stack<ChromeDriver> chromeDriverStack;

    public LoginController(){
    }

    public void initialize() {
        //Initialize rotate animation object
        rt1 = new RotateTransition(Duration.millis(500), line1);
        rt2 = new RotateTransition(Duration.millis(500), line2);

        //Set variables used in rotating animation
        rt1.setByAngle(360); // set rotation done in one animation
        rt1.setCycleCount(3);// set amount of times animation is repeated
        rt2.setByAngle(360);
        rt2.setCycleCount(3);

        //add eventhandler to button, when button is pressed we will check whether the entered password is correct
        enterButton.addEventHandler(ActionEvent.ACTION, new finishHandler());


        //set circle color
        confirmationCircle.setFill(Color.LIGHTGREEN);
        //hide progress circle
        confirmationCircle.setVisible(false);
        line1.setVisible(false);
        line2.setVisible(false);




        //printing our path for debugging
        //System.out.println(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        File jarDirPath = jarFile.getParentFile();

        System.out.println("filepath");
        System.out.println(jarFile.getPath());
        System.out.println("dir path");
        System.out.println(jarDirPath.getPath());
        //remember to make temporary files outside of jar as they are readonly

        //create themesettingsv2Chosen file if it does not exist
        if(!Files.exists(Paths.get(getDirPath()+"/themeSettingsv2Chosen.txt"))){
            //writing to a file that does not exist creates it
            EaseOfUse.writeToFile("#c06c84\n#f67280", getDirPath()+"/themeSettingsv2Chosen.txt");
        }


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
        this.chromeDriverStack = chromeDriverStack;
    }
    //unzips files to current directory
    public void unzipFile(String zipFilePath) {
        try {
            //create input stream on zipfile
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(getDirPath() + "/chromedriver.zip"));
            ZipEntry zipEntry;
            //get next entry
            zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) { //
                //create file object of zipentry in DIR
                File newFile = new File(getDirPath() + "/" + zipEntry.getName());
                if (zipEntry.isDirectory()) {//if zip entry is DIR make file DIR
                    newFile.mkdir();
                } else { //else make normal file
                    //create parent file for zipentry since there is no zipentry for root files
                    File parentFile = newFile.getParentFile();
                    if (!parentFile.exists()) {
                        parentFile.mkdir();
                    }

                    byte[] data = zipInputStream.readAllBytes();
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(newFile));

                    bufferedOutputStream.write(data);

                    //get enxt entry
                    zipEntry = zipInputStream.getNextEntry();

                }


            }
        }
        catch (IOException ioException ){
            ioException.printStackTrace();
        }

    }

    //generic type eventhandler, can be used for every event, this checks whether the password entered is correct
    //used this way:
    // add eventhandler to node and specify which event to listen after, then eventhandler to handle that event of same type or parent type
    //enterButton.getScene().addEventHandler(KeyEvent.KEY_PRESSED, new finishHandler());
    public class finishHandler implements EventHandler<Event> {

        @Override
        public void handle(Event event) {


            //Decrypt password file using entered password as a key
            //If the key to decrypt the file is incorrect the decrypted file will not be created
            Conversion.encryptOrDecryptFilejar(textField.getText(), "passHid.txt",getDirPath()+"/pass.txt", Cipher.DECRYPT_MODE);


            //The default value of the password read from the decrypted file
            //If the password file decryption fails it will stay null
            String password = null;

            //Read from decrypted file
            //If the decryption fails the file doesnt exist and this part will be passed over as the exception is handled
            //the default value of the read password will then be null
            try(BufferedReader bufferedReader = new BufferedReader(new FileReader(getDirPath()+"/pass.txt")) ) {
                //read from file
                password = bufferedReader.readLine().trim();
                //System.out.println(password);//print out password for testing



            }catch (IOException ioException){

            }




            //check if contents of password file match entered password, if it was decrypted using the right password it will otherwise it will be null
            if(textField.getText().equals(password)){
                //creating neccesary files if they do not already exist
                //----------------------------------------------------------------------
                //create empty encrypted encoded file if it does not already exist
                if(!Files.exists(Paths.get(getDirPath()+"/encryptedEncodedFile.txt"))){
                    //create encoded file to encrypt
                    File emptyEncodedFile = new File(getDirPath()+"/encodedfile.txt");
                    try {
                        emptyEncodedFile.createNewFile();
                        Conversion.encryptOrDecryptFile(password,getDirPath()+"/encodedfile.txt", getDirPath()+"/encryptedEncodedFile.txt", Cipher.ENCRYPT_MODE);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                if(!Files.exists(Paths.get(getDirPath()+"/encryptedEncodedFile2.txt"))){
                    //create encoded file to encrypt
                    File emptyEncodedFile2 = new File(getDirPath()+"/encodedfile2.txt");
                    try {
                        emptyEncodedFile2.createNewFile();
                        Conversion.encryptOrDecryptFile(password,getDirPath()+"/encodedfile2.txt", getDirPath()+"/encryptedEncodedFile2.txt", Cipher.ENCRYPT_MODE);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                //download chromedriver zip and extract if it does not exist
                if(!Files.exists(Paths.get(getDirPath()+"/chromedriver.exe"))){
                    try {
                        //download zip
                        URL urlToDownloadFile = new URL("https://chromedriver.storage.googleapis.com/93.0.4577.63/chromedriver_win32.zip");
                        BufferedInputStream bufferedInputStream = new BufferedInputStream(urlToDownloadFile.openStream());
                        byte[] data = bufferedInputStream.readAllBytes();
                        BufferedOutputStream bufferedOutputStream= new BufferedOutputStream(new FileOutputStream ( getDirPath() + "/chromedriver.zip"));
                        bufferedOutputStream.write(data);
                        bufferedOutputStream.close();
                        bufferedInputStream.close();
                        //unzip it
                        EaseOfUse easeOfUse = new EaseOfUse();
                        easeOfUse.unzipFile(getDirPath() + "/chromedriver.zip");
                        //delete uneeded zip later
                        File zipFile = new File(getDirPath() + "/chromedriver.zip");
                        zipFile.deleteOnExit();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                //----------------------------------------------------------------------




                //reveal progress circle
                confirmationCircle.setVisible(true);
                line1.setVisible(true);
                line2.setVisible(true);
                //start playing circle animation to show progress
                rt1.play();
                rt2.play();

                //remove file after program closes as we need it while program is running
                Conversion.deleteFileOnExit(getDirPath()+"/pass.txt");

                //if the contents was decrypted correctly use it to decrypt the contents
                Conversion.encryptOrDecryptFile(password, getDirPath()+"/encryptedEncodedFile.txt",getDirPath()+"/encodedfile.txt", Cipher.DECRYPT_MODE);
                //remove unencrypted files after program closes
                Conversion.deleteFileOnExit(getDirPath()+"/encodedfile.txt");


                passwordState.setText("the password is correct");
                passwordState.setTextFill(Color.GREEN);
                Stage stage = (Stage) textField.getScene().getWindow();

                //Waits a bit before proceeding to next screen
                PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1));
                pauseTransition.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        try {
                            //get parent node from fxml
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/controller/view/sample.fxml"));
                            Parent root = fxmlLoader.load();
                            Scene mainScene = new Scene(root, 800, 500);

                            //pass chromedriver and options to main screen
                            Controller controller = fxmlLoader.getController();
                            controller.setChromeOptions(chromeOptions);
                            controller.setChromeDriverStack(chromeDriverStack);
                            //pass reference scene and controller to controller, this is used when returning to the scene;
                            controller.setMainScreenController(controller);
                            controller.setMainScene(mainScene);
                            controller.setupKeyLogOnScene();

                            //create instance of loading screen
                            controller.createInstanceOfLoadingScreen();

                            //set saved selection of color as theme
                            BufferedReader bufferedReader = new BufferedReader(new FileReader(getDirPath() + "/themeSettingsv2Chosen.txt"));
                            String color1 = bufferedReader.readLine();
                            String color2 = bufferedReader.readLine();
                            bufferedReader.close();
                            controller.setTheme(color1, color2);


                            //finish setting up by placing node in scene and stage
                            stage.setScene(mainScene);
                            stage.setTitle("Peoplepedia");
                            stage.show();
                        }catch (IOException ioException){
                            ioException.printStackTrace();
                        }
                    }
                });
                pauseTransition.play();



            }else {
                passwordState.setText("the password "+textField.getText()+" is wrong");
                passwordState.setTextFill(Color.RED);


            }

        }
    }

}
