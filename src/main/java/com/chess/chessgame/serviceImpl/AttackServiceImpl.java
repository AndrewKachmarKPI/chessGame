package com.chess.chessgame.serviceImpl;

import com.chess.chessgame.domain.figures.ChessFigure;
import com.chess.chessgame.domain.figures.Position;
import com.chess.chessgame.services.AttackService;

/**
 * Клас для обробки, вираховування пересувань та можливих атак фігур
 */
public class AttackServiceImpl implements AttackService {
    /**
     * Метод для отримання вертикального зрізу матриці
     *
     * @param matrix   матриця гри або фігури
     * @param position позиція фігури
     * @return вертикальний зріз матриці
     */
    @Override
    public int[] getVerticalSplice(int[][] matrix, int position) {
        int[] verticalSplice = new int[8];
        for (int i = 0; i < 8; i++) {
            verticalSplice[i] = matrix[i][position];
        }
        return verticalSplice;
    }

    /**
     * Запис вертикального зрізу у матрицю
     *
     * @param matrix   матриця гри або фігури
     * @param splice   вертикальний зріз
     * @param position позиція фігури
     * @return матриця гри або фігури
     */
    @Override
    public int[][] setVerticalSplice(int[][] matrix, int[] splice, int position) {
        for (int i = 0; i < 8; i++) {
            matrix[i][position] = splice[i];
        }
        return matrix;
    }

    /**
     * Форматування зрізу у двох напрамках проставлення можливих ходів фігури
     *
     * @param figureArray масив матриці ходів фігури
     * @param gameArray   масив матриці гри
     * @param position    позиція фігури
     * @return результуючий масив ходів та атак
     */
    @Override
    public int[] processSpliceMultiDirection(int[] figureArray, int[] gameArray, int position) {
        boolean isFound = false;
        for (int i = position + 1; i < figureArray.length; i++) {
            if (gameArray[i] > 1 && !isFound) {
                figureArray[i] = 10;
                isFound = true;
            } else {
                if (isFound) {
                    figureArray[i] = 0;
                }
            }
        }
        isFound = false;
        for (int i = position - 1; i + 1 > 0; i--) {
            if (gameArray.length > 1 && gameArray[i] > 1 && !isFound) {
                figureArray[i] = 10;
                isFound = true;
            } else {
                if (isFound) {
                    figureArray[i] = 0;
                }
            }
        }
        return figureArray;
    }

    /**
     * Форматування діагонального зрізу і проставлення можливих ходів фігури
     *
     * @param figureArray  масив матриці ходів фігури
     * @param gameArray    масив матриці гри
     * @param figureNumber номер фігури
     * @return результуючий масив ходів та атак
     */
    @Override
    public int[] processSpliceDiagonal(int[] figureArray, int[] gameArray, int figureNumber) {
        boolean isFound = false;
        for (int i = getPosOfElement(figureArray, figureNumber) + 1; i < figureArray.length; i++) {
            if (gameArray.length > 1 && gameArray[i] > 1 && !isFound) {
                figureArray[i] = 10;
                isFound = true;
            } else {
                if (isFound) {
                    figureArray[i] = 0;
                }
            }
        }
        isFound = false;
        for (int i = getPosOfElement(figureArray, figureNumber) - 1; i + 1 > 0; i--) {
            if (gameArray.length > 1 && gameArray[i] > 1 && !isFound) {
                figureArray[i] = 10;
                isFound = true;
            } else {
                if (isFound) {
                    figureArray[i] = 0;
                }
            }
        }
        return figureArray;
    }

    /**
     * Запис діагонального зрізу у матрицю
     *
     * @param matrix   матриця гри або фігури
     * @param splice   масив для запису в матрицю
     * @param isMain   головна чи побічна діагональ матриці
     * @param position позиція фігури
     */
    @Override
    public void setDiagonalSplice(int[][] matrix, int[] splice, boolean isMain, Position position) {
        int k = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isMain) {
                    if ((i + position.getyPosition() == j + position.getxPosition()) && k < splice.length) {
                        matrix[i][j] = splice[k];
                        k++;
                    }
                } else {
                    if ((i + (8 - position.getyPosition()) + j - position.getxPosition() == 8) && k < splice.length) {
                        matrix[i][j] = splice[k];
                        k++;
                    }
                }
            }
        }
    }

    /**
     * Отримання діагонального зрізу матриці фігури відносно позиції
     *
     * @param matrix   матриця гри або фігури
     * @param isMain   головна чи побічна діагональ матриці
     * @param position позиція фігури
     * @return діагональний зріз матриці
     */
    @Override
    public int[] getDiagonalSplice(int[][] matrix, boolean isMain, Position position) {
        int[] splice = {};

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isMain) {
                    if (i + position.getyPosition() == j + position.getxPosition()) {
                        splice = addElementToArray(splice, matrix[i][j]);
                    }
                } else {
                    if (i + (8 - position.getyPosition()) + j - position.getxPosition() == 8) {
                        splice = addElementToArray(splice, matrix[i][j]);
                    }
                }
            }
        }
        return splice;
    }

    /**
     * Отримання позиції фігури у масиві
     *
     * @param array        масив для отримання позиції
     * @param figureNumber елемент пошуку
     * @return позиція елементу в масиві
     */
    @Override
    public int getPosOfElement(int[] array, int figureNumber) {
        int pos = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == figureNumber) {
                pos = i;
            }
        }
        return pos;
    }

    /**
     * Додавання елементу до масиву
     *
     * @param array   масив
     * @param element елемент
     * @return масив з доданим елементом
     */
    @Override
    public int[] addElementToArray(int[] array, int element) {
        int[] newArray = new int[array.length + 1];
        System.arraycopy(array, 0, newArray, 0, array.length);
        newArray[array.length] = element;
        return newArray;
    }

    /**
     * Проставлення можливих ходів фігури відносно матриць гри
     *
     * @param matrix     матриця фігури
     * @param gameMatrix матриця гри
     * @return матриця фігури з атаками
     */
    @Override
    public int[][] setAttackSimple(int[][] matrix, int[][] gameMatrix) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (gameMatrix[i][j] > 1 && matrix[i][j] == 1) {
                    matrix[i][j] = 10;
                }
            }
        }
        return matrix;
    }

    /**
     * Вираховування ходів короля
     *
     * @param figure       фігура з дошки
     * @param chessFigure  король
     * @param matrix       матриця для фігури
     * @param figureMatrix матриця короля
     */
    public void removeTrailing(ChessFigure figure, ChessFigure chessFigure, int[][] matrix, int[][] figureMatrix) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (matrix[i][j] == 1) {
                    figureMatrix[i][j] = 0;
                }
            }
        }
    }

    /**
     * Вираховування ходів короля по горизонталі і вертикалі
     *
     * @param figure       фігура з дошки
     * @param chessFigure  король
     * @param matrix       матриця для фігури
     * @param figureMatrix матриця короля
     */
    public void removeAxis(ChessFigure figure, ChessFigure chessFigure, int[][] matrix, int[][] figureMatrix) {
        for (int j = 0; j < 8; j++) {
            if (matrix[figure.getPosition().getxPosition()][j] == 10 && figure.getPosition().getxPosition() == chessFigure.getPosition().getxPosition()) {
                if (figure.getPosition().getyPosition() > chessFigure.getPosition().getyPosition() && j != 0) {
                    figureMatrix[figure.getPosition().getxPosition()][j - 1] = 0;
                }
                if (figure.getPosition().getyPosition() < chessFigure.getPosition().getyPosition() && j != 7) {
                    figureMatrix[figure.getPosition().getxPosition()][j + 1] = 0;
                }
            }
            if (matrix[j][figure.getPosition().getyPosition()] == 10 && figure.getPosition().getyPosition() == chessFigure.getPosition().getyPosition()) {
                if (figure.getPosition().getxPosition() > chessFigure.getPosition().getxPosition() && j != 0) {
                    figureMatrix[j-1][figure.getPosition().getyPosition()] = 0;
                }
                if (figure.getPosition().getxPosition() < chessFigure.getPosition().getxPosition() && j != 7) {
                    figureMatrix[j+1][figure.getPosition().getyPosition()] = 0;
                }
            }
        }
    }

    /**
     * Вираховування ходів короля по діагоналях
     * @param figure       фігура з дошки
     * @param chessFigure  король
     * @param matrix       матриця для фігури
     * @param figureMatrix матриця короля
     */
    public void removeDiagonal(ChessFigure figure, ChessFigure chessFigure, int[][] matrix, int[][] figureMatrix) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (matrix[i][j] == 1 || matrix[i][j] == 10 && isOneDiagonal(figure.getPosition(),chessFigure.getPosition(), i, j, true)
                        || isOneDiagonal(figure.getPosition(),chessFigure.getPosition(), i, j, false)) {
                    if (figure.getPosition().getxPosition() > chessFigure.getPosition().getxPosition() &&
                            figure.getPosition().getyPosition() > chessFigure.getPosition().getyPosition() && chessFigure.getPosition().getxPosition() != 0 && chessFigure.getPosition().getyPosition() != 0) {
                        figureMatrix[chessFigure.getPosition().getxPosition() - 1][chessFigure.getPosition().getyPosition() - 1] = 0;
                    }
                    if (figure.getPosition().getxPosition() < chessFigure.getPosition().getxPosition() &&
                            figure.getPosition().getyPosition() < chessFigure.getPosition().getyPosition() && chessFigure.getPosition().getxPosition() != 7 && chessFigure.getPosition().getyPosition() != 7) {
                        figureMatrix[chessFigure.getPosition().getxPosition() + 1][chessFigure.getPosition().getyPosition() + 1] = 0;
                    }
                    if (figure.getPosition().getxPosition() < chessFigure.getPosition().getxPosition() &&
                            figure.getPosition().getyPosition() > chessFigure.getPosition().getyPosition() && chessFigure.getPosition().getxPosition() != 7 && chessFigure.getPosition().getyPosition() != 0) {
                        figureMatrix[chessFigure.getPosition().getxPosition() + 1][chessFigure.getPosition().getyPosition() - 1] = 0;
                    }
                    if (figure.getPosition().getxPosition() > chessFigure.getPosition().getxPosition() &&
                            figure.getPosition().getyPosition() < chessFigure.getPosition().getyPosition() && chessFigure.getPosition().getxPosition() != 0 && chessFigure.getPosition().getyPosition() != 7) {
                        figureMatrix[chessFigure.getPosition().getxPosition() - 1][chessFigure.getPosition().getyPosition() + 1] = 0;
                    }
                }
            }
        }
    }

    /**
     * Метод перевірки чи 2 фігури знаходяться на 1 діагоналі
     * @param figurePosition позиція першої фігури
     * @param chessPosition позиція другої фігури
     * @param i індекс
     * @param j індекс
     * @param mode головна чи побічна діагональ
     * @return чи 2 фігури знаходяться на одній діагоналі
     */
    private boolean isOneDiagonal(Position figurePosition, Position chessPosition, int i, int j, boolean mode) {
        if (mode) {
            return isDiagonal(figurePosition, i, j, true) && isDiagonal(chessPosition, i, j, true);
        } else {
            return isDiagonal(figurePosition, i, j, false) && isDiagonal(chessPosition, i, j, false);
        }
    }

    /**
     * @param position позиція фігури
     * @param i індекс
     * @param j індекс
     * @param mode головна чи побічна діагональ
     * @return чи фігури знаходяться на діагоналі
     */
    public boolean isDiagonal(Position position, int i, int j, boolean mode) {
        if (mode) {
            return i + position.getyPosition() == j + position.getxPosition();
        } else {
            return i + (8 - position.getyPosition()) + j - position.getxPosition() == 8;
        }
    }

    /**
     *
     * @param position позиція фігури
     * @param x індекс
     * @return чи індекс сусідній до позиції по x
     */
    public boolean isNeighbourX(Position position, int x) {
        return Math.abs(position.getxPosition() - x) == 1;
    }
    /**
     *
     * @param position позиція фігури
     * @param y індекс
     * @return чи індекс сусідній до позиції по y
     */
    public boolean isNeighbourY(Position position, int y) {
        return Math.abs(position.getyPosition() - y) == 1;
    }
}
