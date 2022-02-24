package com.chess.chessgame.domain.board;

import com.chess.chessgame.domain.figures.Position;
import javafx.scene.paint.Color;

public class SelectedCells {
    private Integer id;
    private Color color;
    private Position position;

    public SelectedCells() {

    }

    public SelectedCells(Integer id, Position position) {
        this.id = id;
        this.position = position;
    }

    public SelectedCells(Color color, Position position) {
        this.color = color;
        this.position = position;
    }

    @Override
    public String toString() {
        return "SelectedCells{" +
                "id=" + id +
                ", color=" + color +
                ", position=" + position +
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

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
