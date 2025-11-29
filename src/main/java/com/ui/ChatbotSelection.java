package com.ui;

import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import logic.Chatbot;

/**
 * Screen for selecting an AI chatbot assistant with unique strengths and weaknesses.
 */
public class ChatbotSelection {
    private final StackPane root;
    private final VBox contentLayout;
    private final WindowManager manager;

    public ChatbotSelection(WindowManager manager) {
        this.manager = manager;

        root = new StackPane();
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #000000");

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
        Label title = new Label("CHOOSE YOUR ASSISTANT");
        title.setTextFill(Color.web(Theme.ACCENT_COLOR));
        title.setFont(Theme.FONT_HEADER);

        // Grid of Bots
        FlowPane botGrid = new FlowPane();
        botGrid.setAlignment(Pos.CENTER);
        botGrid.setHgap(20);
        botGrid.setVgap(20);
        botGrid.setPadding(new Insets(20));

        // The 7 Chatbots
        String[] botNames = {"CHATGPT", "GEMINI", "GROK", "COPILOT", "CLAUDE", "DEEPSEEK", "PERPLEXITY"};

        for (String name : botNames) {
            Pane botCard = createBotCard(name);
            botGrid.getChildren().add(botCard);
        }
        contentLayout.getChildren().addAll(title, botGrid);
        root.getChildren().addAll(backlight, bgView, contentLayout);
    }


    private Pane createBotCard(String name) {
        Image cardBgImage = new Image(getClass().getResourceAsStream("/res/card.png"));
        ImageView cardBgView = new ImageView(cardBgImage);


        cardBgView.setPreserveRatio(false);
        cardBgView.setSmooth(false);
        cardBgView.setStyle(
                "-fx-background-color: transparent; " + 
                "-fx-padding: 0; " + 
                "-fx-background-radius: 0;"               
            );

        VBox contentBox = new VBox(10);
        contentBox.setAlignment(Pos.CENTER);

        contentBox.setPadding(new Insets(25));

        Image chatbotAtlas = new Image(getClass().getResourceAsStream("/res/chatbots.png"));
        ImageView avatar = new ImageView(chatbotAtlas);
        int spriteIndex = getSpriteIndex(name);
        int spriteSize = 32;
        Rectangle2D viewport = new Rectangle2D(spriteIndex * spriteSize, 0, spriteSize, spriteSize);
        avatar.setViewport(viewport);
        avatar.setFitWidth(80);
        avatar.setFitHeight(80);
        avatar.setPreserveRatio(true);
        avatar.setSmooth(false);
        avatar.setStyle(
                "-fx-background-color: transparent; " + 
                "-fx-padding: 0; " + 
                "-fx-background-radius: 0;"               
            );

        Label nameLabel = new Label(name);
        nameLabel.setTextFill(Color.WHITE);
        nameLabel.setFont(Theme.FONT_NORMAL);

        String[] stats = assignStats(name);

        Button btnSelect = Theme.createStyledButton("SELECT");
        btnSelect.setOnAction(e -> {
            Chatbot selectedBot = new Chatbot(name, stats[0], stats[1]);
            manager.onChatbotSelected(selectedBot);
        });

        contentBox.getChildren().addAll(avatar, nameLabel, btnSelect);

        StackPane finalCardStack = new StackPane();

        finalCardStack.getChildren().addAll(cardBgView, contentBox);
        cardBgView.fitWidthProperty().bind(finalCardStack.widthProperty());
        cardBgView.fitHeightProperty().bind(finalCardStack.heightProperty());

        return finalCardStack;
    }

    private String[] assignStats(String name) {
        switch (name) {
            case "CHATGPT":  return new String[]{"INTRO", "MAPPINGS"};
            case "GEMINI":  return new String[]{"OOP", "FUNCTIONAL"};
            case "GROK": return new String[]{"PROCEDURAL", "EVENT_DRIVEN"};
            case "COPILOT":   return new String[]{"FUNCTIONAL", "OOP"};
            case "CLAUDE":   return new String[]{"IMP_DEC", "INTRO"};
            case "DEEPSEEK":   return new String[]{"EVENT_DRIVEN", "IMP_DEC"};
            case "PERPLEXITY":   return new String[]{"MAPPINGS", "PROCEDURAL"};
            default:       return new String[]{"INTRO", "OOP"};
        }
    }

    public Pane getLayout() {
        return root;
    }

    private int getSpriteIndex(String name) {
        switch(name.toUpperCase()) {
            case "CHATGPT": return 0;
            case "GEMINI": return 1;
            case "GROK": return 2;
            case "COPILOT": return 3;
            case "CLAUDE": return 4;
            case "DEEPSEEK": return 5;
            case "PERPLEXITY": return 6;
            default: return 0;
        }
    }
}
