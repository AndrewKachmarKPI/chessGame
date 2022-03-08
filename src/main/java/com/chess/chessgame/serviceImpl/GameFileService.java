package com.chess.chessgame.serviceImpl;

import com.chess.chessgame.domain.figures.ChessFigure;
import com.chess.chessgame.domain.figures.Position;
import com.chess.chessgame.enums.FigureColor;
import com.chess.chessgame.enums.FigureName;

import java.io.*;
import java.util.*;

public class GameFileService {
    public static List<ChessFigure> getFiguresFromInitFile() {
        List<ChessFigure> figures = new ArrayList<>();
        File file = new File("D:\\PROJECTS\\chessGame\\src\\main\\resources\\game\\init.txt");
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bufferedReader.readLine()) != null && figures.size() <= 10) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return figures;
    }
    public static List<ChessFigure> getAllFigures() {
        List<ChessFigure> figures = new ArrayList<>();
        try {
            File file = new File("D:\\PROJECTS\\chessGame\\src\\main\\resources\\game\\allFigures.txt");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                String color = line.split(" ")[0];
                String name = line.split(" ")[1];
                figures.add(new ChessFigure(FigureName.valueOf(name.toUpperCase(Locale.ROOT)), FigureColor.valueOf(color.toUpperCase(Locale.ROOT))));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return figures;
    }
    public static boolean clearFiguresFile(){
        try {
            File file = new File("D:\\PROJECTS\\chessGame\\src\\main\\resources\\game\\init.txt");
            PrintWriter writer = new PrintWriter(file);
            writer.print("");
            writer.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean writeFigureToFile(String figureLine){
        try {
            String fileName = "D:\\PROJECTS\\chessGame\\src\\main\\resources\\game\\init.txt";
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
}
