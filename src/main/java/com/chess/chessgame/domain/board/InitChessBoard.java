package com.chess.chessgame.domain.board;

import com.chess.chessgame.domain.figures.Position;
import com.chess.chessgame.enums.FigureColor;
import com.chess.chessgame.enums.FigureName;

public class InitChessBoard {
    private FigureColor figureColor;
    private FigureName figureName;
    private Position position;

    public InitChessBoard(FigureColor figureColor, FigureName figureName, Position position) {
        this.figureColor = figureColor;
        this.figureName = figureName;
        this.position = position;
    }

    public FigureColor getFigureColor() {
        return figureColor;
    }

    public void setFigureColor(FigureColor figureColor) {
        this.figureColor = figureColor;
    }

    public FigureName getFigureName() {
        return figureName;
    }

    public void setFigureName(FigureName figureName) {
        this.figureName = figureName;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
