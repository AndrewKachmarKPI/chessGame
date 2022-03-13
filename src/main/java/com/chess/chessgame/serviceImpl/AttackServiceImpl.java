package com.chess.chessgame.serviceImpl;

import com.chess.chessgame.domain.figures.Position;
import com.chess.chessgame.services.AttackService;

public class AttackServiceImpl implements AttackService {
    @Override
    public int[] getVerticalSplice(int[][] matrix, int position) {
        int[] verticalSplice = new int[8];
        for (int i = 0; i < 8; i++) {
            verticalSplice[i] = matrix[i][position];
        }
        return verticalSplice;
    }

    @Override
    public int[][] setVerticalSplice(int[][] matrix, int[] splice, int position) {
        for (int i = 0; i < 8; i++) {
            matrix[i][position] = splice[i];
        }
        return matrix;
    }

    @Override
    public int[] processSpliceHorizontal(int[] figureArray, int[] gameArray, int position) {
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

    @Override
    public int[] processSpliceVertical(int[] figureArray, int[] gameArray, int xPosition) {
        boolean isFound = false;
        for (int i = xPosition + 1; i < figureArray.length; i++) {
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
        for (int i = xPosition - 1; i + 1 > 0; i--) {
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

    public int[] addElementToArray(int[] array, int element) {
        int[] newArray = new int[array.length + 1];
        System.arraycopy(array, 0, newArray, 0, array.length);
        newArray[array.length] = element;
        return newArray;
    }
}
