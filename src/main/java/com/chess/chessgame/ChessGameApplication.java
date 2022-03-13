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
//        stage.setFullScreen(true);
        stage.setResizable(false);
//        stage.setMaxWidth(720);
//        stage.setMaxHeight(1000);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
//TODO DISABLE KING MOVE UNDER OTHER FIGURE TRAJECTORY
