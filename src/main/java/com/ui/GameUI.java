package com.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import logic.Chatbot;

/**
 * Main game interface displaying questions, options, and chatbot interactions.
 */
public class GameUI {
    private Chatbot currentChatbot;
    private final BorderPane uiLayer;
    private final StackPane root;
    private Label subjectLabel;
    private ImageView botAvatar;
    private Label questionLabel;
    private TextArea codeArea;
    private Button[] optionButtons;
    private Label dialogLabel;
    private Label botNameLabelBottom;
    private ProgressBar progressBar;
    private Label progressLabel;
    private Button btnAsk;
    private Button btnCopy;
    private Button btnSave;

    public GameUI(Chatbot chatbot) {
        this.currentChatbot = chatbot;

        Image bgImage = new Image(getClass().getResourceAsStream("/res/a.gif"));
        ImageView bgView = new ImageView(bgImage);
        bgView.setSmooth(false);
        bgView.setPreserveRatio(false);
        bgView.setManaged(false);

        uiLayer = new BorderPane();

        createTopPanel();
        createCenterPanel();
        createBottomPanel();

        root = new StackPane();
        root.setStyle("fx-background-color: black;");
        root.getChildren().addAll(bgView, uiLayer);

        bgView.fitWidthProperty().bind(root.widthProperty());
        bgView.fitHeightProperty().bind(root.heightProperty());

    }

    public Parent getRoot() { return root; }

    private void createTopPanel() {
    BorderPane topContainer = new BorderPane();
    topContainer.setPadding(new Insets(20));

    VBox topLeft = new VBox(10);
    topLeft.setAlignment(Pos.TOP_LEFT);

    Image chatbotAtlas = new Image(getClass().getResourceAsStream("/res/chatbots.png"));
    botAvatar = new ImageView(chatbotAtlas);
    int spriteSize = 32;
    int xOffset = 0;

    switch(currentChatbot.getName().toUpperCase()) {
        case "CHATGPT": xOffset = 0; break;
        case "GEMINI": xOffset = 1; break;
        case "GROK": xOffset = 2; break;
        case "COPILOT": xOffset = 3; break;
        case "CLAUDE": xOffset = 4; break;
        case "DEEPSEEK": xOffset = 5; break;
        case "PERPLEXITY": xOffset = 6; break;
        default: xOffset = 0; break;
    }

    botAvatar.setViewport(new Rectangle2D(xOffset * spriteSize, 0, spriteSize, spriteSize));
    botAvatar.setFitWidth(80);
    botAvatar.setFitHeight(80);
    botAvatar.setPreserveRatio(true);
    botAvatar.setSmooth(false);

    topLeft.getChildren().addAll(botAvatar);

    VBox topRight = new VBox(10);
    topRight.setAlignment(Pos.TOP_RIGHT);

    HBox toolsBox = new HBox(5); // Spacing between icons
    toolsBox.setAlignment(Pos.CENTER_RIGHT);

    Image toolsAtlas = new Image(getClass().getResourceAsStream("/res/commands.png"));

    ImageView askIcon = new ImageView(toolsAtlas);
    askIcon.setViewport(new Rectangle2D(0, 0, 128, 128)); // First sprite
    askIcon.setFitWidth(64); 
    askIcon.setFitHeight(64);
    askIcon.setSmooth(false);

    btnAsk = new Button();
    btnAsk.setGraphic(askIcon);
    btnAsk.setStyle("-fx-background-color: transparent; -fx-padding: 0; -fx-background-radius: 0; -fx-border-width: 0;");

    ImageView copyIcon = new ImageView(toolsAtlas);
    copyIcon.setViewport(new Rectangle2D(128, 0, 128, 128)); // Second sprite (x = 128)
    copyIcon.setFitWidth(64);
    copyIcon.setFitHeight(64);
    copyIcon.setSmooth(false);

    btnCopy = new Button();
    btnCopy.setGraphic(copyIcon);
    btnCopy.setStyle("-fx-background-color: transparent; -fx-padding: 0; -fx-background-radius: 0; -fx-border-width: 0;");

    ImageView saveIcon = new ImageView(toolsAtlas);
    saveIcon.setViewport(new Rectangle2D(256, 0, 128, 128));
    saveIcon.setFitWidth(64);
    saveIcon.setFitHeight(64);
    saveIcon.setSmooth(false);

    btnSave = new Button();
    btnSave.setGraphic(saveIcon);
    btnSave.setStyle("-fx-background-color: transparent; -fx-padding: 0; -fx-background-radius: 0; -fx-border-width: 0;");
    
    btnSave.setDisable(true); 

    toolsBox.getChildren().addAll(btnAsk, btnCopy, btnSave);

    subjectLabel = new Label("Subject: LOADING...");
    subjectLabel.setFont(Theme.FONT_NORMAL);
    subjectLabel.setTextFill(Color.WHITE);

    topRight.getChildren().addAll(toolsBox, subjectLabel);

    topContainer.setLeft(topLeft);
    topContainer.setRight(topRight);

    uiLayer.setTop(topContainer);
    }


    private void createCenterPanel() {
        VBox centerContainer = new VBox(30);
        centerContainer.setAlignment(Pos.TOP_CENTER);
        centerContainer.setPadding(new Insets(10, 40, 20, 40));

        Image qBoxAtlas = new Image(getClass().getResourceAsStream("/res/textbx.png"));
        ImageView qBoxBackground = new ImageView(qBoxAtlas);

        int xOffset = 0;

        Rectangle2D viewport = new Rectangle2D(xOffset, 0, 800, 150);
        qBoxBackground.setViewport(viewport);

        qBoxBackground.setPreserveRatio(false);
        qBoxBackground.setSmooth(false);

        StackPane qStack = new StackPane();
        qStack.setMaxWidth(800);

        qBoxBackground.fitWidthProperty().bind(qStack.widthProperty());
        qBoxBackground.fitHeightProperty().bind(qStack.heightProperty());


        VBox qBoxContent = new VBox(15);
        qBoxContent.setAlignment(Pos.TOP_CENTER);
        qBoxContent.setMaxWidth(800);
        qBoxContent.setStyle("-fx-padding: 15;");


        questionLabel = new Label("Loading Question...");
        questionLabel.setTextFill(Color.WHITE);
        questionLabel.setFont(Font.font("Consolas", FontWeight.BOLD, 18));
        questionLabel.setWrapText(true);
        questionLabel.setTextAlignment(TextAlignment.CENTER);

        codeArea = new TextArea();
        codeArea.setEditable(false);
        codeArea.setWrapText(true);
        codeArea.setMaxHeight(150);
        codeArea.setMaxWidth(800);
        codeArea.setStyle(
                "-fx-background-color: rgba(80,185,235,0.20); " +
                "-fx-control-inner-background: rgba(80,185,235,0.35); " +
                "-fx-font-family: 'Consolas'; " +
                "-fx-font-size: 17px; " +
                "-fx-text-fill: black; " +
                "-fx-highlight-fill: rgba(255,255,255,0.25); " +
                "-fx-highlight-text-fill: black; " +
                "-fx-border-color: #50b9eb; " +
                "-fx-border-width: 1.5; " +
                "-fx-background-radius: 10; " +
                "-fx-border-radius: 10; " +
                "-fx-effect: dropshadow(gaussian, rgba(80,185,235,0.35), 15, 0.3, 0, 0);"
        );
        codeArea.setVisible(false);

        qBoxContent.getChildren().addAll(questionLabel, codeArea);
        qStack.getChildren().addAll(qBoxBackground, qBoxContent);

        GridPane optionsGrid = new GridPane();
        optionsGrid.setAlignment(Pos.CENTER);
        optionsGrid.setHgap(20);
        optionsGrid.setVgap(20);

        optionButtons = new Button[4];
        Image buttonImage = new Image(getClass().getResourceAsStream("/res/choice.png"));
        for (int i = 0; i < 4; i++) {

            ImageView buttonSprite = new ImageView(buttonImage);
            buttonSprite.setFitWidth(400);
            buttonSprite.setFitHeight(90);

            optionButtons[i] = Theme.createStyledButton("Option " + (i + 1));
            optionButtons[i].setGraphic(buttonSprite);
            optionButtons[i].setContentDisplay(ContentDisplay.CENTER);
            optionButtons[i].setStyle(
                    "-fx-background-color: transparent; " +
                            "-fx-padding: 10 20 10 20; " +
                            "-fx-background-radius: 0;"

            );

            optionButtons[i].setWrapText(true);
            optionButtons[i].setTextAlignment(TextAlignment.LEFT);
            optionButtons[i].setPrefSize(400, 90);
            optionButtons[i].setMinSize(400, 90);
            optionButtons[i].setMaxSize(400, 90);
            optionButtons[i].setAlignment(Pos.CENTER_LEFT);

            optionsGrid.add(optionButtons[i], i % 2, i / 2);
        }

        centerContainer.getChildren().addAll(qStack, optionsGrid);
        uiLayer.setCenter(centerContainer);
    }

    private void createBottomPanel() {
        BorderPane bottomContainer = new BorderPane();
        bottomContainer.setPadding(new Insets(20));
        bottomContainer.setStyle("-fx-border-color: " + Theme.ACCENT_COLOR + "; -fx-border-width: 2px 0 0 0; -fx-background-color: #111;");


        VBox dialogueBox = new VBox(5);
        dialogueBox.setAlignment(Pos.CENTER_LEFT);
        dialogueBox.setMaxWidth(800);

        botNameLabelBottom = new Label("ASSISTANT SAYS:");
        botNameLabelBottom.setFont(Font.font("Consolas", FontWeight.BOLD, 12));
        botNameLabelBottom.setTextFill(Color.web(Theme.ACCENT_COLOR));

        dialogLabel = new Label();
        dialogLabel.setTextFill(Color.WHITE);
        dialogLabel.setFont(Theme.FONT_NORMAL);
        dialogLabel.setWrapText(true);

        dialogueBox.getChildren().addAll(botNameLabelBottom, dialogLabel);


        VBox progressBox = new VBox(5);
        progressBox.setAlignment(Pos.CENTER_RIGHT);
        progressBox.setPrefWidth(300);

        progressLabel = new Label("Progress");
        progressLabel.setTextFill(Color.WHITE);
        progressLabel.setFont(Font.font("Consolas", 12));

        progressBar = new ProgressBar(0.0);
        progressBar.setPrefWidth(280);
        progressBar.setStyle("-fx-accent: " + Theme.ACCENT_COLOR + ";");

        progressBox.getChildren().addAll(progressLabel, progressBar);

        bottomContainer.setLeft(dialogueBox);
        bottomContainer.setRight(progressBox);

        uiLayer.setBottom(bottomContainer);
    }


    public Label getSubjectLabel() { return subjectLabel; }
    public Label getQuestionLabel() { return questionLabel; }
    public TextArea getCodeArea() { return codeArea; }
    public Button[] getOptionButtons() { return optionButtons; }
    public Label getDialogLabel() { return dialogLabel; }
    public Label getBotNameLabel() { return botNameLabelBottom; }
    public Button getBtnAsk() { return btnAsk; }
    public Button getBtnCopy() { return btnCopy; }
    public Button getBtnSave() { return btnSave; }
    public Label getStatsLabel() { return null; }
    public ProgressBar getProgressBar() { return progressBar; }
    public Label getProgressLabel() { return progressLabel; }
}
