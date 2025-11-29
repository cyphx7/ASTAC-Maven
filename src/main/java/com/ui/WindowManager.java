package com.ui;

import data.JsonDataLoader;
import logic.Chatbot;
import logic.GameSession;
import logic.Question;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Central manager for window navigation and game state.
 * Handles scene transitions, global game state, and command usage tracking.
 */
public class WindowManager {
    private final Stage stage;
    private final Scene mainScene;
    private final StackPane rootStack;
    private final StackPane contentLayer;
    private JsonDataLoader dataLoader;
    private Chatbot currentChatbot;
    private Set<String> completedSubjects;
    private int globalScore;
    private boolean isAskUsed = false;
    private boolean isCopyUsed = false;
    private boolean isSaveUsed = false;
    private SoundManager soundManager;

    public WindowManager(Stage stage) {
        this.stage = stage;
        this.stage.setTitle("Are You Smarter Than a Chatbot?");
        this.stage.setFullScreenExitHint("");

        this.rootStack = new StackPane();
        this.contentLayer = new StackPane();
        
        this.rootStack.getChildren().addAll(contentLayer);
        this.rootStack.setStyle("-fx-background-color: black");
        this.mainScene = new Scene(rootStack, 1280, 720);

        this.stage.setScene(mainScene);

        new CursorManager(mainScene);
        soundManager = new SoundManager();

        dataLoader = new JsonDataLoader();
        dataLoader.loadQuestionsFromDirectory("MCQ");
        soundManager.playBackgroundMusic("/res/theme.mp3");
    }

    private void setRoot(Parent content) {
        contentLayer.getChildren().clear();
        contentLayer.getChildren().add(content);
        stage.setFullScreen(false);
        stage.show();
    }

    public void showCustomAlert(String title, String message, Runnable onDismiss) {
        Rectangle dimmer = new Rectangle(stage.getWidth(), stage.getHeight(), Color.rgb(0, 0, 0, 0.7));

        VBox box = new VBox(20);
        box.setAlignment(Pos.CENTER);
        box.setMaxSize(500, 300);
        box.setStyle("-fx-background-color: #222; -fx-border-color: " + Theme.ACCENT_COLOR + "; -fx-border-width: 3px; -fx-padding: 30;");

        Label lblTitle = new Label(title);
        lblTitle.setFont(Theme.FONT_HEADER);
        lblTitle.setTextFill(Color.web(Theme.ACCENT_COLOR));

        Label lblMsg = new Label(message);
        lblMsg.setFont(Theme.FONT_NORMAL);
        lblMsg.setTextFill(Color.WHITE);
        lblMsg.setWrapText(true);
        lblMsg.setStyle("-fx-text-alignment: center;");

        Button btnOk = Theme.createStyledButton("CONTINUE");
        btnOk.setOnAction(e -> {
            contentLayer.getChildren().removeAll(dimmer, box);
            if (onDismiss != null) onDismiss.run();
        });

        box.getChildren().addAll(lblTitle, lblMsg, btnOk);
        contentLayer.getChildren().addAll(dimmer, box);
    }

    public void showMainMenu() {
        playClickSound();
        MainMenu menu = new MainMenu(this);
        setRoot(menu.getLayout());
    }


    public void showGuide() {
        playClickSound();
        GuideScreen screen = new GuideScreen(this);
        setRoot(screen.getLayout());
    }

    public void startNewGame() {
        playClickSound();
        completedSubjects = new HashSet<>();
        globalScore = 0;
        isAskUsed = false;
        isCopyUsed = false;
        isSaveUsed = false;
        showChatbotSelection();
    }

    public void showChatbotSelection() {
        playClickSound();
        ChatbotSelection screen = new ChatbotSelection(this);
        setRoot(screen.getLayout());
    }

    public void showSubjectSelection() {
        playClickSound();
        SubjectSelection screen = new SubjectSelection(this, completedSubjects);
        setRoot(screen.getLayout());
    }

    public void onChatbotSelected(Chatbot bot) {
        this.currentChatbot = bot;
        showSubjectSelection();
    }

    public void onSubjectSelected(String subject) {
        try {
            List<Question> roundQuestions = getQuestionsForSubject(subject);

            if (roundQuestions.isEmpty()) {
                roundQuestions.add(new Question("Dummy Q1 (" + subject + ")", null, Arrays.asList("A","B","C","D"), 0, subject, Question.QuestionType.THEORETICAL));
                roundQuestions.add(new Question("Dummy Q2 (" + subject + ")", null, Arrays.asList("A","B","C","D"), 0, subject, Question.QuestionType.THEORETICAL));
            }

            GameSession roundSession = new GameSession(currentChatbot, roundQuestions, this);
            currentChatbot.revealStats();

            GameUI gameView = new GameUI(currentChatbot);
            new GameController(roundSession, gameView, this);

            setRoot(gameView.getRoot());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void finishRound(String subject, int score) {
        globalScore += score;
        completedSubjects.add(subject);

        if (completedSubjects.size() >= 7) {

            int percent = (int) ((globalScore / 14.0) * 100);

            String msg = "Final Score: " + globalScore + "/14 (" + percent + "%)\n";
            if (percent == 100) msg += "PERFECT SCORE! You are Smarter than a Chatbot!";
            else msg += "You survived, but are you smarter?";

            showCustomAlert("VICTORY", msg, this::showMainMenu);
        } else {
            showSubjectSelection();
        }
    }


    public boolean isAskUsed() { return isAskUsed; }
    public void markAskUsed() { this.isAskUsed = true; }
    public boolean isCopyUsed() { return isCopyUsed; }
    public void markCopyUsed() { this.isCopyUsed = true; }
    public boolean isSaveUsed() { return isSaveUsed; }
    public void markSaveUsed() { this.isSaveUsed = true; }
    public int getGlobalScore() { return globalScore; }

    private List<Question> getQuestionsForSubject(String subject) {
        List<Question> all = dataLoader.generateGameSet();
        return all.stream()
                .filter(q -> q.getSubject().equalsIgnoreCase(subject))
                .limit(2)
                .collect(Collectors.toList());
    }

    public void playClickSound() {
        soundManager.playSFX("/res/click.wav"); 
    }

    public void playSuccessSound() {
        soundManager.playSFX("/res/success.wav");
    }

    public void playErrorSound() {
        soundManager.playSFX("/res/error.wav");
    }

    
}
