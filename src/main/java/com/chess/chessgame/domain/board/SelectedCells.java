package com.chess.chessgame.domain.board;

import com.chess.chessgame.domain.figures.Position;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SelectedCells {
    private Integer id;
    private Color color;
    private Rectangle rectangle;

    public SelectedCells() {

    }

    public SelectedCells(Color color, Rectangle rectangle) {
        this.color = color;
        this.rectangle = rectangle;
    }

    @Override
    public String toString() {
        return "SelectedCells{" +
                "id=" + id +
                ", color=" + color +
                ", rectangle=" + rectangle +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
