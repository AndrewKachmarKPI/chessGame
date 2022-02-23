package com.chess.chessgame.domain.figures;

import com.chess.chessgame.enums.FigureColor;
import com.chess.chessgame.enums.FigureName;

public class Bishop extends ChessFigure {
    public Bishop() {
    }

    public Bishop(FigureName name, FigureColor color, Position position) {
        super(name, color, position);
    }
}
