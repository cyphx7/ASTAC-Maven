package com.ui;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class SoundManager {
    
    private MediaPlayer musicPlayer;

    public void playBackgroundMusic(String fileName) {
        try {
            URL resource = getClass().getResource(fileName);
            
            if (resource == null) {
                System.out.println("Error: Music file not found at " + fileName);
                return;
            }

            Media sound = new Media(resource.toExternalForm());
            
            if (musicPlayer != null) {
                musicPlayer.stop();
            }

            musicPlayer = new MediaPlayer(sound);

            musicPlayer.setCycleCount(MediaPlayer.INDEFINITE); 
            musicPlayer.setVolume(0.5); 
            musicPlayer.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopMusic() {
        if (musicPlayer != null) {
            musicPlayer.stop();
        }
    }

    public void setVolume(double volume) {
        if (musicPlayer != null) {
            musicPlayer.setVolume(volume);
        }
    }

    public void playSFX(String fileName) {
        try {
            URL resource = getClass().getResource(fileName);
            if (resource == null) {
                System.out.println("SFX Missing: " + fileName);
                return;
            }

            AudioClip clip = new AudioClip(resource.toExternalForm());
            clip.setVolume(0.7); 
            clip.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
