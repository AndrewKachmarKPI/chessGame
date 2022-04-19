package com.chess.chessgame.services;

import com.chess.chessgame.domain.figures.ChessFigure;
import javafx.scene.image.Image;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;


public interface GameFileService {
    List<ChessFigure> getFiguresFromInitFile();

    void removeFigureFromFile(ChessFigure chessFigure);

    List<ChessFigure> getAllFigures();

    boolean clearFiguresFile();

    boolean writeFigureToFile(String fileName,ChessFigure chessFigure);
    boolean writeFigureToFile(String fileName, ChessFigure chessFigure, String... args);
    boolean saveResultFile(Map<ChessFigure, List<ChessFigure>> chessFigureListMap) throws IOException;

    String getFigurePath(ChessFigure chessFigure);

    Image loadImageByPath(String path);
    void createWorkingFiles() throws IOException;
    boolean deleteWorkingFiles();
}
