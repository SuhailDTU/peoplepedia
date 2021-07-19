package sample;

import controller.LoginController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.CategoryHandler;
import model.Conversion;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

public class Main extends Application {
    //This stack is for closing multiple chromedrivers if i decide to implement multiple windows
    Stack<ChromeDriver> chromeDriverStack = new Stack<ChromeDriver>();

    @Override
    public void start(Stage primaryStage) throws Exception{
        //create chromedriver to pass along to login screen
        System.setProperty("webdriver.chrome.driver","src/libs/chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--incognito");
        chromeOptions.addArguments("--start-maximized");


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../controller/view/loginScreen.fxml"));
        Parent root = fxmlLoader.load();
        root.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.ENTER){

                }
            }
        });
        primaryStage.setTitle("Peoplepedia login");
        primaryStage.setScene(new Scene(root, 600, 400));


        //adding key event handler to scene
        LoginController loginController = fxmlLoader.getController();
        LoginController.finishHandler newFinishHandler = loginController.new finishHandler();
        primaryStage.getScene().addEventHandler(KeyEvent.KEY_PRESSED, newFinishHandler);

        //pass the reference to the chromedriver along to login screen
        loginController.setChromeOptions(chromeOptions);
        loginController.setChromeDriverStack(chromeDriverStack);


        //if window closes re-encrypt encodedfile to use for next time
        //unless we are at log-in screen
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                //Get the password if it exists, if it does exist encrypt encodedfile, otherwise ignore exception
                String pass = "";
                try(BufferedReader bufferedReader = new BufferedReader(new FileReader("src/pass.txt"))) {
                    pass = bufferedReader.readLine().trim();
                    Conversion.encryptOrDecryptFile(pass, "src/encodedfile.txt", "src/encryptedEncodedFile.txt", Cipher.ENCRYPT_MODE );
                    System.out.println("\nSuccesfully saved data prior to exit");

                }catch (IOException ioException){
                    //ioException.printStackTrace();
                    System.out.println("\nCould not save data prior to exit");
                }

                //The reason we ignore these exceptions is because the only scenario where the pass file and encodedfile do not exist
                //is at the login screen where they are not created yet. Thus if we try to use the password
                //or re-encrypt the encodedfile when exiting the loginScreen it will give an error as these files do not exist yet.
                //These errors are expected so we ignore them. This exception happens due to a handler placed on the stage itself which encrypts
                //"encodedfile.txt" using the contents of "pass.txt" as a key inorder to save changes. This happens on the window exit event.
                //These two files are only created when successfully logged in, thus dont exist at the log-in screen


                //End all chromedrivers in stack
                int i = 0;
                ChromeDriver currentDriver;
                System.out.println("ChromeDriver Stack length " + chromeDriverStack.size());
                while(chromeDriverStack.empty() == false){
                    currentDriver = chromeDriverStack.pop();
                    i++;
                    System.out.println("Closed driver " + i);
                    currentDriver.quit();
                }


            }
        });


        //show stage
        primaryStage.show();
    }





    public static void main(String[] args) {
        launch(args);
    }


}
