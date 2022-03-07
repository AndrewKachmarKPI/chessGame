package com.chess.chessgame.domain.figures;

import com.chess.chessgame.enums.FigureColor;
import com.chess.chessgame.enums.FigureName;

public class Rook extends ChessFigure {
    public Rook() {
    }

    public Rook(ChessFigure chessFigure) {
        super(chessFigure.getName(), chessFigure.getColor(), chessFigure.getPosition());
    }

    public Rook(FigureName name, FigureColor color, Position position) {
        super(name, color, position);
    }

    public int[][] getMoveDirection(int[][] gameMatrix) {
        int[][] matrix = new int[8][8];
        for (int i = 0; i < 8; i++) {
            matrix[this.getPosition().getxPosition()][i] = 1;
        }
        for (int i = 0; i < 8; i++) {
            matrix[i][this.getPosition().getyPosition()] = 1;
        }
        matrix[this.getPosition().getxPosition()][this.getPosition().getyPosition()] = 4;
        return removeDuplicates(matrix, gameMatrix);
    }

    public int[][] removeDuplicates(int[][] matrix, int[][] gameMatrix) {
        int[] horizontalFigureSplice = matrix[this.getPosition().getxPosition()];
        int[] horizontalGameSplice = gameMatrix[this.getPosition().getxPosition()];
        int[] verticalFigureSplice = getVerticalSplice(matrix);
        int[] verticalGameSplice = getVerticalSplice(gameMatrix);

        matrix[this.getPosition().getxPosition()] = processSpliceHorizontal(horizontalFigureSplice, horizontalGameSplice);
        matrix = setVerticalSplice(matrix, processSpliceVertical(verticalFigureSplice, verticalGameSplice));
        return matrix;
    }

    public int[] processSpliceHorizontal(int[] figureArray, int[] gameArray) {
        boolean isFound = false;
        for (int i = this.getPosition().getyPosition() + 1; i < 8; i++) {
            if (gameArray[i] > 1 && !isFound) {
                figureArray[i] = 10;
                isFound = true;
            } else {
                if (isFound) {
                    figureArray[i] = 0;
                }
            }
        }
        isFound = false;
        for (int i = this.getPosition().getyPosition() - 1; i + 1 > 0; i--) {
            if (gameArray[i] > 1 && !isFound) {
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

    public int[] processSpliceVertical(int[] figureArray, int[] gameArray) {
        boolean isFound = false;
        for (int i = this.getPosition().getxPosition() + 1; i < 8; i++) {
            if (gameArray[i] > 1 && !isFound) {
                figureArray[i] = 10;
                isFound = true;
            } else {
                if (isFound) {
                    figureArray[i] = 0;
                }
            }
        }
        isFound = false;
        for (int i = this.getPosition().getxPosition() - 1; i + 1 > 0; i--) {
            if (gameArray[i] > 1 && !isFound) {
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

    public int[] getVerticalSplice(int[][] matrix) {
        int[] verticalSplice = new int[8];
        for (int i = 0; i < 8; i++) {
            verticalSplice[i] = matrix[i][this.getPosition().getyPosition()];
        }
        return verticalSplice;
    }

    public int[][] setVerticalSplice(int[][] matrix, int[] splice) {
        for (int i = 0; i < 8; i++) {
            matrix[i][this.getPosition().getyPosition()] = splice[i];
        }
        return matrix;
    }
}
