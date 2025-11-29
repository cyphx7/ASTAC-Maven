package com.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class SettingsScreen {
    private final VBox layout;

    public SettingsScreen(WindowManager manager) {
        // 1. Layout Setup (Matches GuideScreen)
        layout = new VBox(25);
        layout.setAlignment(Pos.CENTER);
        // Use Theme.BG_COLOR to match the GuideScreen background exactly
        layout.setStyle("-fx-background-color: " + Theme.BG_COLOR + "; -fx-padding: 40;");

        // 2. Title (Matches GuideScreen)
        Label title = new Label("SYSTEM SETTINGS");
        title.setTextFill(Color.web(Theme.ACCENT_COLOR));
        title.setFont(Theme.FONT_HEADER);

        // 3. Music Controls
        Label musicLabel = new Label("MUSIC VOLUME");
        musicLabel.setTextFill(Color.WHITE);
        musicLabel.setFont(Theme.FONT_NORMAL);

        Slider musicSlider = createNeonSlider();
        // Get current volume (0.0 to 1.0) and convert to slider (0 to 100)
        musicSlider.setValue(manager.getSoundManager().getMusicVolume() * 100);
        musicSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            manager.getSoundManager().setMusicVolume(newVal.doubleValue() / 100.0);
        });

        // 4. SFX Controls
        Label sfxLabel = new Label("SFX VOLUME");
        sfxLabel.setTextFill(Color.WHITE);
        sfxLabel.setFont(Theme.FONT_NORMAL);

        Slider sfxSlider = createNeonSlider();
        sfxSlider.setValue(manager.getSoundManager().getSFXVolume() * 100);
        sfxSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            manager.getSoundManager().setSFXVolume(newVal.doubleValue() / 100.0);
        });

        // 5. Back Button (Matches GuideScreen style)
        Button btnBack = Theme.createStyledButton("BACK TO MENU");
        btnBack.setOnAction(e -> manager.showMainMenu());

        // 6. Add to Layout
        // Added some spacing/spacer labels for visual separation
        layout.getChildren().addAll(
                title,
                new Label(""), // Spacer
                musicLabel,
                musicSlider,
                new Label(""), // Spacer
                sfxLabel,
                sfxSlider,
                new Label(""), // Spacer
                btnBack
        );

        // Load the CSS for the sliders so they still glow
        try {
            layout.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("Warning: style.css not found.");
        }
    }

    public VBox getLayout() {
        return layout;
    }

    private Slider createNeonSlider() {
        Slider slider = new Slider(0, 100, 50);
        slider.setMaxWidth(400);
        slider.getStyleClass().add("neon-slider");
        return slider;
    }
}