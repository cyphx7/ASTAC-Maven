package com.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Main menu screen with navigation options for the game.
 */
public class MainMenu {
    private final StackPane layout;

    public MainMenu(WindowManager manager) {
        Image bgImage = new Image(getClass().getResourceAsStream("/res/b.gif"));
        ImageView bgView = new ImageView(bgImage);
        bgView.setSmooth(false);
        bgView.setPreserveRatio(true);

        VBox contentBox = new VBox(350);
        contentBox.setAlignment(Pos.CENTER);

        Image titleImage = new Image(getClass().getResourceAsStream("/res/title.gif"));
        ImageView titleView = new ImageView(titleImage);
        titleView.setSmooth(false);
        titleView.setPreserveRatio(true);

        // --- NEW LOGIC: CONTINUE vs PLAY ---
        Button btnPlay;
        if (manager.isGameActive()) {
            btnPlay = createSpriteButton("CONTINUE");
            btnPlay.setOnAction(e -> manager.continueGame());
        } else {
            btnPlay = createSpriteButton("PLAY GAME");
            btnPlay.setOnAction(e -> manager.startNewGame());
        }

        Button btnGuide = createSpriteButton("GUIDE");
        Button btnSettings = createSpriteButton("SETTINGS");
        Button btnExit = createSpriteButton("EXIT");

        btnGuide.setOnAction(e -> manager.showGuide());
        btnSettings.setOnAction(e -> manager.showSettings());
        btnExit.setOnAction(e -> System.exit(0));

        HBox buttonRow = new HBox(20);
        buttonRow.setAlignment(Pos.CENTER);
        buttonRow.getChildren().addAll(btnPlay, btnGuide, btnSettings, btnExit);

        contentBox.getChildren().addAll(titleView, buttonRow);

        layout = new StackPane();
        layout.getChildren().addAll(bgView, contentBox);
        bgView.fitWidthProperty().bind(layout.widthProperty().multiply(0.50));
        bgView.fitHeightProperty().bind(layout.heightProperty().multiply(0.30));
        layout.setStyle("-fx-background-color: #000000");
    }

    public StackPane getLayout() {
        return layout;
    }

    private Button createSpriteButton(String text) {
        Button btn = new Button(text);

        Image img = new Image(getClass().getResourceAsStream("/res/mainbutton.png"));
        ImageView view = new ImageView(img);

        view.setFitWidth(128);
        view.setFitHeight(64);
        view.setSmooth(false);

        btn.setGraphic(view);

        btn.setContentDisplay(ContentDisplay.CENTER);

        btn.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-padding: 0; " +
                        "-fx-background-radius: 0; " +
                        "-fx-border-width: 0;"
        );

        btn.setTextFill(Color.WHITE);
        btn.setFont(Theme.FONT_NORMAL);

        return btn;
    }
}