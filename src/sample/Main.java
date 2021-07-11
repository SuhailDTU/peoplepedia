package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Conversion;

import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../controller/view/loginScreen.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Peoplepedia login");
        primaryStage.setScene(new Scene(root, 600, 400));

        //if window closes re-encrypt encodedfile to use for next time
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                String pass = "";
                try(BufferedReader bufferedReader = new BufferedReader(new FileReader("src/pass.txt"))) {
                    pass = bufferedReader.readLine().trim();
                }catch (IOException ioException){

                }
                Conversion.encryptOrDecryptFile(pass, "src/encodedfile.txt", "src/encryptedEncodedFile.txt", Cipher.ENCRYPT_MODE );
            }
        });

        //show stage
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
