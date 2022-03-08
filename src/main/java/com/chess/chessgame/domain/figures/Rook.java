package com.chess.chessgame.domain.figures;

import com.chess.chessgame.enums.FigureColor;
import com.chess.chessgame.enums.FigureName;
import com.chess.chessgame.serviceImpl.AttackServiceImpl;
import com.chess.chessgame.services.AttackService;

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

    @Override
    public int[][] removeDuplicates(int[][] matrix, int[][] gameMatrix) {
        AttackService attackService = new AttackServiceImpl();
        int[] horizontalFigureSplice = matrix[this.getPosition().getxPosition()];
        int[] horizontalGameSplice = gameMatrix[this.getPosition().getxPosition()];
        int[] verticalFigureSplice = attackService.getVerticalSplice(matrix, this.getPosition().getyPosition());
        int[] verticalGameSplice = attackService.getVerticalSplice(gameMatrix, this.getPosition().getyPosition());

        matrix[this.getPosition().getxPosition()] = attackService.processSpliceHorizontal(horizontalFigureSplice, horizontalGameSplice, this.getPosition().getyPosition());
        matrix = attackService.setVerticalSplice(matrix, attackService.processSpliceVertical(verticalFigureSplice,
                verticalGameSplice, this.getPosition().getxPosition()), this.getPosition().getyPosition());
        return matrix;
    }
}
