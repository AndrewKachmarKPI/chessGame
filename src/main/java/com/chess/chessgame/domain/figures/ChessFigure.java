package com.chess.chessgame.domain.figures;

import com.chess.chessgame.enums.FigureColor;
import com.chess.chessgame.enums.FigureName;
import com.chess.chessgame.serviceImpl.AttackServiceImpl;
import com.chess.chessgame.services.AttackService;

import java.util.Objects;

public abstract class ChessFigure {
    private FigureName name;
    private FigureColor color;
    private Position position;

    public AttackService attackService;

    public ChessFigure() {
        attackService = new AttackServiceImpl();
    }

    public ChessFigure(FigureName name, FigureColor color) {
        this.name = name;
        this.color = color;
        attackService = new AttackServiceImpl();
    }

    public ChessFigure(FigureName name, FigureColor color, Position position) {
        this.name = name;
        this.color = color;
        this.position = position;
        attackService = new AttackServiceImpl();
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

    public abstract int[][] getMoveDirection(int[][] gameMatrix);

    public abstract int[][] removeDuplicates(int[][] matrix, int[][] gameMatrix);

    @Override
    public String toString() {
        return "ChessFigure{" +
                ", name=" + name +
                ", color=" + color +
                ", position=" + position +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessFigure that = (ChessFigure) o;
        return name == that.name && color == that.color && Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color, position);
    }
}
