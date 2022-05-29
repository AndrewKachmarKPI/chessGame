package com.chess.chessgame.serviceImpl;

import com.chess.chessgame.domain.figures.ChessFigure;
import com.chess.chessgame.domain.figures.Position;
import com.chess.chessgame.enums.FigureColor;
import com.chess.chessgame.enums.FigureName;
import com.chess.chessgame.services.GameFileService;
import com.chess.chessgame.services.GameService;
import javafx.scene.image.Image;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Клас для обробки роботи з файлами
 */
public class GameFileServiceImpl implements GameFileService {
    private static final GameService gameService = new GameServiceImpl();

    /**
     * Зчитування вмісту файлу та парсинг в об’єкт ChessFigure
     * @param fileName імя файлу
     * @return список фігур файлу
     */
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

    /**
     * Видалення запису про фігуру з файлу
     * @param chessFigure об'єкт фігури
     * @param fileName імя файлу
     */
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

    /**
     * Повне очищення файлу з записами про фігури
     * @param fileName імя файлу
     * @return
     */
    @Override
    public boolean clearFiguresFile(String fileName) {
        boolean isCleared = false;
        try {
            File file = new File(fileName);
            PrintWriter writer = new PrintWriter(file);
            writer.print("");
            writer.close();
            isCleared = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return isCleared;
    }

    /**
     * Додавання запису про фігуру у файл
     * @param fileName імя файлу
     * @param chessFigure об'єкт фігури
     */
    @Override
    public void appendFigureToFile(String fileName, ChessFigure chessFigure) {
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

    /**
     * Дописування тексту у файл
     * @param fileName імя файлу
     * @param text текст для запису
     */
    @Override
    public void writeTextToFile(String fileName, String text) {
        try {
            Path path = Paths.get(fileName);
            BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
            bufferedWriter.write(text);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Отримання зображення фігури по посиланню
     * @param path посилання на зображення
     * @return
     */
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

    /**
     * Створення гри зі стандартним розташуванням фігур
     * @throws IOException
     */
    @Override
    public void createWorkingFiles() throws IOException {
        File initFile = new File(System.getProperty("user.dir") + "\\init.txt");
        if (!initFile.exists() && initFile.createNewFile()) {
            String defaultChessPosition = "white king 0 0\n" + "white queen 5 1\n" + "white rook 2 5\n" +
                    "white bishop 3 7\n" + "white knight 5 5\n" + "black king 7 7\n" +
                    "black queen 1 5\n" + "black rook 3 1\n" + "black bishop 7 3";
            writeTextToFile("init.txt", defaultChessPosition);
        } else {
            deleteWorkingFiles();
            createWorkingFiles();
        }
    }

    /**
     * Видалення файлу зі стандартним розташуванням фігур
     */
    @Override
    public void deleteWorkingFiles() {
        File initFile = new File(System.getProperty("user.dir") + "\\init.txt");
        initFile.delete();
    }

    /**
     * Конвертація фігури у рядок для збереження
     * @param chessFigure об'єкт фігури
     * @return рядок для збереження
     */
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

    /**
     * Отримання форматованого запису про фігуру
     * @param chessFigure об'єкт фігури
     * @return рядок з інформацією про фігуру
     */
    @Override
    public String getFigurePath(ChessFigure chessFigure) {
        return chessFigure.getColor().toString().toLowerCase(Locale.ROOT) + " " +
                chessFigure.getName().toString().toLowerCase(Locale.ROOT) + " " +
                chessFigure.getPosition().getyPosition() + " " +
                chessFigure.getPosition().getxPosition();
    }

    /**
     * Запис результатів гри у файл
     * @param chessFigureListMap Map з усіма атаками фігур
     * @param directory Директорія запису файлу результату
     * @return збережено файл чи ні
     * @throws IOException
     */
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
            stringBuilder.append("\nTotal:").append(chessFigureListMap.keySet().size()).append(" attacks \n");
            writeTextToFile(fileName, stringBuilder.toString());
            isSaved = true;
        }
        return isSaved;
    }

    /**
     * Отримання символа фігури
     * @param chessFigure об'єкт фігури
     * @return символа фігури
     */
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

    /**
     * Отримання форматованого запису списку фігур для збереження
     * @param chessFigures спсисок фігур
     * @return рядок з списком фігур
     */
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

    /**
     * Отримання ресурсних файлів
     * @param fileName назва файлу
     * @return вхідний потік
     */
    private InputStream getFileInputStream(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }

    /**
     * Валідація вхідного файлу користувача
     * @param fileName назва файлу
     * @return валідний файл чи ні
     */
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

    /**
     * Отримання вмісту ігрового файлу
     * @param fileName назва файлу
     * @return рядок з вмістом файлу
     */
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
