package com.chess.chessgame.services;

import com.chess.chessgame.domain.board.BoardCell;
import com.chess.chessgame.domain.board.ChessBoard;
import com.chess.chessgame.domain.board.InitChessBoard;
import com.chess.chessgame.domain.figures.*;
import com.chess.chessgame.enums.FigureColor;
import com.chess.chessgame.enums.FigureName;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


public class GameService {
    public static ChessBoard chessBoard = new ChessBoard();

    public static void initGame() {
        createGame();
        paintFigures();
        createFigurePassingMap();
    }

    private static void createGame() {
        File file = new File("D:\\PROJECTS\\chessGame\\src\\main\\resources\\game\\init.txt");
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            List<ChessFigure> figures = new ArrayList<>();
            int[][] matrix = new int[8][8];
            Map<ChessFigure, List<ChessFigure>> chessFigureMap = new HashMap<>();
            while ((line = bufferedReader.readLine()) != null && figures.size() <= 10) {
                line = line.trim();
                String color = line.split(" ")[0];
                String name = line.split(" ")[1];
                Position position = new Position(Integer.parseInt(line.split(" ")[2]), Integer.parseInt(line.split(" ")[3]));
                ChessFigure chessFigure = new ChessFigure(FigureName.valueOf(name.toUpperCase(Locale.ROOT)),
                        FigureColor.valueOf(color.toUpperCase(Locale.ROOT)), position);
                figures.add(chessFigure);
                chessFigureMap.put(chessFigure, new ArrayList<>());
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (chessFigure.getPosition().getyPosition() == i && chessFigure.getPosition().getxPosition() == j) {
                            matrix[i][j] = getFigureNumber(chessFigure);
                            break;
                        }
                    }
                }
            }
            chessBoard.setChessFigureMap(chessFigureMap);
            chessBoard.setFigures(figures);
            chessBoard.setChessMatrix(matrix);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createFigurePassingMap() {
        List<ChessFigure> chessFigures = chessBoard.getFigures();
        Map<ChessFigure, List<ChessFigure>> chessFigureMap = chessBoard.getChessFigureMap();

        chessFigures.forEach(chessFigure -> {
            List<ChessFigure> passedFigures = checkForPassing(chessFigure);
            chessFigureMap.put(chessFigure, passedFigures);
        });
    }

    private static List<ChessFigure> checkForPassing(ChessFigure chessFigure) {
        List<Position> figurePositions = new ArrayList<>();
        int[][] figureMatrix = getFigureTrajectory(chessFigure);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (figureMatrix[i][j] == 1) {
                    figurePositions.add(new Position(i, j));
                }
            }
        }
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
                if (position.equals(figure.getPosition())) {
                    matchedFigures.add(figure);
                }
            });
        });
        return matchedFigures;
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

    public static List<Position> getPassedPositions(ChessFigure chessFigure) {
        ChessFigure figure = chessBoard.getFigures().stream().filter(chessFigure1 -> chessFigure1.getName().equals(chessFigure.getName()))
                .findFirst().orElse(new ChessFigure());
        return chessBoard.getChessFigureMap().get(figure).stream().map(ChessFigure::getPosition).collect(Collectors.toList());
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
//                    if (gameMatrix[i][j] > 1) {
//                        finalMatrix[i][j] = 10;
//                    }
                }
            }
        }
        return finalMatrix;
    }

    public static void clearGameField() {
        chessBoard = new ChessBoard();
        try {
            File file = new File("D:\\PROJECTS\\chessGame\\src\\main\\resources\\game\\init.txt");
            PrintWriter writer = new PrintWriter(file);
            writer.print("");
            writer.close();
            createGame();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static boolean writeFigureToFile(ChessFigure chessFigure) {
        try {

            String fileName = "D:\\PROJECTS\\chessGame\\src\\main\\resources\\game\\init.txt";
            FileWriter fileWriter = new FileWriter(fileName, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            String chessPath = chessFigure.getColor().toString().toLowerCase(Locale.ROOT) + " " +
                    chessFigure.getName().toString().toLowerCase(Locale.ROOT) + " " +
                    chessFigure.getPosition().getyPosition() + " " +
                    chessFigure.getPosition().getxPosition();
            bufferedWriter.write(chessPath);
            bufferedWriter.newLine();
            bufferedWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
