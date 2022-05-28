package com.chess.chessgame.domain.board;

import com.chess.chessgame.domain.figures.ChessFigure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChessBoard {
    private List<ChessFigure> figures;
    private Map<ChessFigure, List<ChessFigure>> chessFigureMap = new HashMap<>();
    private int[][] chessMatrix;

    public ChessBoard() {
    }

    public ChessBoard(List<ChessFigure> figures, Map<ChessFigure, List<ChessFigure>> chessFigureMap, int[][] chessMatrix) {
        this.figures = figures;
        this.chessFigureMap = chessFigureMap;
        this.chessMatrix = chessMatrix;
    }

    public Map<ChessFigure, List<ChessFigure>> getChessFigureMap() {
        return chessFigureMap;
    }

    public void setChessFigureMap(Map<ChessFigure, List<ChessFigure>> chessFigureMap) {
        this.chessFigureMap = chessFigureMap;
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
