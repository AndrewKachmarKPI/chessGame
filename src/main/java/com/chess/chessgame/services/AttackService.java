package com.chess.chessgame.services;

import com.chess.chessgame.domain.figures.ChessFigure;
import com.chess.chessgame.domain.figures.Position;


public interface AttackService {
    int[] getVerticalSplice(int[][] matrix, int position);
    int[][] setVerticalSplice(int[][] matrix, int[] splice, int position);
    int[] processSpliceMultiDirection(int[] figureArray, int[] gameArray, int position);
    int[] processSpliceDiagonal(int[] figureArray, int[] gameArray, int figureNumber);
    void setDiagonalSplice(int[][] matrix, int[] splice, boolean isMain, Position position);
    int[] getDiagonalSplice(int[][] matrix, boolean isMain, Position position);
    int getPosOfElement(int[] array, int figureNumber);
    int[] addElementToArray(int[] array, int element);
    int[][] setAttackSimple(int[][] matrix, int[][] gameMatrix);
    void removeTrailing(ChessFigure figure, ChessFigure chessFigure, int[][] matrix, int[][] figureMatrix);
    void removeAxis(ChessFigure figure, ChessFigure chessFigure, int[][] matrix, int[][] figureMatrix);
    void removeDiagonal(ChessFigure figure, ChessFigure chessFigure, int[][] matrix, int[][] figureMatrix);
    boolean isDiagonal(Position position, int i, int j, boolean mode);
    boolean isNeighbourX(Position position, int x);
    boolean isNeighbourY(Position position, int y);
}
