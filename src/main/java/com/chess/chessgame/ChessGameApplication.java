package com.chess.chessgame;

import com.chess.chessgame.services.GameFieldService;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class ChessGameApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Chess game!");
        stage.setScene(GameFieldService.createGameScene());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
