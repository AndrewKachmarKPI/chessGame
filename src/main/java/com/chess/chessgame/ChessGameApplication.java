package com.chess.chessgame;

import com.chess.chessgame.serviceImpl.GameFieldService;
import com.chess.chessgame.serviceImpl.GameFileService;
import javafx.application.Application;
import javafx.stage.Stage;

import static com.chess.chessgame.serviceImpl.GameFileService.loadImageByPath;


public class ChessGameApplication extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Chess game!");
        stage.setScene(GameFieldService.createGameScene());
        stage.getIcons().add(loadImageByPath("images/mainIcon.png"));
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
