package com.chess.chessgame.serviceImpl;

import com.chess.chessgame.domain.board.ChessBoard;
import com.chess.chessgame.domain.figures.*;
import com.chess.chessgame.enums.FigureColor;
import com.chess.chessgame.enums.FigureName;
import com.chess.chessgame.services.GameFileService;
import com.chess.chessgame.services.GameService;

import java.io.IOException;
import java.util.*;


public class GameServiceImpl implements GameService {
    public GameFileService gameFileService;
    public ChessBoard chessBoard;

    public GameServiceImpl() {
        this.gameFileService = new GameFileServiceImpl();
        this.chessBoard = new ChessBoard();
    }

    @Override
    public void initGame(String fileName) {
        loadFigures(fileName);
        fillFigureMap();
    }

    private void loadFigures(String fileName) {
        List<ChessFigure> figures = gameFileService.getFiguresFromFile(fileName);
        int[][] matrix = new int[8][8];
        Map<ChessFigure, List<ChessFigure>> chessFigureMap = new HashMap<>();
        figures.forEach(chessFigure -> {
            chessFigureMap.put(chessFigure, new ArrayList<>());
            matrix[chessFigure.getPosition().getxPosition()][chessFigure.getPosition().getyPosition()] = getFigureNumber(chessFigure);
        });
        chessBoard = new ChessBoard(figures, chessFigureMap, matrix);
    }

    private void fillFigureMap() {
        List<ChessFigure> chessFigures = chessBoard.getFigures();
        Map<ChessFigure, List<ChessFigure>> chessFigureMap = chessBoard.getChessFigureMap();
        chessFigures.forEach(chessFigure -> chessFigureMap.put(chessFigure, getAttackedFigures(chessFigure)));
    }

    private List<ChessFigure> getAttackedFigures(ChessFigure chessFigure) {
        List<Position> figurePositions = convertMatrixToPositionList(getFigureTrajectory(chessFigure.getPosition(), chessFigure.getName(), chessFigure.getColor()));
        List<ChessFigure> matchedFigures = new ArrayList<>();

        figurePositions.forEach(position -> chessBoard.getFigures().forEach(figure -> {
            if (position.equals(figure.getPosition()) && !figure.getColor().equals(chessFigure.getColor())) {
                matchedFigures.add(figure);
            }
        }));
        return matchedFigures;
    }

    private List<Position> convertMatrixToPositionList(int[][] figureMatrix) {
        List<Position> figurePositions = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (figureMatrix[i][j] == 10) {
                    figurePositions.add(new Position(i, j));
                }
            }
        }
        return figurePositions;
    }

    @Override
    public List<ChessFigure> getFiguresForSetup() {
        return chessBoard.getFigures();
    }


    private int getFigureNumber(ChessFigure chessFigure) {
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

    @Override
    public Map<ChessFigure, List<ChessFigure>> getAttackMap() {
        Map<ChessFigure, List<ChessFigure>> chessFigureListMap = new HashMap<>();
        chessBoard.getChessFigureMap().forEach((chessFigure, chessFigures) -> {
            if (!chessFigures.isEmpty()) {
                chessFigureListMap.put(chessFigure, chessFigures);
            }
        });
        return chessFigureListMap;
    }

    @Override
    public int[][] getFigureTrajectory(Position pos, FigureName figureName, FigureColor figureColor) {
        int[][] matrix = new int[8][8];
        ChessFigure chessFigure = createChessFigure(pos, figureName, figureColor);
        if (chessFigure != null) {
            matrix = chessFigure.getMoveDirection(chessBoard.getChessMatrix());
        }
        return matrix;
    }

    @Override
    public ChessFigure createChessFigure(Position pos, FigureName figureName, FigureColor figureColor) {
        switch (figureName) {
            case KING: {
                return new King(figureName, figureColor, pos);
            }
            case QUEEN: {
                return new Queen(figureName, figureColor, pos);
            }
            case ROOK: {
                return new Rook(figureName, figureColor, pos);
            }
            case BISHOP: {
                return new Bishop(figureName, figureColor, pos);
            }
            case KNIGHT: {
                return new Knight(figureName, figureColor, pos);
            }
        }
        return null;
    }

    @Override
    public void clearGameBoard(String fileName) {
        chessBoard = new ChessBoard();
        if (gameFileService.clearFiguresFile(fileName)) {
            chessBoard = new ChessBoard();
        }
    }

    @Override
    public void addNewFigure(ChessFigure chessFigure, String fileName) {
        gameFileService.appendFigureToFile(fileName, chessFigure);
    }

    @Override
    public void removeFigure(Position position, String fileName) {
        ChessFigure chessFigure = chessBoard.getFigures()
                .stream()
                .filter(chessFigure1 -> chessFigure1.getPosition().getxPosition() == position.getxPosition() && chessFigure1.getPosition().getyPosition() == position.getyPosition())
                .findFirst().orElseThrow(() -> new RuntimeException("Figure not found!"));
        if (chessFigure.getName() != null) {
            chessBoard.getFigures().remove(chessFigure);
            chessBoard.getChessFigureMap().remove(chessFigure);
            gameFileService.removeFigureFromFile(chessFigure, fileName);
        }
    }

    @Override
    public boolean saveGameResults(String directory) {
        boolean isSaved = false;
        try {
            isSaved = gameFileService.saveResultFile(getAttackMap(), directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSaved;
    }

    @Override
    public List<ChessFigure> getAvailableFigures() {
        List<ChessFigure> figures = new ArrayList<>();
        if (chessBoard.getFigures().size() <= 10) {
            String allFigures = "white king\n" + "white queen\n" + "white rook\n" + "white bishop\n" + "white knight\n" + "black king\n" + "black queen\n" + "black rook\n" + "black bishop\n" + "black knight\n";
            String[] splitFigures = allFigures.split("\n");
            for (String splitFigure : splitFigures) {
                String line = splitFigure.trim();
                String color = line.split(" ")[0];
                String name = line.split(" ")[1];
                figures.add(createChessFigure(new Position(), FigureName.valueOf(name.toUpperCase(Locale.ROOT)), FigureColor.valueOf(color.toUpperCase(Locale.ROOT))));
            }
        }
        return figures;
    }
}
