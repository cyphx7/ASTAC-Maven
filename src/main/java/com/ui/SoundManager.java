package com.ui;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class SoundManager {

    private MediaPlayer musicPlayer;
    private double musicVolume = 0.5; // Default 50%
    private double sfxVolume = 0.5;   // Default 50%

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
            musicPlayer.setVolume(musicVolume); // Use stored volume
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

    // --- Volume Controls ---

    public void setMusicVolume(double volume) {
        this.musicVolume = volume;
        if (musicPlayer != null) {
            musicPlayer.setVolume(musicVolume);
        }
    }

    public double getMusicVolume() {
        return musicVolume;
    }

    public void setSFXVolume(double volume) {
        this.sfxVolume = volume;
    }

    public double getSFXVolume() {
        return sfxVolume;
    }

    public void playSFX(String fileName) {
        try {
            URL resource = getClass().getResource(fileName);
            if (resource == null) {
                System.out.println("SFX Missing: " + fileName);
                return;
            }

            AudioClip clip = new AudioClip(resource.toExternalForm());
            clip.setVolume(sfxVolume); // Use stored SFX volume
            clip.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}