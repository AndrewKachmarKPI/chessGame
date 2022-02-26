package com.chess.chessgame.domain.board;

import com.chess.chessgame.domain.figures.ChessFigure;

import java.util.List;

public class ChessBoard {
    private List<ChessFigure> figures;
    private List<List<BoardCell>> cellMatrix;
    private int[][] chessMatrix;

    public ChessBoard() {
    }

    public ChessBoard(List<ChessFigure> figures, List<List<BoardCell>> cellMatrix, int[][] chessMatrix) {
        this.figures = figures;
        this.cellMatrix = cellMatrix;
        this.chessMatrix = chessMatrix;
    }

    public ChessBoard(List<ChessFigure> figures, int[][] chessMatrix) {
        this.figures = figures;
        this.chessMatrix = chessMatrix;
    }

    public List<List<BoardCell>> getCellMatrix() {
        return cellMatrix;
    }

    public void setCellMatrix(List<List<BoardCell>> cellMatrix) {
        this.cellMatrix = cellMatrix;
    }

    public List<ChessFigure> getFigures() {
        return figures;
    }

    public void setFigures(List<ChessFigure> figures) {
        this.figures = figures;
    }

    public int[][] getChessMatrix() {
        return chessMatrix;
    }

    public void setChessMatrix(int[][] chessMatrix) {
        this.chessMatrix = chessMatrix;
    }
}
