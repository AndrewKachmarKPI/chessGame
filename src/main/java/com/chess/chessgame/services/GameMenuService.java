package com.chess.chessgame.services;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public interface GameMenuService {
    File selectFileDialog(Stage stage);
    void setGameFileName(String fileName, String fileContent);
    Scene createMainScene();
}
