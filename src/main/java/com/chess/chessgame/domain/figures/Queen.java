package com.chess.chessgame.domain.figures;

import com.chess.chessgame.enums.FigureColor;
import com.chess.chessgame.enums.FigureName;
import com.chess.chessgame.serviceImpl.AttackServiceImpl;
import com.chess.chessgame.services.AttackService;

public class Queen extends ChessFigure {

    public Queen(FigureName name, FigureColor color, Position position) {
        super(name, color, position);
    }

    @Override
    public int[][] getMoveDirection(int[][] gameMatrix) {
        int[][] matrix = fillInitialMatrix();
        for (int i = 0; i < 8; i++) {
            matrix[this.getPosition().getxPosition()][i] = 1;
        }
        for (int i = 0; i < 8; i++) {
            matrix[i][this.getPosition().getyPosition()] = 1;
        }
        matrix[this.getPosition().getxPosition()][this.getPosition().getyPosition()] = 3;
        return removeDuplicates(matrix, gameMatrix);
    }

    @Override
    public int[][] removeDuplicates(int[][] matrix, int[][] gameMatrix) {
        int[] horizontalFigureSplice = matrix[this.getPosition().getxPosition()];
        int[] horizontalGameSplice = gameMatrix[this.getPosition().getxPosition()];
        int[] verticalFigureSplice = attackService.getVerticalSplice(matrix,this.getPosition().getyPosition());
        int[] verticalGameSplice = attackService.getVerticalSplice(gameMatrix,this.getPosition().getyPosition());

        int[] mainDiagonalFigure = attackService.getDiagonalSplice(matrix, true, this.getPosition());
        int[] secondDiagonalFigure = attackService.getDiagonalSplice(matrix, false, this.getPosition());
        int[] mainDiagonalGame = attackService.getDiagonalSplice(gameMatrix, true, this.getPosition());
        int[] secondDiagonalGame = attackService.getDiagonalSplice(gameMatrix, false, this.getPosition());

        matrix[this.getPosition().getxPosition()] = attackService.processSpliceMultiDirection(horizontalFigureSplice,
                horizontalGameSplice,this.getPosition().getyPosition());
        matrix = attackService.setVerticalSplice(matrix, attackService.processSpliceMultiDirection(verticalFigureSplice,
                verticalGameSplice,this.getPosition().getxPosition()),this.getPosition().getyPosition());



        attackService.setDiagonalSplice(matrix, attackService.processSpliceDiagonal(mainDiagonalFigure, mainDiagonalGame, 3), true, this.getPosition());
        attackService.setDiagonalSplice(matrix, attackService.processSpliceDiagonal(secondDiagonalFigure, secondDiagonalGame, 3), false, this.getPosition());

        return matrix;
    }
}
