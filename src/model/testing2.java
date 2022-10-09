package model;

import controller.WebScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.URI;

public class testing2 extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("directory path "+new File("..").getPath());
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../controller/view/WebScreen.fxml"));
        Parent root = fxmlLoader.load();
        WebScreenController webScreenController = fxmlLoader.getController();
        webScreenController.setNameLabel("testname");
        webScreenController.startVideo("https://www.google.com/");

        primaryStage.setScene(new Scene(root,800, 500));
        primaryStage.show();
    }
}
