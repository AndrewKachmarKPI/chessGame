package com.chess.chessgame.domain.figures;

import com.chess.chessgame.enums.FigureColor;
import com.chess.chessgame.enums.FigureName;

public class King extends ChessFigure {
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
        //FIXME ADD DIAGONAL
        matrix[this.getPosition().getxPosition()][this.getPosition().getyPosition()] = 2;
        return matrix;
    }

    public boolean isNeighbourX(int x) {
        return this.getPosition().getxPosition() + 1 == x || this.getPosition().getxPosition() - 1 == x;
    }
    public boolean isNeighbourY(int y) {
        return this.getPosition().getyPosition() + 1 == y || this.getPosition().getxPosition() - 1 == y;
    }
}
