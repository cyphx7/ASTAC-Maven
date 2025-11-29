package com.ui;

import javafx.scene.Scene;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;

public class CursorManager {

    private ImageCursor defaultCursor;
    private ImageCursor pressedCursor;

    private Scene scene;

    public CursorManager(Scene scene) {
        this.scene = scene;
        loadCursors();
        setupGlobalEvents();
    }

    private void loadCursors() {

        Image cursorAtlas = new Image(getClass().getResourceAsStream("/res/mouse.png"));
        PixelReader reader = cursorAtlas.getPixelReader();

        WritableImage defaultImg = new WritableImage(reader, 32, 0, 32, 32);

        WritableImage pressedImg = new WritableImage(reader, 64, 0, 32, 32);

        defaultCursor = new ImageCursor(defaultImg, 0, 0);
        pressedCursor = new ImageCursor(pressedImg, 0, 0);

        scene.setCursor(defaultCursor);
    }

    private void setupGlobalEvents() {
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> scene.setCursor(pressedCursor));
        scene.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> scene.setCursor(defaultCursor));
    }
}