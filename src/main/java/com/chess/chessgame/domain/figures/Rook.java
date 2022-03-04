package com.chess.chessgame.domain.figures;

import com.chess.chessgame.enums.FigureColor;
import com.chess.chessgame.enums.FigureName;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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

    public int[][] removeDuplicates(int[][] matrix) {
//        for (int i = 0; i < this.getPosition().getxPosition(); i++) {
//            if (matrix[i][this.getPosition().getyPosition()] == 9) {
//                if (this.getPosition().getyPosition() != 0) {
//                    matrix[i - 1][this.getPosition().getyPosition()] = 0;
//                }
//            }
//        }
//        for (int i = 8; i > this.getPosition().getxPosition(); i--) {
//            if (matrix[i-1][this.getPosition().getyPosition()] == 9) {
//                if (i != this.getPosition().getyPosition()) {
//                    matrix[i - 1][this.getPosition().getyPosition()] = 0;
//                }
//            }
//        }
        int[] horizontal = matrix[this.getPosition().getxPosition()];
        int[] vertical = new int[8];
        for (int i = 0; i < 8; i++) {
            vertical[i] = matrix[i][this.getPosition().getyPosition()];
        }

        boolean isMatch = false;
        for (int i = this.getPosition().getyPosition()+1; i < 8; i++) {
            if (vertical[i] == 9 && !isMatch) {
                isMatch = true;
            } else {
                vertical[i] = 0;
            }
        }
        isMatch = false;
        for (int i = this.getPosition().getyPosition()-1; i >= 0; i--) {
            if (vertical[i] == 9 && !isMatch) {
                isMatch = true;
            } else {
                vertical[i] = 0;
            }
        }

        isMatch = false;
        for (int i = this.getPosition().getxPosition()+1; i < 8; i++) {
            if (vertical[i] == 9 && !isMatch) {
                isMatch = true;
            } else {
                vertical[i] = 0;
            }
        }
        isMatch = false;
        for (int i = this.getPosition().getxPosition()-1; i >= 0; i--) {
            if (vertical[i] == 9 && !isMatch) {
                isMatch = true;
            } else {
                vertical[i] = 0;
            }
        }

        matrix[this.getPosition().getxPosition()][this.getPosition().getyPosition()] = 4;
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
