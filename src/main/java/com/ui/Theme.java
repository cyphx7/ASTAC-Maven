package com.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Provides consistent colors, fonts, and button styling.
 */
public class Theme {

    public static final String BG_COLOR = "#0d0d0d";
    public static final String PRIMARY_COLOR = "#1a1a1a";
    public static final String ACCENT_COLOR = "#00e5ff";
    public static final String ERROR_COLOR = "#ff3333";


    public static final Font FONT_HEADER = Font.font("Consolas", FontWeight.BOLD, 28);
    public static final Font FONT_NORMAL = Font.font("Consolas", FontWeight.NORMAL, 16);

    public static void applyCyberpunkStyle(Button btn) {
        if (btn == null) return;

        btn.setFont(FONT_NORMAL);
        btn.setTextFill(Color.WHITE);
        btn.setBackground(new Background(new BackgroundFill(Color.web(PRIMARY_COLOR), new CornerRadii(5), Insets.EMPTY)));
        btn.setStyle("-fx-border-color: " + ACCENT_COLOR + "; -fx-border-width: 1px;");
        btn.setPadding(new Insets(10, 20, 10, 20));


        btn.setOnMouseEntered(e -> {
            btn.setBackground(new Background(new BackgroundFill(Color.web(ACCENT_COLOR), new CornerRadii(5), Insets.EMPTY)));
            btn.setTextFill(Color.BLACK);
        });

        btn.setOnMouseExited(e -> {
            btn.setBackground(new Background(new BackgroundFill(Color.web(PRIMARY_COLOR), new CornerRadii(5), Insets.EMPTY)));
            btn.setTextFill(Color.WHITE);
        });
    }

    public static Button createStyledButton(String text) {
        Button btn = new Button(text);
        applyCyberpunkStyle(btn);
        return btn;
    }
}
