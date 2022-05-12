package com.chess.chessgame.services;
import com.chess.chessgame.domain.figures.ChessFigure;
import javafx.scene.Group;

import java.io.File;


public interface GameFieldService {
    Group createGameGroup();
    void onLoadGame(File file);
    void onStartGame();
}
