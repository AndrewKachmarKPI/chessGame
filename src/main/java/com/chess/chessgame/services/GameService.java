package com.chess.chessgame.services;

import com.chess.chessgame.domain.figures.*;
import com.chess.chessgame.enums.FigureColor;
import com.chess.chessgame.enums.FigureName;
import java.util.*;


public interface GameService {
    void initGame(String fileName);

    List<ChessFigure> getFiguresForSetup();

    Map<ChessFigure, List<ChessFigure>> getAttackMap();

    int[][] getFigureTrajectory(Position pos, FigureName figureName, FigureColor figureColor);

    ChessFigure createChessFigure(Position pos, FigureName figureName, FigureColor figureColor);
    void clearGameBoard(String fileName);


    void addNewFigure(ChessFigure chessFigure, String fileName);

    List<ChessFigure> getAvailableFigures();


    void removeFigure(Position position, String fileName);

    boolean saveGameResults();
}
