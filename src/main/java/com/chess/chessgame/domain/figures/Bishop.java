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

    public int[][] getMoveDirection() {
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
        return matrix;
    }
}
