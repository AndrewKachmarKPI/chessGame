package com.chess.chessgame.domain.figures;

import com.chess.chessgame.enums.FigureColor;
import com.chess.chessgame.enums.FigureName;

public class King extends ChessFigure{
    public King() {
    }

    public King(FigureName name, FigureColor color, Position position) {
        super(name, color, position);
    }
}
