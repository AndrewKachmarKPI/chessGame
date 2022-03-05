package com.chess.chessgame.domain.figures;

import com.chess.chessgame.enums.FigureColor;
import com.chess.chessgame.enums.FigureName;

public class King extends ChessFigure {
    public King(ChessFigure chessFigure) {
        super(chessFigure.getName(), chessFigure.getColor(), chessFigure.getPosition());
    }

    public King() {
    }

    public King(FigureName name, FigureColor color, Position position) {
        super(name, color, position);
    }

    public int[][] getMoveDirection() {
        int[][] matrix = new int[8][8];
        for (int i = 0; i < 8; i++) {
            if (i == this.getPosition().getyPosition() - 1 || i == this.getPosition().getyPosition() + 1) {
                matrix[this.getPosition().getxPosition()][i] = 1;
            }
        }
        for (int i = 0; i < 8; i++) {
            if (i == this.getPosition().getxPosition() - 1 || i == this.getPosition().getxPosition() + 1) {
                matrix[i][this.getPosition().getyPosition()] = 1;
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isDiagonal(i,j,true) && isNeighbourY(j) && isNeighbourX(i)) {
                    matrix[i][j] = 1;
                }
            }
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isDiagonal(i,j,false)&& isNeighbourY(j) && isNeighbourX(i)) {
                    matrix[i][j] = 1;
                }
            }
        }
        //FIXME ADD DIAGONAL
        matrix[this.getPosition().getxPosition()][this.getPosition().getyPosition()] = 2;
        return matrix;
    }

    public boolean isDiagonal(int i, int j, boolean mode) {
        if (mode) {
            return i + this.getPosition().getyPosition() == j + this.getPosition().getxPosition();
        } else {
            return i + (8 - this.getPosition().getyPosition()) + j - this.getPosition().getxPosition() == 8;
        }
    }

    public boolean isNeighbourX(int x) {
        return Math.abs(this.getPosition().getxPosition() -x)==1;
    }

    public boolean isNeighbourY(int y) {
        return  Math.abs(this.getPosition().getyPosition()-y )==1;
    }
}
