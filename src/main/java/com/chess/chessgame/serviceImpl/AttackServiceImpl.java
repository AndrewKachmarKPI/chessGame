package com.chess.chessgame.serviceImpl;

import com.chess.chessgame.domain.figures.Position;
import com.chess.chessgame.services.AttackService;

/**
 * Клас для обробки, вираховування пересувань та можливих атак фігур
 */
public class AttackServiceImpl implements AttackService {
    /**
     * Метод для отримання вертикального зрізу матриці
     * @param matrix матриця гри або фігури
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
     * @param matrix матриця гри або фігури
     * @param splice вертикальний зріз
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
     * @param figureArray масив матриці ходів фігури
     * @param gameArray масив матриці гри
     * @param position позиція фігури
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
     * @param figureArray масив матриці ходів фігури
     * @param gameArray масив матриці гри
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
     * @param matrix матриця гри або фігури
     * @param splice масив для запису в матрицю
     * @param isMain головна чи побічна діагональ матриці
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
     * @param matrix матриця гри або фігури
     * @param isMain головна чи побічна діагональ матриці
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
     * @param array масив для отримання позиції
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
     * @param array масив
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
     * @param matrix матриця фігури
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
}
