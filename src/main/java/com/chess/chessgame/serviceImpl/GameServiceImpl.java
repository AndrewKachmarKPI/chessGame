package com.chess.chessgame.serviceImpl;

import com.chess.chessgame.domain.board.ChessBoard;
import com.chess.chessgame.domain.figures.*;
import com.chess.chessgame.enums.FigureColor;
import com.chess.chessgame.enums.FigureName;
import com.chess.chessgame.services.GameFileService;
import com.chess.chessgame.services.GameService;
import javafx.geometry.Pos;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Класс для оброблення ігрової логіки
 */
public class GameServiceImpl implements GameService {
    public GameFileService gameFileService;
    public ChessBoard chessBoard;

    public GameServiceImpl() {
        this.gameFileService = new GameFileServiceImpl();
        this.chessBoard = new ChessBoard();
    }

    /**
     * Запуск та створення гри
     *
     * @param fileName шлях до вхідного файлу
     */
    @Override
    public void initGame(String fileName) {
        loadFigures(fileName);
        fillFigureMap();
    }

    /**
     * Завантаження фігур з файлу у гру
     *
     * @param fileName шлях до вхідного файлу
     */
    private void loadFigures(String fileName) {
        List<ChessFigure> figures = gameFileService.getFiguresFromFile(fileName);
        int[][] matrix = new int[8][8];
        Map<ChessFigure, List<ChessFigure>> chessFigureMap = new HashMap<>();
        Map<ChessFigure, int[][]> figureMatrix = new HashMap<>();
        figures.forEach(chessFigure -> {
            chessFigureMap.put(chessFigure, new ArrayList<>());
            matrix[chessFigure.getPosition().getxPosition()][chessFigure.getPosition().getyPosition()] = getFigureNumber(chessFigure);
        });
        chessBoard = new ChessBoard(figures, chessFigureMap, figureMatrix, matrix);
        figures.forEach(chessFigure -> figureMatrix.put(chessFigure, chessFigure.getMoveDirection(chessBoard.getChessMatrix())));
        chessBoard.setFigureMatrix(figureMatrix);
    }

    /**
     * Заповнення матриці об’єктів фігурами та їхніми атаками
     */
    private void fillFigureMap() {
        List<ChessFigure> chessFigures = chessBoard.getFigures();
        Map<ChessFigure, List<ChessFigure>> chessFigureMap = chessBoard.getChessFigureMap();
        chessFigures.forEach(chessFigure -> chessFigureMap.put(chessFigure, getAttackedFigures(chessFigure)));
    }

    /**
     * Отримання фігур які б’ються вибраною фігурою
     *
     * @param chessFigure об'єкт фігури
     * @return список атакованих фігур
     */
    private List<ChessFigure> getAttackedFigures(ChessFigure chessFigure) {
        List<Position> figurePositions = convertMatrixToPositionList(getFigureTrajectory(chessFigure.getPosition(), chessFigure.getName(), chessFigure.getColor()));
        List<ChessFigure> matchedFigures = new ArrayList<>();

        figurePositions.forEach(position -> chessBoard.getFigures().forEach(figure -> {
            if (position.equals(figure.getPosition()) && !figure.getColor().equals(chessFigure.getColor())) {//COLOR check
                matchedFigures.add(figure);
            }
        }));
        return matchedFigures;
    }

    /**
     * Метод для вираховування ходів короля
     *
     * @param chessFigure  об'єкт короля
     * @param figureMatrix матриця короля
     */
    private void kingMovementsCheck(ChessFigure chessFigure, int[][] figureMatrix) {
        chessBoard.getFigureMatrix().forEach((figure, matrix) -> {
            if (chessFigure.getPosition() != figure.getPosition() && figure.getColor() != chessFigure.getColor()) {//COLOR check
                chessFigure.getAttackService().removeTrailing(figure, chessFigure, matrix, figureMatrix);

                List<Position> attackPos = convertMatrixToPositionList(matrix);
                Optional<Position> position = attackPos.stream().filter(pos -> pos.equals(chessFigure.getPosition())).findAny();
                if (figure.getName() != FigureName.KNIGHT && figure.getName() != FigureName.BISHOP && position.isPresent()) {
                    chessFigure.getAttackService().removeAxis(figure, chessFigure, matrix, figureMatrix);
                }
                if (position.isPresent() && figure.getName() != FigureName.KNIGHT && figure.getName() != FigureName.ROOK) {
                    chessFigure.getAttackService().removeDiagonal(figure, chessFigure, matrix, figureMatrix);
                }
            }
        });
        List<Position> attackPos = convertMatrixToPositionList(figureMatrix);
        attackPos.forEach(position -> {
            Optional<ChessFigure> foundFigure = chessBoard.getFigures().stream().filter(figure -> figure.getPosition().equals(position)).findFirst();
            foundFigure.ifPresent(figure -> {
                System.out.println("ATTACKED" + figure.getName() + "-" + figure.getColor());
                if (isAttacked(chessFigure, figure)) {
                    figureMatrix[figure.getPosition().getxPosition()][figure.getPosition().getyPosition()] = 0;
                }
            });
        });
    }

    private boolean isAttacked(ChessFigure compareFigure, ChessFigure attackedFigure) {
        AtomicBoolean isAttacked = new AtomicBoolean(false);
        chessBoard.getFigureMatrix().forEach((figure, matrix) -> {
            if (!compareFigure.equals(figure) && !compareFigure.getColor().equals(figure.getColor())) { //COLOR check
                List<Position> positions = convertMatrixToPositionList(matrix);
                Optional<Position> finalPosition = positions.stream()
                        .filter(position -> position.equals(attackedFigure.getPosition())).findFirst();
                if (finalPosition.isPresent()) {
                    isAttacked.set(true);
                }
            }
        });
        return isAttacked.get();
    }

    /**
     * Конвертація матриці чисел у список обєктів позицій
     *
     * @param figureMatrix матриця фігури
     * @return спиосок позицій атакованих фігур
     */
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

    /**
     * Отримання усіх фігур на шахівниці
     *
     * @return список фігур
     */
    @Override
    public List<ChessFigure> getFiguresForSetup() {
        return chessBoard.getFigures();
    }


    /**
     * Отримання порядкового номеру фігури
     *
     * @param chessFigure об'єкт фігури
     * @return номер фігури
     */
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

    /**
     * Отримання списку яка фігура яку б’є
     *
     * @return Map атак фігур
     */
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

    /**
     * Отримання траєкторії ходу фігури
     *
     * @param pos         позиція фігури
     * @param figureName  назва фігури
     * @param figureColor колір фігури
     * @return матриця траекторії фігури
     */
    @Override
    public int[][] getFigureTrajectory(Position pos, FigureName figureName, FigureColor figureColor) {
        int[][] matrix = new int[8][8];
        ChessFigure chessFigure = createChessFigure(pos, figureName, figureColor);
        if (chessFigure != null) {
            matrix = chessFigure.getMoveDirection(chessBoard.getChessMatrix());
        }
        if (figureName == FigureName.KING) {
            kingMovementsCheck(chessFigure, matrix);
        }
        return matrix;
    }

    /**
     * Створення шахової фігури відповідних параметрів
     *
     * @param pos         позиція фігури
     * @param figureName  назва фігури
     * @param figureColor колір фігури
     * @return створений об'єкт фігури
     */
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

    /**
     * Очищення шахівниці
     *
     * @param fileName шлях до вхідного файлу
     */
    @Override
    public void clearGameBoard(String fileName) {
        chessBoard = new ChessBoard();
        if (gameFileService.clearFiguresFile(fileName)) {
            chessBoard = new ChessBoard();
        }
    }

    /**
     * Додавання фігури на шахівницю
     *
     * @param chessFigure об'єкт фігури
     * @param fileName    шлях до вхідного файлу
     */
    @Override
    public void addNewFigure(ChessFigure chessFigure, String fileName) {
        gameFileService.appendFigureToFile(fileName, chessFigure);
    }

    /**
     * Видалення фігури з шахівниці
     *
     * @param position позиція фігури
     * @param fileName шлях до вхідного файлу
     */
    @Override
    public void removeFigure(Position position, String fileName) {
        Optional<ChessFigure> chessFigure = chessBoard.getFigures()
                .stream()
                .filter(chessFigure1 -> chessFigure1.getPosition().getxPosition() == position.getxPosition() && chessFigure1.getPosition().getyPosition() == position.getyPosition())
                .findFirst();
        if (chessFigure.isPresent()) {
            chessBoard.getFigures().remove(chessFigure.get());
            chessBoard.getChessFigureMap().remove(chessFigure.get());
            gameFileService.removeFigureFromFile(chessFigure.get(), fileName);
        }
    }

    /**
     * Зберігання результату гри
     *
     * @param directory шлях до вхідного файлу
     * @return чи збережено файл
     */
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

    /**
     * Отримання списку доступних фігур для розміщення
     *
     * @return спсисок доступних фігур
     */
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
