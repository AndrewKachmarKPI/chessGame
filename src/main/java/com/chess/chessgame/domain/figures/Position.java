package com.chess.chessgame.domain.figures;

import java.util.Objects;

public class Position {
    private int xPosition;
    private int yPosition;

    public Position() {
    }
    public Position(int xPosition, int yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }
    public int getxPosition() {
        return xPosition;
    }
    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }
    public int getyPosition() {
        return yPosition;
    }
    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }
    @Override
    public String toString() {
        return "Position{" +
                "xPosition=" + xPosition +
                ", yPosition=" + yPosition +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return xPosition == position.xPosition && yPosition == position.yPosition;
    }
    @Override
    public int hashCode() {
        return Objects.hash(xPosition, yPosition);
    }
}
