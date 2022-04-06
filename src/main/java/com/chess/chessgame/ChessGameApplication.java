package com.chess.chessgame;

import com.chess.chessgame.serviceImpl.GameFieldServiceImpl;
import com.chess.chessgame.serviceImpl.GameFileServiceImpl;
import com.chess.chessgame.services.GameFieldService;
import com.chess.chessgame.services.GameFileService;
import javafx.application.Application;
import javafx.stage.Stage;




public class ChessGameApplication extends Application {
    @Override
    public void start(Stage stage) {
        GameFileService gameFileService = new GameFileServiceImpl();
        GameFieldService gameFieldService = new GameFieldServiceImpl();
        stage.setTitle("Chess game!");
        stage.setScene(gameFieldService.createGameScene());
        stage.getIcons().add(gameFileService.loadImageByPath("images/mainIcon.png"));
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
