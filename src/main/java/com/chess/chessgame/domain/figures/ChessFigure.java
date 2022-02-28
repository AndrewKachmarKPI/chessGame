package com.chess.chessgame.domain.figures;

import com.chess.chessgame.enums.FigureColor;
import com.chess.chessgame.enums.FigureName;

import java.util.Objects;

public class ChessFigure {
    private Integer id;
    private FigureName name;
    private FigureColor color;
    private Position position;
    private String image;


    public ChessFigure() {

    }
    public ChessFigure(FigureName name, FigureColor color) {
        this.name = name;
        this.color = color;
    }
    public ChessFigure(FigureName name, FigureColor color, Position position) {
        this.name = name;
        this.color = color;
        this.position = position;
    }

    public FigureName getName() {
        return name;
    }

    public void setName(FigureName name) {
        this.name = name;
    }

    public FigureColor getColor() {
        return color;
    }

    public void setColor(FigureColor color) {
        this.color = color;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ChessFigure{" +
                "id=" + id +
                ", name=" + name +
                ", color=" + color +
                ", position=" + position +
                ", image='" + image + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessFigure that = (ChessFigure) o;
        return Objects.equals(id, that.id) && name == that.name && color == that.color && Objects.equals(position, that.position) && Objects.equals(image, that.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, position, image);
    }
}
