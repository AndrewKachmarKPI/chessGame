package com.chess.chessgame.domain.board;

import java.util.ArrayList;
import java.util.List;

public class GameField {
    private List<ChessCell> selectedCells;
    private ChessCell chessCell;
    private boolean isGameStarted;

    public GameField() {
        this.selectedCells = new ArrayList<>();
        this.chessCell = new ChessCell();
        this.isGameStarted = false;
    }

    public GameField(List<ChessCell> selectedCells, ChessCell chessCell) {
        this.selectedCells = selectedCells;
        this.chessCell = chessCell;
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        isGameStarted = gameStarted;
    }

    public List<ChessCell> getSelectedCells() {
        return selectedCells;
    }

    public void setSelectedCells(List<ChessCell> selectedCells) {
        this.selectedCells = selectedCells;
    }

    public ChessCell getChessCell() {
        return chessCell;
    }

    public void setChessCell(ChessCell chessCell) {
        this.chessCell = chessCell;
    }
}
