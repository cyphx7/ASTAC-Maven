package com.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.Random;
import java.util.Set;

/**
 * Subject selection screen showing available programming topics.
 * Completed subjects are disabled and marked as done.
 */
public class SubjectSelection {
    private final StackPane root;
    private final VBox contentLayout;
    private final WindowManager manager;


    private final String[] subjects = {
            "INTRO",           // Intro to Paradigms
            "PROCEDURAL",      // Procedural Programming
            "FUNCTIONAL",      // Functional Programming
            "OOP",             // Object-Oriented Programming
            "IMP_DEC",         // Imperative vs Declarative
            "EVENT_DRIVEN",    // Event-Driven Programming
            "MAPPINGS"         // Component Mappings
    };

    public SubjectSelection(WindowManager manager, Set<String> completedSubjects) {
        this.manager = manager;
        root = new StackPane();
        Rectangle backlight = new Rectangle();
        backlight.setFill(Color.WHITE);
        backlight.setOpacity(0.2);
        backlight.widthProperty().bind(root.widthProperty());
        backlight.heightProperty().bind(root.heightProperty());

        Random rand = new Random();
        Timeline backgroundFlicker = new Timeline(
            new KeyFrame(Duration.millis(50), e -> {
                double chance = rand.nextDouble(); 

                if (chance < 0.01) {
                    backlight.setOpacity(0.4 + (rand.nextDouble() * 0.2)); 
                } else if (chance < 0.05) {
                    backlight.setOpacity(0.05); 
                } else {
                    backlight.setOpacity(0.2);
                }
            })
        );
        backgroundFlicker.setCycleCount(Timeline.INDEFINITE);
        backgroundFlicker.play();

        contentLayout = new VBox(30);
        contentLayout.setAlignment(Pos.CENTER);

        Image bgImage = new Image(getClass().getResourceAsStream("/res/hologram.png"));
        ImageView bgView = new ImageView(bgImage);
        bgView.setSmooth(false);
        bgView.setPreserveRatio(true);
        bgView.setManaged(false);
        bgView.fitWidthProperty().bind(root.widthProperty());
        bgView.fitHeightProperty().bind(root.heightProperty());

        Label title = new Label("SELECT A SUBJECT");
        title.setTextFill(Color.web(Theme.ACCENT_COLOR));
        title.setFont(Theme.FONT_HEADER);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(20);

        for (int i = 0; i < subjects.length; i++) {
            String subjectName = subjects[i];
            Button btn = Theme.createStyledButton(subjectName);
            btn.setPrefSize(200, 60);

            if (completedSubjects.contains(subjectName)) {
                btn.setDisable(true);
                btn.setText(subjectName + " (DONE)");
                btn.setStyle("-fx-background-color: #111; -fx-text-fill: #555; -fx-border-color: #555;");
            } else {
                btn.setOnAction(e -> manager.onSubjectSelected(subjectName));
            }


            grid.add(btn, i % 3, i / 3);
        }
        contentLayout.getChildren().addAll(title, grid);
        root.getChildren().addAll(backlight, bgView, contentLayout);
    }

    public StackPane getLayout() {
        return root;
    }
}
