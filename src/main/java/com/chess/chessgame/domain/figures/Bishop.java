package com.chess.chessgame.domain.figures;

import com.chess.chessgame.enums.FigureColor;
import com.chess.chessgame.enums.FigureName;

public class Bishop extends ChessFigure {

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
        int[] mainDiagonalFigure = attackService.getDiagonalSplice(matrix, true, this.getPosition());
        int[] secondDiagonalFigure = attackService.getDiagonalSplice(matrix, false, this.getPosition());
        int[] mainDiagonalGame = attackService.getDiagonalSplice(gameMatrix, true, this.getPosition());
        int[] secondDiagonalGame = attackService.getDiagonalSplice(gameMatrix, false, this.getPosition());

        attackService.setDiagonalSplice(matrix, attackService.processSpliceDiagonal(mainDiagonalFigure, mainDiagonalGame, 5), true, this.getPosition());
        attackService.setDiagonalSplice(matrix, attackService.processSpliceDiagonal(secondDiagonalFigure, secondDiagonalGame, 5), false, this.getPosition());
        return matrix;
    }
}
