package com.chess.chessgame.domain.figures;

import com.chess.chessgame.enums.FigureColor;
import com.chess.chessgame.enums.FigureName;

public class Rook extends ChessFigure{
    public Rook() {
    }

    public Rook(FigureName name, FigureColor color, Position position) {
        super(name, color, position);
    }

}
