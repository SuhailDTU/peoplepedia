package controller;

import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class WebScreenController {


    @FXML
    public Label nameLabel;
    @FXML
    public WebView webview;
    public WebEngine webEngine;
    //for realoding url incase of failure
    private String reloadLink;

    public WebScreenController(){
    }

    public void initialize(){
        //get the webengine from the webview object
        webEngine = webview.getEngine();
        webEngine.setOnStatusChanged(new EventHandler<WebEvent<String>>() {
            @Override
            public void handle(WebEvent<String> stringWebEvent) {
                System.out.println("webengine status: "+stringWebEvent.toString() + "\n" + webEngine.getLoadWorker().getState().toString() );
                //if something cancels the loading of the video do it again
                if(webEngine.getLoadWorker().getState() == Worker.State.CANCELLED){
                    webEngine.load(reloadLink);
                }
            }
        });
    }

    public void setNameLabel(String name){
        nameLabel.setText(name);
    }
    public void startVideo(String url){
        //start video
        reloadLink = url;
        webEngine.load(url);
    }
    public void getStatus(){
        webEngine.setOnStatusChanged(new EventHandler<WebEvent<String>>() {
            @Override
            public void handle(WebEvent<String> stringWebEvent) {
                System.out.println("webengine status: "+stringWebEvent.toString() + "\n" + webEngine.getLoadWorker().getState().toString() );

            }
        });
    }
}
