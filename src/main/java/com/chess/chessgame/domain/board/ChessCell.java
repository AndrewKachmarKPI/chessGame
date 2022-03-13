package com.chess.chessgame.domain.board;


import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ChessCell {
    private Color color;
    private Rectangle rectangle;
    private boolean isInFocus;


    public ChessCell(Color color, Rectangle rectangle) {
        this.color = color;
        this.rectangle = rectangle;
    }

    public ChessCell() {
        isInFocus = false;
    }

    public ChessCell(Color color, Rectangle rectangle, boolean isInFocus) {
        this.color = color;
        this.rectangle = rectangle;
        this.isInFocus = isInFocus;
    }

    public boolean isInFocus() {
        return isInFocus;
    }

    public void setInFocus(boolean inFocus) {
        isInFocus = inFocus;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }
}
