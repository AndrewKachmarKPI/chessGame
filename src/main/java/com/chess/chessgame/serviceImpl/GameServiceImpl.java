package com.chess.chessgame.serviceImpl;

import com.chess.chessgame.domain.board.ChessBoard;
import com.chess.chessgame.domain.figures.*;
import com.chess.chessgame.enums.FigureColor;
import com.chess.chessgame.enums.FigureName;
import com.chess.chessgame.services.GameFileService;
import com.chess.chessgame.services.GameService;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class GameServiceImpl implements GameService {
    public GameFileService gameFileService;
    public ChessBoard chessBoard;

    public GameServiceImpl() {
        this.gameFileService = new GameFileServiceImpl();
        this.chessBoard = new ChessBoard();
    }

    @Override
    public void initGame(String file) {
        loadFigures(file);
        fillFigureMap();
    }

    private void loadFigures(String fileName) {
        List<ChessFigure> figures =  gameFileService.getFiguresFromFile(fileName);
        int[][] matrix = new int[8][8];
        Map<ChessFigure, List<ChessFigure>> chessFigureMap = new HashMap<>();
        figures.forEach(chessFigure -> {
            chessFigureMap.put(chessFigure, new ArrayList<>());
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (chessFigure.getPosition().getxPosition() == i && chessFigure.getPosition().getyPosition() == j) {
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

    private void fillFigureMap() {
        List<ChessFigure> chessFigures = chessBoard.getFigures();
        Map<ChessFigure, List<ChessFigure>> chessFigureMap = chessBoard.getChessFigureMap();

        chessFigures.forEach(chessFigure -> {
            List<ChessFigure> passedFigures = getAttackedFigures(chessFigure);
            chessFigureMap.put(chessFigure, passedFigures);
        });
    }

    private List<ChessFigure> getAttackedFigures(ChessFigure chessFigure) {
        List<Position> figurePositions = convertMatrixToPositionList(getFigureTrajectory(chessFigure.getPosition(), chessFigure.getName(), chessFigure.getColor()));
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
        gameFileService.writeFigureToFile(fileName, chessFigure);
    }

    @Override
    public List<ChessFigure> getAvailableFigures() {
        List<ChessFigure> allFigures = gameFileService.getAllFigures();
        if (chessBoard.getFigures().size() >= 10) {
            allFigures = new ArrayList<>();
        }
        return allFigures;
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
    public boolean saveGameResults() {
        boolean isSaved = false;
        try {
            isSaved = gameFileService.saveResultFile(getAttackMap());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSaved;
    }
}
