package com.chess.chessgame.domain.board;

import com.chess.chessgame.domain.figures.ChessFigure;
import com.chess.chessgame.domain.figures.Position;
import javafx.scene.paint.Color;

public class BoardCell {
    private Position position;
    private ChessFigure chessFigure;
    private Color color;
    private boolean isHit;

    public BoardCell(Position position, ChessFigure chessFigure, Color color, boolean isHit) {
        this.position = position;
        this.chessFigure = chessFigure;
        this.color = color;
        this.isHit = isHit;
    }

    public BoardCell(Position position, ChessFigure chessFigure, boolean isHit) {
        this.position = position;
        this.chessFigure = chessFigure;
        this.isHit = isHit;
    }

    public BoardCell(Position position, boolean isHit) {
        this.position = position;
        this.isHit = isHit;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public ChessFigure getChessFigure() {
        return chessFigure;
    }

    public void setChessFigure(ChessFigure chessFigure) {
        this.chessFigure = chessFigure;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isHit() {
        return isHit;
    }

    public void setHit(boolean hit) {
        isHit = hit;
    }
}
