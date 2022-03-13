package com.chess.chessgame.services;

import com.chess.chessgame.domain.figures.ChessFigure;
import javafx.scene.image.Image;

import java.util.List;


public interface GameFileService {
    List<ChessFigure> getFiguresFromInitFile();

    void removeFigureFromFile(ChessFigure chessFigure);

    List<ChessFigure> getAllFigures();

    boolean clearFiguresFile();

    boolean writeFigureToFile(String figureLine);

    Image loadImageByPath(String path);
}
