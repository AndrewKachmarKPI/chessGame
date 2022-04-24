package com.chess.chessgame.domain.board;

import java.util.ArrayList;
import java.util.List;

public class GameBoard {
    private List<GameBoardCell> selectedCells;
    private GameBoardCell gameBoardCell;
    private boolean isGameStarted;

    public GameBoard() {
        this.selectedCells = new ArrayList<>();
        this.gameBoardCell = new GameBoardCell();
        this.isGameStarted = false;
    }

    public GameBoard(List<GameBoardCell> selectedCells, GameBoardCell gameBoardCell) {
        this.selectedCells = selectedCells;
        this.gameBoardCell = gameBoardCell;
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        isGameStarted = gameStarted;
    }

    public List<GameBoardCell> getSelectedCells() {
        return selectedCells;
    }

    public void setSelectedCells(List<GameBoardCell> selectedCells) {
        this.selectedCells = selectedCells;
    }

    public GameBoardCell getChessCell() {
        return gameBoardCell;
    }

    public void setChessCell(GameBoardCell gameBoardCell) {
        this.gameBoardCell = gameBoardCell;
    }
}
