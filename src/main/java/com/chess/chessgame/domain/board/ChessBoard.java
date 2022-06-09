package com.chess.chessgame.domain.board;

import com.chess.chessgame.domain.figures.ChessFigure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChessBoard {
    private List<ChessFigure> figures;
    private Map<ChessFigure, int[][]> figureMatrix = new HashMap<>();
    private Map<ChessFigure, List<ChessFigure>> chessFigureMap = new HashMap<>();
    private int[][] chessMatrix;

    public ChessBoard() {
    }

    public ChessBoard(List<ChessFigure> figures, Map<ChessFigure, List<ChessFigure>> chessFigureMap,
                      Map<ChessFigure, int[][]> figureMatrix, int[][] chessMatrix) {
        this.figures = figures;
        this.chessFigureMap = chessFigureMap;
        this.chessMatrix = chessMatrix;
        this.figureMatrix = figureMatrix;
    }

    public Map<ChessFigure, List<ChessFigure>> getChessFigureMap() {
        return chessFigureMap;
    }

    public List<ChessFigure> getFigures() {
        return figures;
    }

    public int[][] getChessMatrix() {
        return chessMatrix;
    }

    public Map<ChessFigure, int[][]> getFigureMatrix() {
        return figureMatrix;
    }

    public void setFigureMatrix(Map<ChessFigure, int[][]> figureMatrix) {
        this.figureMatrix = figureMatrix;
    }
}
