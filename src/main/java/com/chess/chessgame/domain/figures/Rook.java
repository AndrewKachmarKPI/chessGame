package com.chess.chessgame.domain.figures;

import com.chess.chessgame.enums.FigureColor;
import com.chess.chessgame.enums.FigureName;

import java.util.ArrayList;
import java.util.List;

public class Rook extends ChessFigure {
    public Rook() {
    }

    public Rook(ChessFigure chessFigure) {
        super(chessFigure.getName(), chessFigure.getColor(), chessFigure.getPosition());
    }

    public Rook(FigureName name, FigureColor color, Position position) {
        super(name, color, position);
    }

    public int[][] getMoveDirection() {
        int[][] matrix = new int[8][8];
        for (int i = 0; i < 8; i++) {
            matrix[this.getPosition().getxPosition()][i] = 1;
        }
        for (int i = 0; i < 8; i++) {
            matrix[i][this.getPosition().getyPosition()] = 1;
        }
        matrix[this.getPosition().getxPosition()][this.getPosition().getyPosition()] = 4;
        return matrix;
    }

    public int[][] removeDuplicates(int[][] matrix, int[][] gameMatrix) {
        List<Position> positionsForRemove = new ArrayList<>();
        List<Position> horizontalPositions = new ArrayList<>();
        List<Position> verticalPositions = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            if (gameMatrix[this.getPosition().getxPosition()][i] > 1) {
                verticalPositions.add(new Position(i, this.getPosition().getxPosition()));
            }
        }
        for (int i = 0; i < 8; i++) {
            if (gameMatrix[i][this.getPosition().getyPosition()] > 1) {
                horizontalPositions.add(new Position(this.getPosition().getyPosition(), i));
            }
        }
        return matrix;
    }

    public boolean isPositionMatch(Position position) {
        int[][] possibleMoves = getMoveDirection();
        boolean isMatch = false;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                isMatch = possibleMoves[i][j] == 1 && position.getxPosition() == j && position.getyPosition() == i;
            }
        }
        return isMatch;
    }

}
