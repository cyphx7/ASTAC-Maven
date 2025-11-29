package com.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

/**
 * Displays game rules and mechanics to help players understand the game.
 */
public class GuideScreen {
    private final VBox layout;

    public GuideScreen(WindowManager manager) {
        layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: " + Theme.BG_COLOR + "; -fx-padding: 40;");

        Label title = new Label("GAME MECHANICS");
        title.setTextFill(Color.web(Theme.ACCENT_COLOR));
        title.setFont(Theme.FONT_HEADER);

        String text =
                "OBJECTIVE:\n" +
                        "Answer 14 questions across 7 subjects to prove you are Smarter Than a Chatbot!\n\n" +
                        "COMMANDS (One use per game):\n" +
                        "1. ASK BOT: The Bot gives you a hint. They are smart in their Strengths but dumb in their Weaknesses.\n" +
                        "2. COPY PASTE: Automatically submits the Bot's answer. Risky!\n" +
                        "3. SAVE: Passive. If you answer wrong, the Bot attempts to save you. If they fail, it's Game Over.\n\n" +
                        "SCORING:\n" +
                        "Get 100% to win. One mistake (without a Save) ends the game.";

        Label content = new Label(text);
        content.setTextFill(Color.WHITE);
        content.setFont(Theme.FONT_NORMAL);
        content.setTextAlignment(TextAlignment.CENTER);
        content.setWrapText(true);
        content.setMaxWidth(800);

        // Back Button
        Button btnBack = Theme.createStyledButton("BACK TO MENU");
        btnBack.setOnAction(e -> manager.showMainMenu());

        layout.getChildren().addAll(title, content, btnBack);
    }

    public VBox getLayout() {
        return layout;
    }
}
