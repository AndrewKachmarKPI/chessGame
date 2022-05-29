package com.chess.chessgame.domain.figures;

import com.chess.chessgame.enums.FigureColor;
import com.chess.chessgame.enums.FigureName;

public class Bishop extends ChessFigure {

    public Bishop(FigureName name, FigureColor color, Position position) {
        super(name, color, position);
    }

    public int[][] getMoveDirection(int[][] gameMatrix) {
        int[][] matrix = fillInitialMatrix();
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
