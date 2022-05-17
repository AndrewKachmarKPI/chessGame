package com.chess.chessgame.serviceImpl;

import com.chess.chessgame.domain.figures.ChessFigure;
import com.chess.chessgame.domain.figures.Position;
import com.chess.chessgame.enums.FigureColor;
import com.chess.chessgame.enums.FigureName;
import com.chess.chessgame.services.GameFileService;
import com.chess.chessgame.services.GameService;
import javafx.scene.image.Image;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class GameFileServiceImpl implements GameFileService {
    private static final GameService gameService = new GameServiceImpl();

    @Override
    public List<ChessFigure> getFiguresFromFile(String fileName) {
        List<ChessFigure> figures = new ArrayList<>();
        File file = new File(System.getProperty("user.dir") + "\\" + fileName);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bufferedReader.readLine()) != null) { //&& figures.size() <= 10
                line = line.trim();
                if (!line.isEmpty()) {
                    String color = line.split(" ")[0];
                    String name = line.split(" ")[1];
                    Position position = new Position(Integer.parseInt(line.split(" ")[2]), Integer.parseInt(line.split(" ")[3]));

                    ChessFigure chessFigure = gameService.createChessFigure(position, FigureName.valueOf(name.toUpperCase(Locale.ROOT)),
                            FigureColor.valueOf(color.toUpperCase(Locale.ROOT)));
                    figures.add(chessFigure);
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return figures;
    }

    @Override
    public void removeFigureFromFile(ChessFigure chessFigure, String fileName) {
        String removeChessLine = chessFigure.getColor().toString().toLowerCase(Locale.ROOT) + " " +
                chessFigure.getName().toString().toLowerCase(Locale.ROOT) + " " +
                chessFigure.getPosition().getxPosition() + " " +
                chessFigure.getPosition().getyPosition();
        try {
            String writeTo = System.getProperty("user.dir") + "\\temp.txt";
            File writeToFile = new File(writeTo);
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(writeToFile));
            String readFrom = System.getProperty("user.dir") + "\\" + fileName;
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
            File file = new File(System.getProperty("user.dir") + "\\allFigures.txt");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                String color = line.split(" ")[0];
                String name = line.split(" ")[1];
                figures.add(gameService.createChessFigure(new Position(), FigureName.valueOf(name.toUpperCase(Locale.ROOT)), FigureColor.valueOf(color.toUpperCase(Locale.ROOT))));
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return figures;
    }

    @Override
    public boolean clearFiguresFile(String fileName) {
        try {
            File file = new File(System.getProperty("user.dir") + "\\" + fileName);
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
    public boolean writeFigureToFile(String fileName, ChessFigure chessFigure) {
        try {
            FileWriter fileWriter = new FileWriter(System.getProperty("user.dir") + "\\" + fileName, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(getFigurePath(chessFigure));
            bufferedWriter.newLine();
            bufferedWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean writeFigureToFile(String fileName, ChessFigure chessFigure, String... args) {
        try {
            FileWriter fileWriter = new FileWriter(System.getProperty("user.dir") + "\\" + fileName, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(getFigurePath(chessFigure) + "->" + Arrays.toString(args));
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

    @Override
    public void createWorkingFiles() throws IOException {
        File initFile = new File(System.getProperty("user.dir") + "\\init.txt");
        if (!initFile.exists() && initFile.createNewFile()) {
            String defaultChessPosition = "white king 0 0\n" + "white queen 5 1\n" + "white rook 2 5\n" + "white bishop 3 7\n" + "white knight 5 5\n" + "black king 7 7\n" + "black queen 1 5\n" + "black rook 3 1\n" + "black bishop 7 3\n";
            writeInitialFiles("init.txt", defaultChessPosition);
        }else{
            deleteWorkingFiles();
            createWorkingFiles();
        }
        File allFiguresTxt = new File(System.getProperty("user.dir") + "\\allFigures.txt");
        if (!allFiguresTxt.exists() && allFiguresTxt.createNewFile()) {
            String defaultChessPosition = "white king\n" + "white queen\n" + "white rook\n" + "white bishop\n" + "white knight\n" + "black king\n" + "black queen\n" + "black rook\n" + "black bishop\n" + "black knight\n";
            writeInitialFiles("allFigures.txt", defaultChessPosition);
        } else {
            deleteWorkingFiles();
            createWorkingFiles();
        }
    }

    @Override
    public void createDefaultGameFile() throws IOException {
        File initFile = new File(System.getProperty("user.dir") + "\\init.txt");
        if (!initFile.exists() && initFile.createNewFile()) {
            String defaultChessPosition = "white king 0 0\n" + "white queen 5 1\n" + "white rook 2 5\n" + "white bishop 3 7\n" + "white knight 5 5\n" + "black king 7 7\n" + "black queen 1 5\n" + "black rook 3 1\n" + "black bishop 7 3\n";
            writeInitialFiles("init.txt", defaultChessPosition);
        }else {
            File file = new File(System.getProperty("user.dir") + "\\init.txt");
            if(file.delete()){
                createDefaultGameFile();
            }
        }
    }

    @Override
    public boolean deleteWorkingFiles() {
        File initFile = new File(System.getProperty("user.dir") + "\\init.txt");
        File allFiguresTxt = new File(System.getProperty("user.dir") + "\\allFigures.txt");
        return initFile.delete() && allFiguresTxt.delete();
    }

    @Override
    public String getFigurePath(ChessFigure chessFigure) {
        return chessFigure.getColor().toString().toLowerCase(Locale.ROOT) + " " +
                chessFigure.getName().toString().toLowerCase(Locale.ROOT) + " " +
                chessFigure.getPosition().getyPosition() + " " +
                chessFigure.getPosition().getxPosition();
    }

    @Override
    public boolean saveResultFile(Map<ChessFigure, List<ChessFigure>> chessFigureListMap) throws IOException {
        boolean isSaved = false;
        String fileIdentifier = UUID.randomUUID().toString().split("-")[0];
        File resultFile = new File(System.getProperty("user.dir") + "\\" + "game-result-" + fileIdentifier + ".txt");
        if (resultFile.createNewFile()) {
            chessFigureListMap.forEach((chessFigure, chessFigures) -> {
                chessFigure.increasePosition();
                writeFigureToFile("game-result-" + fileIdentifier + ".txt", chessFigure, getFormattedFigureListPath(chessFigures));
            });
            isSaved = true;
        }
        return isSaved;
    }

    private String getFormattedFigureListPath(List<ChessFigure> chessFigures) {
        StringBuilder stringBuilder = new StringBuilder("[");
        chessFigures.forEach(chessFigure -> stringBuilder.append(getFigurePath(chessFigure)).append("|"));
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    private void writeInitialFiles(String fileName, String defaultData) {
        try {
            File file = new File(System.getProperty("user.dir") + "\\" + fileName);
            PrintWriter writer = new PrintWriter(file);
            writer.print(defaultData);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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

    @Override
    public boolean gameFileValidator(String fileName) {
        List<String> names = new ArrayList<>();
        names.add("king");
        names.add("queen");
        names.add("rook");
        names.add("bishop");
        names.add("knight");
        List<String> colors = new ArrayList<>();
        colors.add("white");
        colors.add("black");

        boolean isValid = true;
        File file = new File(System.getProperty("user.dir") + "\\" + fileName);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            int count = 0;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if(count>10){
                    isValid = false;
                    break;
                }
                if (line.split(" ").length != 4) {
                    isValid = false;
                    break;
                }
                if (!colors.contains(line.split(" ")[0])) {
                    isValid = false;
                    break;
                }
                if (!names.contains(line.split(" ")[1])) {
                    isValid = false;
                    break;
                }

                try {
                    int posX = Integer.parseInt(line.split(" ")[2]);
                    if (posX < 0 || posX > 7) {
                        isValid = false;
                        break;
                    }
                    int posY = Integer.parseInt(line.split(" ")[3]);
                    if (posY < 0 || posY > 7) {
                        isValid = false;
                        break;
                    }
                } catch (NumberFormatException e) {
                    isValid = false;
                    break;
                }
                count++;
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isValid;
    }
}
