package com.chess.chessgame.serviceImpl;

import com.chess.chessgame.domain.board.ChessBoard;
import com.chess.chessgame.domain.figures.*;
import com.chess.chessgame.services.GameFieldService;
import com.chess.chessgame.services.GameFileService;
import com.chess.chessgame.services.GameService;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class GameServiceImpl implements GameService {
    public static GameFileService gameFileService = new GameFileServiceImpl();
    public static ChessBoard chessBoard = new ChessBoard();

    @Override
    public void initGame() {
        loadFigures();
        fillFigureMap();
        loadFiguresOnBoard();
        saveGameResults();
    }

    @Override
    public void loadFigures() {
        List<ChessFigure> figures = gameFileService.getFiguresFromInitFile();
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

    @Override
    public void fillFigureMap() {
        List<ChessFigure> chessFigures = chessBoard.getFigures();
        Map<ChessFigure, List<ChessFigure>> chessFigureMap = chessBoard.getChessFigureMap();

        chessFigures.forEach(chessFigure -> {
            List<ChessFigure> passedFigures = getAttackedFigures(chessFigure);
            chessFigureMap.put(chessFigure, passedFigures);
        });
    }

    @Override
    public List<ChessFigure> getAttackedFigures(ChessFigure chessFigure) {
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

    @Override
    public List<Position> convertMatrixToPositionList(int[][] figureMatrix) {
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
    public void loadFiguresOnBoard() {
        GameFieldService gameFieldService = new GameFieldServiceImpl();
        chessBoard.getFigures().forEach(gameFieldService::setFigureOnBoard);
    }

    @Override
    public int getFigureNumber(ChessFigure chessFigure) {
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
        return chessBoard.getChessFigureMap();
    }

    @Override
    public int[][] getFigureTrajectory(ChessFigure chessFigure) {
        int[][] matrix = new int[8][8];
        switch (chessFigure.getName()) {
            case KING: {
                King king = new King(chessFigure);
                matrix = king.getMoveDirection(chessBoard.getChessMatrix());
                break;
            }
            case QUEEN: {
                Queen queen = new Queen(chessFigure);
                matrix = queen.getMoveDirection(chessBoard.getChessMatrix());
                break;
            }
            case ROOK: {
                Rook rook = new Rook(chessFigure);
                matrix = rook.getMoveDirection(chessBoard.getChessMatrix());
                break;
            }
            case BISHOP: {
                Bishop bishop = new Bishop(chessFigure);
                matrix = bishop.getMoveDirection(chessBoard.getChessMatrix());
                break;
            }
            case KNIGHT: {
                Knight knight = new Knight(chessFigure);
                matrix = knight.getMoveDirection(chessBoard.getChessMatrix());
                break;
            }
        }
        return matrix;
    }

    @Override
    public void clearGameBoard() {
        chessBoard = new ChessBoard();
        if (gameFileService.clearFiguresFile()) {
            loadFigures();
        }
    }

    @Override
    public void addNewFigure(ChessFigure chessFigure) {
        gameFileService.writeFigureToFile("init.txt", chessFigure);
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
    public void removeFigure(Position position) {
        ChessFigure chessFigure = chessBoard.getFigures()
                .stream()
                .filter(chessFigure1 -> chessFigure1.getPosition().getxPosition() == position.getxPosition() && chessFigure1.getPosition().getyPosition() == position.getyPosition())
                .findFirst().orElse(new ChessFigure());
        if (chessFigure.getName() != null) {
            chessBoard.getFigures().remove(chessFigure);
            chessBoard.getChessFigureMap().remove(chessFigure);
            gameFileService.removeFigureFromFile(chessFigure);
        }
    }

    @Override
    public boolean saveGameResults() {
        boolean isSaved = false;
        try {
            isSaved = gameFileService.saveResultFile(chessBoard.getChessFigureMap());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSaved;
    }
}
