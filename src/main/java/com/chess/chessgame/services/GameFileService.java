package com.chess.chessgame.services;

import com.chess.chessgame.domain.figures.ChessFigure;
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public interface GameFileService {
    List<ChessFigure> getFiguresFromFile(String fileName);

    void removeFigureFromFile(ChessFigure chessFigure, String fileName);

    List<ChessFigure> getAllFigures();

    boolean clearFiguresFile(String fileName);

    void writeFigureToFile(String fileName, ChessFigure chessFigure);

    void writeFigureToFile(String fileName, ChessFigure chessFigure, String... args);

    boolean saveResultFile(Map<ChessFigure, List<ChessFigure>> chessFigureListMap) throws IOException;

    String getFigurePath(ChessFigure chessFigure);

    Image loadImageByPath(String path);

    void createWorkingFiles() throws IOException;

    void createDefaultGameFile() throws IOException;

    void deleteWorkingFiles();

    boolean gameFileValidator(String fileName);

    String getFileContent(String fileName);
}
