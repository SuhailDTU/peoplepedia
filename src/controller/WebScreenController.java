package controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class WebScreenController {


    @FXML
    public Label nameLabel;
    @FXML
    public WebView webview;
    public WebEngine webEngine;

    public WebScreenController(){
    }

    public void initialize(){
        //get the webengine from the webview object
        webEngine = webview.getEngine();

    }

    public void setNameLabel(String name){
        nameLabel.setText(name);
    }
    public void startVideo(String url){
        //start video
        webEngine.load(url);
    }
}
