package com.chess.chessgame;

import com.chess.chessgame.services.GameFieldService;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

import static com.chess.chessgame.services.GameFieldService.loadImageByPath;

public class ChessGameApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Chess game!");
        stage.setScene(GameFieldService.createGameScene());
        stage.getIcons().add(loadImageByPath("D:\\PROJECTS\\chessGame\\src\\main\\resources\\images\\mainIcon.png"));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
