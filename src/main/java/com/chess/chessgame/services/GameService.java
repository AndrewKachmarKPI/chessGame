package com.chess.chessgame.services;

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

    public static void initGame(){
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
            while ((line = bufferedReader.readLine()) != null) {
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
                chessBoard.setChessMatrix(matrix);
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

    private static ChessFigure createChessFigure(InitChessBoard initChessBoard) {
//        switch (initChessBoard.getFigureName()) {
//            case KNIGHT: {
//                return new Knight(initChessBoard.getFigureName(), initChessBoard.getFigureColor(), initChessBoard.getPosition());
//            }
//            case BISHOP: {
//                return new Bishop(initChessBoard.getFigureName(), initChessBoard.getFigureColor(), initChessBoard.getPosition());
//            }
//            case QUEEN: {
//                return new Queen(initChessBoard.getFigureName(), initChessBoard.getFigureColor(), initChessBoard.getPosition());
//            }
//            case ROCK: {
//                return new Rook(initChessBoard.getFigureName(), initChessBoard.getFigureColor(), initChessBoard.getPosition());
//            }
//            case KING: {
//                return new King(initChessBoard.getFigureName(), initChessBoard.getFigureColor(), initChessBoard.getPosition());
//            }
//        }
        return new ChessFigure(initChessBoard.getFigureName(), initChessBoard.getFigureColor(), initChessBoard.getPosition());
    }
}