package com.ui;

import logic.Chatbot;
import logic.GameSession;
import logic.Question;
import java.util.List;
import java.util.Random;

/**
 * Controls game logic and UI interactions during gameplay.
 * Handles answer submission, chatbot commands, and game state updates.
 */
public class GameController {
    private final GameSession session;
    private final GameUI ui;
    private final WindowManager manager;

    public GameController(GameSession session, GameUI ui, WindowManager manager) {
        this.session = session;
        this.ui = ui;
        this.manager = manager;
        initController();
        updateUI();
    }

    private void initController() {
        manager.playClickSound();
        for (int i = 0; i < 4; i++) {
            final int index = i;
            ui.getOptionButtons()[i].setOnAction(e -> handleAnswer(index));
        }

        ui.getBtnAsk().setOnAction(e -> {handleAskBot(); manager.playClickSound();});
        ui.getBtnCopy().setOnAction(e -> {handleCopyPaste(); manager.playClickSound();});

        if (manager.isAskUsed()) {
            ui.getBtnAsk().setDisable(true);
            ui.getBtnAsk().setText("");
            ui.getBtnSave().setStyle("-fx-text-fill: " + Theme.ERROR_COLOR + "; -fx-border-color: " + Theme.ERROR_COLOR + ";");
        }
        if (manager.isCopyUsed()) {
            ui.getBtnCopy().setDisable(true);
            ui.getBtnSave().setStyle("-fx-text-fill: " + Theme.ERROR_COLOR + "; -fx-border-color: " + Theme.ERROR_COLOR + ";");
            ui.getBtnCopy().setText("");
        }
        if (manager.isSaveUsed()) {
            ui.getBtnSave().setText("");
            ui.getBtnSave().setStyle("-fx-text-fill: " + Theme.ERROR_COLOR + "; -fx-border-color: " + Theme.ERROR_COLOR + ";");
        }
    }

    private void updateUI() {
        Question q = session.getCurrentQuestion();
        if (q == null) return;

        ui.getSubjectLabel().setText("Subject: " + q.getSubject());
        ui.getQuestionLabel().setText(q.getText());

        if (q.getCodeSnippet() != null && !q.getCodeSnippet().isEmpty()) {
            ui.getCodeArea().setText(q.getCodeSnippet());
            ui.getCodeArea().setVisible(true);
        } else {
            ui.getCodeArea().setVisible(false);
        }

        List<String> opts = q.getOptions();
        for (int i = 0; i < 4; i++) {
            if (i < opts.size()) {
                ui.getOptionButtons()[i].setText(opts.get(i));
                ui.getOptionButtons()[i].setDisable(false);
            } else {
                ui.getOptionButtons()[i].setText("-");
                ui.getOptionButtons()[i].setDisable(true);
            }
        }

        Chatbot bot = session.getCurrentChatbot();
        ui.getBotNameLabel().setText(bot.getName() + " says:");
        if (ui.getStatsLabel() != null) {
            if (bot.isRevealed()) {
                ui.getStatsLabel().setText("Stats:\nSTR: " + bot.getStrengthSubject() + "\nWK: " + bot.getWeaknessSubject());
            } else {
                ui.getStatsLabel().setText("Stats:\n[HIDDEN]");
            }
        }

        String introMsg = bot.isRevealed()
                ? String.format("Hey! I am good at %s but bad at %s.",
                bot.getStrengthSubject(),
                bot.getWeaknessSubject())
                : "Waiting...";
        ui.getDialogLabel().setText(introMsg);

        double progress = (double) (manager.getGlobalScore() + session.getScore()) / 14.0;
        ui.getProgressBar().setProgress(progress);
        ui.getProgressLabel().setText("Total Progress: " + (int)(progress * 100) + "%");
    }

    private void handleAnswer(int index) {
        GameSession.GameResult result = session.submitAnswer(index);

        if (result == GameSession.GameResult.CORRECT) {
            manager.playSuccessSound();
            manager.showCustomAlert("CORRECT!", "Good job! Proceeding...", this::checkGameStatus);
        }
        else if (result == GameSession.GameResult.SAVED_BY_CHATBOT) {
            manager.playErrorSound();
            ui.getBtnSave().setText("");
            ui.getBtnSave().setDisable(true);
            manager.markSaveUsed();


            manager.showCustomAlert("SAVED!", "You were wrong, but " + session.getCurrentChatbot().getName() + " saved you!", this::checkGameStatus);
        }
        else if (result == GameSession.GameResult.WRONG_AND_FAILED) {
            manager.playErrorSound();
            int totalScore = manager.getGlobalScore() + session.getScore();
            int percent = (int) ((totalScore / 14.0) * 100);

            String msg = "You were wrong!\nBot attempted to Save you, but FAILED.\n\nFinal Score: " + totalScore + "/14 (" + percent + "%)";
            // CHANGED: Use endGame() to reset state
            manager.showCustomAlert("GAME OVER", msg, manager::endGame);
        }
        else {
            manager.playErrorSound();
            int totalScore = manager.getGlobalScore() + session.getScore();
            int percent = (int) ((totalScore / 14.0) * 100);

            String msg = "You threw an exception!\n\nFinal Score: " + totalScore + "/14 (" + percent + "%)";
            // CHANGED: Use endGame() to reset state
            manager.showCustomAlert("GAME OVER", msg, manager::endGame);
        }
    }

    private void checkGameStatus() {
        if (session.isGameWon()) {
            manager.showCustomAlert("ROUND COMPLETE", "Returning to Subject Board...", () ->
                    manager.finishRound(session.getSubject(), session.getScore())
            );
        } else {
            updateUI();
        }
    }

    private void handleAskBot() {
        manager.markAskUsed();
        ui.getBtnAsk().setDisable(true);
        ui.getBtnAsk().setText("");

        Question q = session.getCurrentQuestion();
        Chatbot bot = session.getCurrentChatbot();
        boolean success = bot.calculateSuccess(q.getSubject());

        String subject = q.getSubject();
        boolean isStrong = subject.equalsIgnoreCase(bot.getStrengthSubject());
        boolean isWeak = subject.equalsIgnoreCase(bot.getWeaknessSubject());

        int correctOpt = q.getCorrectAnswerIndex() + 1;
        String dialogue;

        if (success) {
            if (isStrong) {
                dialogue = "I'm an expert at " + subject + "! The answer is definitely Option " + correctOpt + ".";
            } else if (isWeak) {
                dialogue = "Ugh, I hate " + subject + "... but I think it's Option " + correctOpt + "?";
            } else {
                dialogue = "I am 90% sure it is Option " + correctOpt + ".";
            }
        } else {
            int wrong;
            do { wrong = new Random().nextInt(4); } while(wrong == q.getCorrectAnswerIndex());

            if (isStrong) {
                dialogue = "Trust me, I know " + subject + ". It has to be Option " + (wrong + 1) + "!";
            } else {
                dialogue = "I have no idea. I'm guessing Option " + (wrong + 1) + "...";
            }
        }

        ui.getDialogLabel().setText(dialogue);
    }

    private void handleCopyPaste() {
        manager.markCopyUsed();
        ui.getBtnCopy().setDisable(true);
        ui.getBtnCopy().setText("");

        Question q = session.getCurrentQuestion();
        Chatbot bot = session.getCurrentChatbot();
        boolean success = bot.calculateSuccess(q.getSubject());

        int finalChoice;
        if (success) {
            finalChoice = q.getCorrectAnswerIndex();
        } else {
            int wrong;
            do { wrong = new Random().nextInt(4); } while(wrong == q.getCorrectAnswerIndex());
            finalChoice = wrong;
        }

        manager.showCustomAlert("COPY PASTE",
                "Bot chose Option " + (finalChoice + 1) + ".\nSubmitting...",
                () -> handleAnswer(finalChoice)
        );
    }
}