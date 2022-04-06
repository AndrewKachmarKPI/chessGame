package com.chess.chessgame.serviceImpl;

import com.chess.chessgame.domain.figures.ChessFigure;
import com.chess.chessgame.domain.figures.Position;
import com.chess.chessgame.enums.FigureColor;
import com.chess.chessgame.enums.FigureName;
import com.chess.chessgame.services.GameFileService;
import javafx.scene.image.Image;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class GameFileServiceImpl implements GameFileService {
    @Override
    public List<ChessFigure> getFiguresFromInitFile() {
        List<ChessFigure> figures = new ArrayList<>();
        InputStream file = getFileInputStream("game/init.txt");
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file));
            String line;
            while ((line = bufferedReader.readLine()) != null) { //&& figures.size() <= 10
                line = line.trim();
                if (!line.isEmpty()) {
                    String color = line.split(" ")[0];
                    String name = line.split(" ")[1];
                    Position position = new Position(Integer.parseInt(line.split(" ")[2]), Integer.parseInt(line.split(" ")[3]));
                    ChessFigure chessFigure = new ChessFigure(FigureName.valueOf(name.toUpperCase(Locale.ROOT)),
                            FigureColor.valueOf(color.toUpperCase(Locale.ROOT)), position);
                    figures.add(chessFigure);
                }
            }
            bufferedReader.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return figures;
    }
    @Override
    public void removeFigureFromFile(ChessFigure chessFigure) {
        String removeChessLine = chessFigure.getColor().toString().toLowerCase(Locale.ROOT) + " " +
                chessFigure.getName().toString().toLowerCase(Locale.ROOT) + " " +
                chessFigure.getPosition().getxPosition() + " " +
                chessFigure.getPosition().getyPosition();
        try {
            String writeTo = "src/main/resources/game/temp.txt";
            File writeToFile = new File(writeTo);
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(writeToFile));
            String readFrom = "src/main/resources/game/init.txt";
            File readFromFile = new File(readFrom);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(readFromFile));

            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
                if (!currentLine.trim().equals(removeChessLine.trim())) {
                    bufferedWriter.write(currentLine + "\n");
                }
            }
            bufferedWriter.close();
            bufferedReader.close();
            Files.move(Paths.get(writeTo), Paths.get(readFrom), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ChessFigure> getAllFigures() {
        List<ChessFigure> figures = new ArrayList<>();
        try {
            InputStream file = getFileInputStream("game/allFigures.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                String color = line.split(" ")[0];
                String name = line.split(" ")[1];
                figures.add(new ChessFigure(FigureName.valueOf(name.toUpperCase(Locale.ROOT)), FigureColor.valueOf(color.toUpperCase(Locale.ROOT))));
            }
            file.close();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return figures;
    }

    @Override
    public boolean clearFiguresFile() {
        try {
            File file = new File("src/main/resources/game/init.txt");
            PrintWriter writer = new PrintWriter(file);
            writer.print("");
            writer.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean writeFigureToFile(String figureLine) {
        try {
            String fileName = "src/main/resources/game/init.txt";
            FileWriter fileWriter = new FileWriter(fileName, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(figureLine);
            bufferedWriter.newLine();
            bufferedWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Image loadImageByPath(String path) {
        InputStream inputStream = getFileInputStream(path);
        Image image = new Image(getFileInputStream(path));
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private InputStream getFileInputStream(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }
}
