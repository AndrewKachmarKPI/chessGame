package com.chess.chessgame.domain.figures;

import com.chess.chessgame.enums.FigureColor;
import com.chess.chessgame.enums.FigureName;

public class King extends ChessFigure {

    public King(FigureName name, FigureColor color, Position position) {
        super(name, color, position);
    }

    @Override
    public int[][] getMoveDirection(int[][] gameMatrix) {
        int[][] matrix = new int[8][8];
        for (int i = 0; i < 8; i++) {
            if (i == this.getPosition().getyPosition() - 1 || i == this.getPosition().getyPosition() + 1) {
                matrix[this.getPosition().getxPosition()][i] = 1;
            }
            if (i == this.getPosition().getxPosition() - 1 || i == this.getPosition().getxPosition() + 1) {
                matrix[i][this.getPosition().getyPosition()] = 1;
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (getAttackService().isDiagonal(this.getPosition(), i, j, true) && getAttackService().isNeighbourY(this.getPosition(), j) &&
                        getAttackService().isNeighbourX(this.getPosition(), i)) {
                    matrix[i][j] = 1;
                }
                if (getAttackService().isDiagonal(this.getPosition(), i, j, false) && getAttackService().isNeighbourY(this.getPosition(), j) &&
                        getAttackService().isNeighbourX(this.getPosition(), i)) {
                    matrix[i][j] = 1;
                }
            }
        }
        matrix[this.getPosition().getxPosition()][this.getPosition().getyPosition()] = 2;
        return removeDuplicates(matrix, gameMatrix);
    }

    @Override
    public int[][] removeDuplicates(int[][] matrix, int[][] gameMatrix) {
        return getAttackService().setAttackSimple(matrix, gameMatrix);
    }
}
