package com.chess.chessgame.serviceImpl;

import com.chess.chessgame.domain.figures.ChessFigure;
import com.chess.chessgame.domain.figures.Position;
import com.chess.chessgame.enums.FigureColor;
import com.chess.chessgame.enums.FigureName;
import com.chess.chessgame.services.GameFileService;
import com.chess.chessgame.services.GameService;
import javafx.scene.image.Image;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class GameFileServiceImpl implements GameFileService {
    private static final GameService gameService = new GameServiceImpl();

    @Override
    public List<ChessFigure> getFiguresFromFile(String fileName) {
        List<ChessFigure> figures = new ArrayList<>();
        File file = new File(fileName);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
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

            File readFromFile = new File(fileName);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(readFromFile));

            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
                if (!currentLine.trim().equals(removeChessLine.trim())) {
                    bufferedWriter.write(currentLine + "\n");
                }
            }
            bufferedWriter.close();
            bufferedReader.close();
            Files.move(Paths.get(writeTo), Paths.get(fileName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean clearFiguresFile(String fileName) {
        try {
            File file = new File(fileName);
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
    public void writeFigureToFile(String fileName, ChessFigure chessFigure) {
        try {
            FileWriter fileWriter = new FileWriter(fileName, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(getFigurePath(chessFigure));
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeTextToFile(String fileName, String text) {
        try {
            Path path = Paths.get(fileName);
            BufferedWriter bufferedWriter = Files.newBufferedWriter(path,StandardCharsets.UTF_8);
            bufferedWriter.write(text + "\n");
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeFigureToFile(String fileName, ChessFigure chessFigure, String... args) {
        try {
            FileWriter fileWriter = new FileWriter(fileName, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(getFigurePathForPrint(chessFigure) + " -> " + Arrays.toString(args));
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        } else {
            File file = new File(System.getProperty("user.dir") + "\\init.txt");
            if (file.delete()) {
                createDefaultGameFile();
            }
        }
    }

    @Override
    public void deleteWorkingFiles() {
        File initFile = new File(System.getProperty("user.dir") + "\\init.txt");
        initFile.delete();
    }

    @Override
    public String getFigurePathForPrint(ChessFigure chessFigure) {
        StringBuilder chessPath = new StringBuilder(appendChessIcon(chessFigure) + " ");
        chessPath.append(chessFigure.getColor().toString().toLowerCase(Locale.ROOT)).append(" ")
                .append(chessFigure.getName().toString().toLowerCase(Locale.ROOT));
        int maxSize = 7;
        for (int i = 0; i < maxSize - chessFigure.getName().toString().length(); i++) {
            chessPath.append(" ");
        }
        chessPath.append(chessFigure.getPosition().getyPosition()).append(" ")
                .append(chessFigure.getPosition().getxPosition());
        return chessPath.toString();
    }

    @Override
    public String getFigurePath(ChessFigure chessFigure) {
        return chessFigure.getColor().toString().toLowerCase(Locale.ROOT) + " " +
                chessFigure.getName().toString().toLowerCase(Locale.ROOT) + " " +
                chessFigure.getPosition().getyPosition() + " " +
                chessFigure.getPosition().getxPosition();
    }

    @Override
    public boolean saveResultFile(Map<ChessFigure, List<ChessFigure>> chessFigureListMap, String directory) throws IOException {
        boolean isSaved = false;
        String fileIdentifier = UUID.randomUUID().toString().split("-")[0];
        File resultFile = new File(directory + "\\" + "game-result-" + fileIdentifier + ".txt");
        if (resultFile.createNewFile()) {
            String fileName = directory + "\\" + "game-result-" + fileIdentifier + ".txt";
            StringBuilder stringBuilder = new StringBuilder("Figure attacks!\n");
            chessFigureListMap.forEach((chessFigure, chessFigures) -> {
                stringBuilder.append(getFigurePathForPrint(chessFigure))
                        .append(" -> ")
                        .append(getFormattedFigureListPath(chessFigures)).append("\n");
            });
            stringBuilder.append("\nTotal:").append(chessFigureListMap.keySet().size()).append(" attacks");
            writeTextToFile(fileName, stringBuilder.toString());
            isSaved = true;
        }
        return isSaved;
    }

    private String appendChessIcon(ChessFigure chessFigure) {
        String ico = "";
        switch (chessFigure.getName()) {
            case KING: {
                if (chessFigure.getColor() == FigureColor.BLACK) {
                    ico = "♚";
                } else {
                    ico = "♔";
                }
                break;
            }
            case BISHOP: {
                if (chessFigure.getColor() == FigureColor.BLACK) {
                    ico = "♝";
                } else {
                    ico = "♗";
                }
                break;
            }
            case KNIGHT: {
                if (chessFigure.getColor() == FigureColor.BLACK) {
                    ico = "♞";
                } else {
                    ico = "♘";
                }
                break;
            }
            case QUEEN: {
                if (chessFigure.getColor() == FigureColor.BLACK) {
                    ico = "♛";
                } else {
                    ico = "♕";
                }
                break;
            }
            case ROOK: {
                if (chessFigure.getColor() == FigureColor.BLACK) {
                    ico = "♜";
                } else {
                    ico = "♖";
                }
                break;
            }
        }
        return ico;
    }

    private String getFormattedFigureListPath(List<ChessFigure> chessFigures) {
        StringBuilder stringBuilder = new StringBuilder();
        AtomicInteger k = new AtomicInteger();
        chessFigures.forEach(chessFigure -> {
            stringBuilder.append(getFigurePathForPrint(chessFigure));
            k.getAndIncrement();
            if (k.get() != chessFigures.size()) {
                stringBuilder.append("|");
            }
        });
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
        File file = new File(fileName);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            int count = 0;
            List<Position> positions = new ArrayList<>();
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                if (count > 10) {
                    isValid = false;
                    break;
                }
                if (line.split(" ").length != 4) {
                    isValid = false;
                    break;
                }
                if (!colors.contains(line.split(" ")[0]) || !names.contains(line.split(" ")[1])) {
                    isValid = false;
                    break;
                }

                try {
                    int posX = Integer.parseInt(line.split(" ")[2]);
                    int posY = Integer.parseInt(line.split(" ")[3]);
                    if ((posX < 0 || posX > 7) || (posY < 0 || posY > 7)) {
                        isValid = false;
                        break;
                    }
                    positions.add(new Position(posX, posY));
                } catch (NumberFormatException e) {
                    isValid = false;
                    break;
                }
                count++;
            }
            for (Position pos : positions) {
                if (Collections.frequency(positions, pos) > 1) {
                    isValid = false;
                    break;
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isValid;
    }

    @Override
    public String getFileContent(String fileName) {
        StringBuilder fileContent = new StringBuilder();
        File file = new File(fileName);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (!line.isEmpty()) {
                    fileContent.append(line.trim()).append("\n");
                }
            }
            bufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent.toString();
    }
}
