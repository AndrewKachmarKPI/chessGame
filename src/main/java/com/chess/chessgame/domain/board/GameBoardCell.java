package com.chess.chessgame.domain.board;


import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GameBoardCell {
    private Color color;
    private Rectangle rectangle;
    private boolean isInFocus;

    public GameBoardCell(Color color, Rectangle rectangle) {
        this.color = color;
        this.rectangle = rectangle;
    }
    public GameBoardCell() {
        isInFocus = false;
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
