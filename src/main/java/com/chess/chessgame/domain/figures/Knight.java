package com.chess.chessgame.domain.figures;

import com.chess.chessgame.enums.FigureColor;
import com.chess.chessgame.enums.FigureName;

public class Knight extends ChessFigure {
    public Knight(ChessFigure chessFigure) {
        super(chessFigure.getName(), chessFigure.getColor(), chessFigure.getPosition());
    }

    public Knight() {
    }

    public Knight(FigureName name, FigureColor color, Position position) {
        super(name, color, position);
    }

    public int[][] getMoveDirection(int[][] gameMatrix) {
        int[][] matrix = new int[8][8];
        for (int i = 0; i < 8; i++) {
            if (i == this.getPosition().getyPosition() - 2 || i == this.getPosition().getyPosition() + 2) {
                if (this.getPosition().getxPosition() - 1 >= 0) {
                    matrix[this.getPosition().getxPosition() - 1][i] = 1;
                } else {
                    matrix[this.getPosition().getxPosition()][i] = 0;
                }
            }
            if (i == this.getPosition().getxPosition() - 2 || i == this.getPosition().getxPosition() + 2) {
                if (this.getPosition().getyPosition() - 1 >= 0) {
                    matrix[i][this.getPosition().getyPosition() - 1] = 1;
                } else {
                    matrix[i][this.getPosition().getyPosition()] = 0;
                }
            }
        }

        for (int i = 0; i < 8; i++) {
            if (i == this.getPosition().getyPosition() - 2 || i == this.getPosition().getyPosition() + 2) {
                if (this.getPosition().getxPosition() + 1 <= 7) {
                    matrix[this.getPosition().getxPosition() + 1][i] = 1;
                } else {
                    matrix[this.getPosition().getxPosition()][i] = 0;
                }
            }
            if (i == this.getPosition().getxPosition() - 2 || i == this.getPosition().getxPosition() + 2) {
                if (this.getPosition().getyPosition() + 1 <= 7) {
                    matrix[i][this.getPosition().getyPosition() + 1] = 1;
                } else {
                    matrix[i][this.getPosition().getyPosition()] = 0;
                }
            }
        }
        matrix[this.getPosition().getxPosition()][this.getPosition().getyPosition()] = 6;
        return removeDuplicates(matrix, gameMatrix);
    }

    @Override
    public int[][] removeDuplicates(int[][] matrix, int[][] gameMatrix) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (gameMatrix[i][j] > 1 && matrix[i][j] == 1) {
                    matrix[i][j] = 10;
                }
            }
        }
        return matrix;
    }
}
