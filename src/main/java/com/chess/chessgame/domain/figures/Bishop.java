package com.chess.chessgame.domain.figures;

import com.chess.chessgame.enums.FigureColor;
import com.chess.chessgame.enums.FigureName;

public class Bishop extends ChessFigure {
    public Bishop(ChessFigure chessFigure) {
        super(chessFigure.getName(), chessFigure.getColor(), chessFigure.getPosition());
    }

    public Bishop(FigureName name, FigureColor color, Position position) {
        super(name, color, position);
    }

    public int[][] getMoveDirection(int[][] gameMatrix) {
        int[][] matrix = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i + this.getPosition().getyPosition() == j + this.getPosition().getxPosition()) {
                    matrix[i][j] = 1;
                }
                if (i + (8 - this.getPosition().getyPosition()) + j - this.getPosition().getxPosition() == 8) {
                    matrix[i][j] = 1;
                }
            }
        }
        matrix[this.getPosition().getxPosition()][this.getPosition().getyPosition()] = 5;
        return removeDuplicates(matrix, gameMatrix);
    }

    @Override
    public int[][] removeDuplicates(int[][] matrix, int[][] gameMatrix) {
        int[] mainDiagonalFigure = getDiagonalSplice(matrix, true);
        int[] secondDiagonalFigure = getDiagonalSplice(matrix, false);
        int[] mainDiagonalGame = getDiagonalSplice(gameMatrix, true);
        int[] secondDiagonalGame = getDiagonalSplice(gameMatrix, false);

        setDiagonalSplice(matrix, processSpliceDiagonal(mainDiagonalFigure, mainDiagonalGame), true);
        setDiagonalSplice(matrix, processSpliceDiagonal(secondDiagonalFigure, secondDiagonalGame), false);
        return matrix;
    }

    public int[] processSpliceDiagonal(int[] figureArray, int[] gameArray) {
        boolean isFound = false;
        for (int i = getPosOfElement(figureArray) + 1; i < figureArray.length; i++) {
            if (gameArray.length > 1 && gameArray[i] > 1 && !isFound) {
                figureArray[i] = 10;
                isFound = true;
            } else {
                if (isFound) {
                    figureArray[i] = 0;
                }
            }
        }
        isFound = false;
        for (int i = getPosOfElement(figureArray) - 1; i+1 >0; i--) {
            if (gameArray.length > 1 && gameArray[i] > 1 && !isFound) {
                figureArray[i] = 10;
                isFound = true;
            } else {
                if (isFound) {
                    figureArray[i] = 0;
                }
            }
        }
        return figureArray;
    }

    public int[][] setDiagonalSplice(int[][] matrix, int[] splice, boolean isMain) {
        int k = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isMain) {
                    if ((i + this.getPosition().getyPosition() == j + this.getPosition().getxPosition()) && k < splice.length) {
                        matrix[i][j] = splice[k];
                        k++;
                    }
                } else {
                    if ((i + (8 - this.getPosition().getyPosition()) + j - this.getPosition().getxPosition() == 8) && k < splice.length) {
                        matrix[i][j] = splice[k];
                        k++;
                    }
                }
            }
        }
        return matrix;
    }

    public int[] getDiagonalSplice(int[][] matrix, boolean isMain) {
        int[] splice = {};

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isMain) {
                    if (i + this.getPosition().getyPosition() == j + this.getPosition().getxPosition()) {
                        splice = addElementToArray(splice, matrix[i][j]);
                    }
                } else {
                    if (i + (8 - this.getPosition().getyPosition()) + j - this.getPosition().getxPosition() == 8) {
                        splice = addElementToArray(splice, matrix[i][j]);
                    }
                }
            }
        }
        return splice;
    }

    public int getPosOfElement(int[] array) {
        int pos = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == 5) {
                pos = i;
            }
        }
        return pos;
    }

    public int[] addElementToArray(int[] array, int element) {
        int[] newArray = new int[array.length + 1];
        System.arraycopy(array, 0, newArray, 0, array.length);
        newArray[array.length] = element;
        return newArray;
    }
}
