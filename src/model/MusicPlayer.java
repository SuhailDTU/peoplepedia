package model;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;

public class MusicPlayer {
    //music player testing
    ArrayList<Media> musicTracks = new ArrayList<Media>();
    MediaPlayer player;
    int currentChosenTrack = 0;
    public PlayState playState = PlayState.stopped;
    private float volume = 0.5F;


    public MusicPlayer() {
        loadInPreDefinedTracks();
    }

    public void loadInPreDefinedTracks(){
        String musicPath = getClass().getResource("/bensound-jazzyfrenchy.mp3").toString();
        musicTracks.add(new Media(musicPath));
    }
    public void playTrack(int trackNumber){
        player = new MediaPlayer(musicTracks.get(trackNumber));
        player.setVolume(volume);
        player.play();

    }
    public void setVolume(float volume){
      this.volume = (volume/100);
      if (playState == PlayState.playing){
          player.setVolume(this.volume);
      }
    }
    public void pauseTrack(){
        player.pause();
        playState = PlayState.stopped;
    }
    public void startTrack(){
        player.setVolume(volume);
        player.play();
        playState = PlayState.playing;
    }
    public void playChosenTrack(){
        playTrack(currentChosenTrack);
        playState = PlayState.playing;
    }


}
