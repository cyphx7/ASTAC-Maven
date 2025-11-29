package com.ui;

import javafx.application.Application;
import javafx.stage.Stage;
import com.ui.WindowManager;
/**
 * Main entry point for the "Are You Smarter Than a ChatBot?" game.
 * A JavaFX application where players compete against AI chatbots in programming knowledge.
 */

public class Main extends Application {
    public void start(Stage primaryStage) {
        WindowManager windowManager = new WindowManager(primaryStage);
        windowManager.showMainMenu();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
