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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

import javafx.util.Duration;
import model.Conversion;
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

    public void initialize(){
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

    }



    public void setChromeOptions(ChromeOptions chromeOptions) {
        this.chromeOptions = chromeOptions;
    }

    public void setChromeDriverStack(Stack<ChromeDriver> chromeDriverStack) {
        this.chromeDriverStack = chromeDriverStack;
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
            Conversion.encryptOrDecryptFile(textField.getText(), "src/passHid.txt","src/pass.txt", Cipher.DECRYPT_MODE);


            //The default value of the password read from the decrypted file
            //If the password file decryption fails it will stay null
            String password = null;

            //Read from decrypted file
            //If the decryption fails the file doesnt exist and this part will be passed over as the exception is handled
            //the default value of the read password will then be null
            try(BufferedReader bufferedReader = new BufferedReader(new FileReader("src/pass.txt")) ) {
                //read from file
                password = bufferedReader.readLine().trim();
                System.out.println(password);



            }catch (IOException ioException){

            }



            //check if contents of password file match entered password, if it was decrypted using the right password it will otherwise it will be null
            if(textField.getText().equals(password)){

                //reveal progress circle
                confirmationCircle.setVisible(true);
                line1.setVisible(true);
                line2.setVisible(true);
                //start playing circle animation to show progress
                rt1.play();
                rt2.play();

                //remove file after program closes as we need it while program is running
                Conversion.deleteFileOnExit("src/pass.txt");

                //if the contents was decrypted correctly use it to decrypt the contents
                Conversion.encryptOrDecryptFile(password, "src/encryptedEncodedFile.txt","src/encodedfile.txt", Cipher.DECRYPT_MODE);
                //remove after program closes
                Conversion.deleteFileOnExit("src/encodedfile.txt");

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
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../controller/view/sample.fxml"));
                            Parent root = fxmlLoader.load();
                            Scene mainScene = new Scene(root, 800, 500);

                            //pass chromedriver and options to main screen
                            Controller controller = fxmlLoader.getController();
                            controller.setChromeOptions(chromeOptions);
                            controller.setChromeDriverStack(chromeDriverStack);
                            //pass reference scene and controller to controller, this is used when returning to the scene;
                            controller.setMainScreenController(controller);
                            controller.setMainScene(mainScene);
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
