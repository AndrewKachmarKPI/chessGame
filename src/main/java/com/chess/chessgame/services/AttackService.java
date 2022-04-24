package com.chess.chessgame.services;

import com.chess.chessgame.domain.figures.Position;


public interface AttackService {
    int[] getVerticalSplice(int[][] matrix, int position);

    int[][] setVerticalSplice(int[][] matrix, int[] splice, int position);

    int[] processSpliceHorizontal(int[] figureArray, int[] gameArray, int position);

    int[] processSpliceVertical(int[] figureArray, int[] gameArray, int xPosition);

    int[] processSpliceDiagonal(int[] figureArray, int[] gameArray, int figureNumber);

    void setDiagonalSplice(int[][] matrix, int[] splice, boolean isMain, Position position);

    int[] getDiagonalSplice(int[][] matrix, boolean isMain, Position position);

    int getPosOfElement(int[] array, int figureNumber);

    int[] addElementToArray(int[] array, int element);

    int[][] removeDuplicatesSimple(int[][] matrix, int[][] gameMatrix);
}
