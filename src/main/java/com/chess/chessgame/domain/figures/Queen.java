package com.chess.chessgame.domain.figures;

import com.chess.chessgame.enums.FigureColor;
import com.chess.chessgame.enums.FigureName;

public class Queen extends ChessFigure{
    public Queen() {
    }

    public Queen(FigureName name, FigureColor color, Position position) {
        super(name, color, position);
    }
}
