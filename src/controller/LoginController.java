package controller;

import javafx.event.ActionEvent;
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

import javax.imageio.IIOException;
import java.io.IOException;

public class LoginController {

    @FXML
    public TextField textField;
    @FXML
    public Label passwordState;
    @FXML
    public Button enterButton;

    public LoginController(){


    }

    public void Initialize(){


    }


    public void checkPassword(ActionEvent actionEvent) {

        if(textField.getText().equals("123")){
            passwordState.setText("the password is correct");
            passwordState.setTextFill(Color.GREEN);
            Stage stage = (Stage) textField.getScene().getWindow();

            try {
                Parent root = FXMLLoader.load(getClass().getResource("../controller/view/sample.fxml"));
                stage.setTitle("Peoplepedia");
                stage.setScene(new Scene(root, 800, 500));
                stage.show();
            }catch (IOException ioException){
                ioException.printStackTrace();
            }


        }else {
            passwordState.setText("the password "+textField.getText()+" is wrong");
            //passwordField.clear();
            passwordState.setTextFill(Color.RED);


        }


    }
}
