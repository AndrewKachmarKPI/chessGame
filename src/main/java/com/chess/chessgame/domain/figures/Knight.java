package com.chess.chessgame.domain.figures;

import com.chess.chessgame.enums.FigureColor;
import com.chess.chessgame.enums.FigureName;

public class Knight extends ChessFigure{
    public Knight() {
    }

    public Knight(FigureName name, FigureColor color, Position position) {
        super(name, color, position);
    }
}
