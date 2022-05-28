package com.chess.chessgame;

import com.chess.chessgame.serviceImpl.GameFileServiceImpl;
import com.chess.chessgame.serviceImpl.GameMenuServiceImpl;
import com.chess.chessgame.services.GameFileService;
import javafx.application.Application;
import javafx.stage.Stage;




public class ChessGameApplication extends Application {
    @Override
    public void start(Stage stage) {
        GameFileService gameFileService = new GameFileServiceImpl();
        GameMenuServiceImpl gameMenuServiceImpl = new GameMenuServiceImpl();
        stage.setTitle("Chess game!");
        stage.setScene(gameMenuServiceImpl.createMainScene());
        stage.getIcons().add(gameFileService.loadImageByPath("images/main-icon.png"));
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
