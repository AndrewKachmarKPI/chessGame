package com.chess.chessgame.domain.board;

import javafx.scene.text.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GameBoard {
    private List<GameBoardCell> selectedCells;
    private GameBoardCell gameBoardCell;
    private boolean isGameStarted;
    private String workingFileDirectory;
    private final Text workingFileName;
    private final Text workingFileContent;

    public GameBoard() {
        this.selectedCells = new ArrayList<>();
        this.gameBoardCell = new GameBoardCell();
        this.isGameStarted = false;
        this.workingFileName = new Text();
        this.workingFileContent = new Text();
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

    public void setGameBoardCell(GameBoardCell gameBoardCell) {
        this.gameBoardCell = gameBoardCell;
    }

    public Text getWorkingFileName() {
        return workingFileName;
    }

    public Text getWorkingFileContent() {
        return workingFileContent;
    }

    public String getWorkingFileDirectory() {
        return workingFileDirectory;
    }

    public void setWorkingFileDirectory(String workingFileDirectory) {
        this.workingFileDirectory = workingFileDirectory;
    }
}
