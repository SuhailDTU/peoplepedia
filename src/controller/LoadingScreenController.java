package controller;

import javafx.animation.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import model.EaseOfUse;

import java.io.InputStream;

public class LoadingScreenController {




    @FXML
    public ImageView LoadingImage;
    @FXML
    public Label loadingText;
    //loading bar
    @FXML
    public Rectangle loadingbar;
    //moving rectangle within loading bar to show progress
    @FXML
    public Rectangle movingRectangle;

    RotateTransition rotateTransition;
    PauseTransition pauseTransition;
    Timeline timeline = new Timeline();
    Timeline timeline2 = new Timeline();
    ScaleTransition scaleTransition;


    public LoadingScreenController(){

    }
    public void initialize(){
        //set set the bars to follow eachother
        movingRectangle.xProperty().bind(loadingbar.xProperty());

        //get desired image from jar file and put in middle and anchor it
        InputStream inputStreamPng = getClass().getClassLoader().getResourceAsStream("smiley.png");
        if(inputStreamPng == null){
            System.out.println("failed getting image");
        }else {
            System.out.println("succesfully retrieved image");
        }

        //create image
        LoadingImage.setImage(new Image(inputStreamPng));

        //create animation with picture
        //rotate image
        rotateTransition = new RotateTransition(Duration.seconds(1), LoadingImage);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(1);
        //Change label text
        KeyFrame keyFrame = new KeyFrame(Duration.millis(200), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (loadingText.getText().contains("...")){
                    loadingText.setText("Loading");
                }else{
                    loadingText.setText(loadingText.getText()+".");
                }
            }
        });
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(8);

        scaleTransition = new ScaleTransition(Duration.millis(500), LoadingImage);



        scaleTransition.setFromX(0.1);
        scaleTransition.setFromY(0.1);
        scaleTransition.setByX(1);
        scaleTransition.setByY(1);
        scaleTransition.setCycleCount(1);


        KeyFrame keyFrame2 = new KeyFrame(Duration.seconds(1.2), new KeyValue(movingRectangle.widthProperty(), loadingbar.getWidth()));
        timeline2.getKeyFrames().add(keyFrame2);
        timeline2.setCycleCount(1);


    }
    public void changeLabelColor(String colorHex, String colorHex2){
        loadingText.setStyle(" -fx-background-color: "+colorHex);
        movingRectangle.setFill(Color.web(colorHex2));
    }


    public void playAnimation(){
        //rotate
        rotateTransition.play();
        //make loading dots
        timeline.play();
        //make loading bar move
        timeline2.play();
        //make smiley go bigger
        LoadingImage.setScaleX(0.1);
        LoadingImage.setScaleY(0.1);
        scaleTransition.play();
    }
    public void resetLoadingBar(){
        movingRectangle.setWidth(10);
    }

}
