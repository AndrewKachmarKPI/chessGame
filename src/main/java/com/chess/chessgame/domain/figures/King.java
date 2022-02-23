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
            if(this.getPosition().getxPosition()-1==i ||this.getPosition().getxPosition()+1==i){
                matrix[this.getPosition().getxPosition()][i] = 1;
            }
        }
        for (int i = 0; i < 8; i++) {
            if(this.getPosition().getyPosition()-1==i ||this.getPosition().getyPosition()+1==i) {
                matrix[i][this.getPosition().getyPosition()] = 1;
            }
        }//FIXME NOT TRUTH
        matrix[this.getPosition().getxPosition()][this.getPosition().getyPosition()] = 2;
        return matrix;
    }
}
