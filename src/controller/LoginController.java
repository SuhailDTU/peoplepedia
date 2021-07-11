package controller;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.imageio.IIOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javafx.util.Duration;
import model.Conversion;

public class LoginController {

    @FXML
    public TextField textField;
    @FXML
    public Label passwordState;
    @FXML
    public Button enterButton;

    public Conversion converter;
    public LoginController(){


    }

    public void Initialize(){


    }


    public void checkPassword(ActionEvent actionEvent) {

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
                        Parent root = FXMLLoader.load(getClass().getResource("../controller/view/sample.fxml"));
                        stage.setTitle("Peoplepedia");
                        stage.setScene(new Scene(root, 800, 500));
                        stage.show();
                    }catch (IOException ioException){
                        ioException.printStackTrace();
                    }
                }
            });
            pauseTransition.play();



        }else {
            passwordState.setText("the password "+textField.getText()+" is wrong");
            //passwordField.clear();
            passwordState.setTextFill(Color.RED);


        }


    }
}
