package com.chess.chessgame.services;

import com.chess.chessgame.domain.board.ChessBoard;
import com.chess.chessgame.domain.figures.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static com.chess.chessgame.services.GameFileService.*;


public class GameService {
    public static ChessBoard chessBoard = new ChessBoard();

    public static void initGame() {
        loadFigures();
        fillFigureMap();
        loadFiguresOnBoard();
    }

    private static void loadFigures() {
        List<ChessFigure> figures = getFiguresFromInitFile();
        int[][] matrix = new int[8][8];
        Map<ChessFigure, List<ChessFigure>> chessFigureMap = new HashMap<>();
        figures.forEach(chessFigure -> {
            chessFigureMap.put(chessFigure, new ArrayList<>());
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (chessFigure.getPosition().getyPosition() == i && chessFigure.getPosition().getxPosition() == j) {
                        matrix[i][j] = getFigureNumber(chessFigure);
                        break;
                    }
                }
            }
        });
        chessBoard.setChessFigureMap(chessFigureMap);
        chessBoard.setFigures(figures);
        chessBoard.setChessMatrix(matrix);
    }

    private static void fillFigureMap() {
        List<ChessFigure> chessFigures = chessBoard.getFigures();
        Map<ChessFigure, List<ChessFigure>> chessFigureMap = chessBoard.getChessFigureMap();

        chessFigures.forEach(chessFigure -> {
            List<ChessFigure> passedFigures = getAttackedFigures(chessFigure);
            chessFigureMap.put(chessFigure, passedFigures);
        });
    }

    private static List<ChessFigure> getAttackedFigures(ChessFigure chessFigure) {
        List<Position> figurePositions = convertMatrixToPositionList(getFigureTrajectory(chessFigure));
        List<Position> matchedPositions = new ArrayList<>();
        List<Position> boardFiguresPositions = chessBoard.getFigures().stream().map(ChessFigure::getPosition).collect(Collectors.toList());

        figurePositions.forEach(position -> {
            boardFiguresPositions.forEach(boardPosition -> {
                if (position.equals(boardPosition)) {
                    matchedPositions.add(boardPosition);
                }
            });
        });

        List<ChessFigure> matchedFigures = new ArrayList<>();
        matchedPositions.forEach(position -> {
            chessBoard.getFigures().forEach(figure -> {
                if (position.equals(figure.getPosition()) && !figure.getColor().equals(chessFigure.getColor())) {
                    matchedFigures.add(figure);
                }
            });
        });
        return matchedFigures;
    }

    private static List<Position> convertMatrixToPositionList(int[][] figureMatrix) {
        List<Position> figurePositions = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (figureMatrix[i][j] == 1) {
                    figurePositions.add(new Position(i, j));
                }
            }
        }
        return figurePositions;
    }

    private static void loadFiguresOnBoard() {
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

    public static Map<ChessFigure, List<ChessFigure>> getAttackMap() {
        return chessBoard.getChessFigureMap();
    }

    public static int[][] getFigureTrajectory(ChessFigure chessFigure) {
        int[][] matrix = new int[8][8];
        switch (chessFigure.getName()) {
            case KING: {
                King king = new King(chessFigure);
                matrix = getAttackedFigures(king.getMoveDirection());
                break;
            }
            case QUEEN: {
                Queen queen = new Queen(chessFigure);
                matrix = getAttackedFigures(queen.getMoveDirection());
                break;
            }
            case ROOK: {
                Rook rook = new Rook(chessFigure);
                matrix = rook.removeDuplicates(getAttackedFigures(rook.getMoveDirection()), chessBoard.getChessMatrix());
                break;
            }
            case BISHOP: {
                Bishop bishop = new Bishop(chessFigure);
                matrix = getAttackedFigures(bishop.getMoveDirection());
                break;
            }
            case KNIGHT: {
                Knight knight = new Knight(chessFigure);
                matrix = getAttackedFigures(knight.getMoveDirection());
                break;
            }
        }
        return matrix;
    }

    private static int[][] getAttackedFigures(int[][] figureMatrix) {
        int[][] finalMatrix = new int[8][8];
        int[][] gameMatrix = chessBoard.getChessMatrix();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                finalMatrix[i][j] = gameMatrix[i][j];
                if (figureMatrix[i][j] == 1) {
                    finalMatrix[i][j] = 1;
//                    if (gameMatrix[i][j] > 1) {
//                        finalMatrix[i][j] = 10;
//                    }
                }
            }
        }
        return finalMatrix;
    }

    public static void clearGameBoard() {
        chessBoard = new ChessBoard();
        if (clearFiguresFile()) {
            loadFigures();
        }
    }


    public static boolean addNewFigure(ChessFigure chessFigure) {
        String chessPath = chessFigure.getColor().toString().toLowerCase(Locale.ROOT) + " " +
                chessFigure.getName().toString().toLowerCase(Locale.ROOT) + " " +
                chessFigure.getPosition().getyPosition() + " " +
                chessFigure.getPosition().getxPosition();
        return writeFigureToFile(chessPath);
    }

    public static List<ChessFigure> getAvailableFigures() {
        List<ChessFigure> allFigures = getAllFigures();
        List<ChessFigure> usedFigures = chessBoard.getFigures();
        usedFigures.forEach(usedFigure -> {
            allFigures.removeIf(figure -> figure.getName().equals(usedFigure.getName()) && figure.getColor().equals(usedFigure.getColor()));
        });
        return allFigures;
    }

    public static void removeFigureFromFile(ChessFigure chessFigure) { //TODO NOT FINISHED FEATURE
        String removeChessLine = chessFigure.getColor().toString().toLowerCase(Locale.ROOT) + " " +
                chessFigure.getName().toString().toLowerCase(Locale.ROOT) + " " +
                chessFigure.getPosition().getxPosition() + " " +
                chessFigure.getPosition().getyPosition();
        try {
            String readFrom = "D:\\PROJECTS\\chessGame\\src\\main\\resources\\game\\init.txt";
            String writeTo = "D:\\PROJECTS\\chessGame\\src\\main\\resources\\game\\temp.txt";
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(writeTo, true));
            BufferedReader bufferedReader = new BufferedReader(new FileReader(readFrom));

            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
                if (!currentLine.trim().equals(removeChessLine.trim())) {
                    bufferedWriter.write(currentLine);
                    bufferedWriter.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeFigure(Position position) {
        ChessFigure chessFigure = chessBoard.getFigures().stream().filter(chessFigure1 -> chessFigure1.getPosition().getxPosition() == position.getxPosition() &&
                        chessFigure1.getPosition().getyPosition() == position.getyPosition())
                .findFirst().orElse(new ChessFigure());
        if (chessFigure.getName() != null && !chessFigure.getName().toString().isEmpty()) {
            chessBoard.getFigures().remove(chessFigure);
            chessBoard.getChessFigureMap().remove(chessFigure);
            removeFigureFromFile(chessFigure);
        }
    }
}
