package com.chess.chessgame.services;

import com.chess.chessgame.domain.figures.*;

import java.util.*;


public interface GameService {
    void initGame();

    void loadFigures();

    void fillFigureMap();

    List<ChessFigure> getAttackedFigures(ChessFigure chessFigure);

    List<Position> convertMatrixToPositionList(int[][] figureMatrix);

    void loadFiguresOnBoard();

    int getFigureNumber(ChessFigure chessFigure);

    Map<ChessFigure, List<ChessFigure>> getAttackMap();

    int[][] getFigureTrajectory(ChessFigure chessFigure);

    void clearGameBoard();


    void addNewFigure(ChessFigure chessFigure);

    List<ChessFigure> getAvailableFigures();


    void removeFigure(Position position);
}
