package com.chess.chessgame.services;

import com.chess.chessgame.domain.board.BoardCell;
import com.chess.chessgame.domain.board.ChessBoard;
import com.chess.chessgame.domain.board.InitChessBoard;
import com.chess.chessgame.domain.figures.*;
import com.chess.chessgame.enums.FigureColor;
import com.chess.chessgame.enums.FigureName;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class GameService {
    public static ChessBoard chessBoard = new ChessBoard();

    public static void initGame() {
        createGame();
        paintFigures();
    }

    private static void createGame() {
        File file = new File("D:\\PROJECTS\\chessGame\\src\\main\\resources\\game\\init.txt");
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            List<ChessFigure> figures = new ArrayList<>();
            int[][] matrix = new int[8][8];
            while ((line = bufferedReader.readLine()) != null && figures.size() <= 10) {
                line = line.trim();
                String color = line.split(" ")[0];
                String name = line.split(" ")[1];
                Position position = new Position(Integer.parseInt(line.split(" ")[2]), Integer.parseInt(line.split(" ")[3]));
                ChessFigure chessFigure = new ChessFigure(FigureName.valueOf(name.toUpperCase(Locale.ROOT)),
                        FigureColor.valueOf(color.toUpperCase(Locale.ROOT)), position);
                figures.add(chessFigure);
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (chessFigure.getPosition().getyPosition() == i && chessFigure.getPosition().getxPosition() == j) {
                            matrix[i][j] = getFigureNumber(chessFigure);
                            break;
                        }
                    }
                }
            }
            chessBoard.setFigures(figures);
            chessBoard.setChessMatrix(matrix);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void paintFigures() {
        chessBoard.getFigures().forEach(GameFieldService::setFigureOnBoard);
    }

    private static int getFigureNumber(ChessFigure chessFigure) {
        switch (chessFigure.getName()) {
            case KING: {
                return 2;
            }
            case QUEEN: {
                return 3;
            }
            case ROOK: {
                return 4;
            }
            case BISHOP: {
                return 5;
            }
            case KNIGHT: {
                return 6;
            }
        }
        return 0;
    }

    public static int[][] getFigureTrajectory(ChessFigure chessFigure) {
        int[][] matrix = new int[8][8];
        switch (chessFigure.getName()) {
            case KING: {
                King king = new King(chessFigure);
                matrix = checkForPassing(king.getMoveDirection());
                break;
            }
            case QUEEN: {
                Queen queen = new Queen(chessFigure);
                matrix = checkForPassing(queen.getMoveDirection());
                break;
            }
            case ROOK: {
                Rook rook = new Rook(chessFigure);
                matrix = checkForPassing(rook.getMoveDirection());
                break;
            }
            case BISHOP: {
                Bishop bishop = new Bishop(chessFigure);
                matrix = checkForPassing(bishop.getMoveDirection());
                break;
            }
            case KNIGHT: {
                Knight knight = new Knight(chessFigure);
                matrix = checkForPassing(knight.getMoveDirection());
                break;
            }
        }
        return matrix;
    }

    private static int[][] checkForPassing(int[][] figureMatrix) {
        int[][] finalMatrix = new int[8][8];
        int[][] gameMatrix = chessBoard.getChessMatrix();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                finalMatrix[i][j] = gameMatrix[i][j];
                if (figureMatrix[i][j] == 1) {
                    finalMatrix[i][j] = 1;
                    if (gameMatrix[i][j] > 1) {
                        finalMatrix[i][j] = 10;
                    }
                }
            }
        }
        return finalMatrix;
    }

}
