package com.chess.chessgame.services;
import com.chess.chessgame.domain.figures.ChessFigure;
import javafx.scene.Scene;



public interface GameFieldService {
    Scene createGameScene();
    void setFigureOnBoard(ChessFigure chessFigure);
}
