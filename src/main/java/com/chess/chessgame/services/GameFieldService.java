package com.chess.chessgame.services;

import com.chess.chessgame.domain.board.SelectedCells;
import com.chess.chessgame.domain.figures.*;
import com.chess.chessgame.enums.FigureColor;
import com.chess.chessgame.enums.FigureName;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class GameFieldService {
    public static Group rootGroup = new Group();
    public static Group borderPanesGroup = new Group();
    public static boolean isCellSelected = false;
    public static Color selectedCellColor;
    public static List<SelectedCells> selectedCells = new ArrayList<>();
    public static boolean gameStarted = false;

    public static Scene createGameScene() {
        BorderPane borderPane = new BorderPane();
        createGameBoard();
        borderPane.setCenter(borderPanesGroup);
        borderPane.setRight(createFormsModule());

        rootGroup.getChildren().add(borderPane);
        return new Scene(rootGroup, 1200, 1000, Color.GRAY);
    }

    public static void createGameBoard() {
        paintGameBoard(borderPanesGroup);
        paintBorders(borderPanesGroup, 0, 80);
        paintBorders(borderPanesGroup, 0, 900);
        paintBorders(borderPanesGroup, 80, 0);
        paintBorders(borderPanesGroup, 900, 0);
    }

    public static VBox createFormsModule() {
        VBox vBox = new VBox();
        vBox.getChildren().add(createButtons());
        for (int i = 0; i < 10; i++) {
            vBox.getChildren().add(createComboBox(i));
        }
        return vBox;
    }


    public static HBox createButtons() {
        EventHandler<MouseEvent> onStartGame = GameFieldService::onStartGame;
        Button startGame = new Button("Start game");
        startGame.addEventHandler(MouseEvent.MOUSE_CLICKED, onStartGame);
//        startGame.setStyle("-fx-border-color: #04AA6D; -fx-color-label-visible: green; -fx-background-color: white; -fx-cursor: hand");
        EventHandler<MouseEvent> onClearField = GameFieldService::onClearField;
        Button clearField = new Button("Clear field");
        clearField.addEventHandler(MouseEvent.MOUSE_CLICKED, onClearField);

        Button randomPosition = new Button("Random position");
        HBox hBox = new HBox(10, startGame, clearField, randomPosition);
        hBox.setPadding(new Insets(20, 0, 0, 20));
//        hBox.getStylesheets().add("css/chessGame.css");
        return hBox;
    }

    public static void paintGameBoard(Group group) {
        boolean isSecond = false;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                BorderPane borderPane = new BorderPane();
                borderPane.setMinWidth(100);
                borderPane.setMinHeight(100);
                borderPane.setLayoutX((j * 100) + 100);
                borderPane.setLayoutY((i * 100) + 100);
                Rectangle rectangle = new Rectangle();
                rectangle.setWidth(100);
                rectangle.setHeight(100);
                if (isSecond) {
                    rectangle.setFill(Color.YELLOWGREEN);
                } else {
                    rectangle.setFill(Color.ANTIQUEWHITE);
                }
                borderPane.setId("BorderPane-" + i + j);
                borderPane.setStyle("-fx-cursor: hand;");
                borderPane.getChildren().add(rectangle);

                EventHandler<MouseEvent> selectFigure = GameFieldService::onHoverFigure;
                EventHandler<MouseEvent> unselectFigure = GameFieldService::onUnHooverFigure;
                borderPane.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, selectFigure);
                borderPane.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, unselectFigure);
                group.getChildren().add(borderPane);
                isSecond = !isSecond;
            }
            isSecond = !isSecond;
        }
    }

    public static void paintBorders(Group group, int x, int y) {
        if (y != 0) {
            for (int i = 0; i < 8; i++) {
                StackPane stack = new StackPane();
                stack.setLayoutX((i * 100) + 100);
                stack.setLayoutY(y);
                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.WHITE);
                rectangle.setWidth(100);
                rectangle.setHeight(20);
                Text text = new Text(String.valueOf(i + 1));
                stack.getChildren().addAll(rectangle, text);
                group.getChildren().add(stack);
            }
        }
        if (x != 0) {
            for (int i = 0; i < 8; i++) {
                StackPane stack = new StackPane();
                stack.setLayoutX(x);
                stack.setLayoutY((i * 100) + 100);
                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.WHITE);
                rectangle.setWidth(20);
                rectangle.setHeight(100);
                Text text = new Text(String.valueOf(i + 1));
                stack.getChildren().addAll(rectangle, text);
                group.getChildren().add(stack);
            }
        }
    }

    public static void setFigureOnBoard(ChessFigure chessFigure) {
        try {
            ImageView imageView = loadFigureImage(chessFigure.getColor(), chessFigure.getName());
            imageView.setId(chessFigure.getColor().toString().toUpperCase(Locale.ROOT) + "-" + chessFigure.getName());
            imageView.setX(50);
            imageView.setY(50);

            List<BorderPane> borderPanes = getAllBorderPanes();
            for (BorderPane pane : borderPanes) {
                String paneId = "BorderPane-" + chessFigure.getPosition().getyPosition() + "" + chessFigure.getPosition().getxPosition();
                if (!isCellOccupied(pane) && pane.getId().equals(paneId)) {
                    pane.setCenter(imageView);
                    break;
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean isCellOccupied(BorderPane borderPane) {
        AtomicBoolean isUsed = new AtomicBoolean(false);
        borderPane.getChildren().forEach(node -> {
            if (node instanceof ImageView) {
                isUsed.set(true);
            }
        });
        return isUsed.get();
    }

    public static List<BorderPane> getAllBorderPanes() {
        List<BorderPane> borderPanes = new ArrayList<>();
        borderPanesGroup.getChildren().forEach(node -> {
            if (node instanceof BorderPane) {
                borderPanes.add((BorderPane) node);
            }
        });
        return borderPanes;
    }

    public static Rectangle getRectangleOfBorderPane(BorderPane borderPane) {
        AtomicReference<Rectangle> rectangle = new AtomicReference<>(new Rectangle());
        borderPane.getChildren().forEach(content -> {
            if (content instanceof Rectangle) {
                rectangle.set((Rectangle) content);
            }
        });
        return rectangle.get();
    }

    public static ImageView getImageOfBorderPane(BorderPane borderPane) {
        AtomicReference<ImageView> rectangle = new AtomicReference<>(new ImageView());
        borderPane.getChildren().forEach(content -> {
            if (content instanceof ImageView) {
                rectangle.set((ImageView) content);
            }
        });
        return rectangle.get();
    }

    public static ImageView loadFigureImage(FigureColor figureColor, FigureName figureName) throws FileNotFoundException {
        String path = "D:\\PROJECTS\\chessGame\\src\\main\\resources\\images\\" + figureColor.toString().toLowerCase(Locale.ROOT)
                + figureName.toString().substring(0, 1).toUpperCase(Locale.ROOT)
                + figureName.toString().substring(1).toLowerCase(Locale.ROOT) + ".png";
        File file = new File(path);
        Image image = new Image(new FileInputStream(file));
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        return imageView;
    }

    public static BorderPane findBorderPaneById(String borderPaneId) {
        List<BorderPane> borderPanes = getAllBorderPanes();
        return borderPanes.stream().filter(borderPane -> borderPane.getId().equals(borderPaneId)).findFirst().orElse(new BorderPane());
    }

    public static void paintFigurePath(int[][] matrix) {
        int k = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (matrix[i][j] == 1) {
                    paintRectangle(i, j, Color.WHITESMOKE);
                    k++;
                }
                if (matrix[i][j] == 10) {
                    paintRectangle(i, j, Color.RED);
                    k++;
                }
            }
        }
    }

    public static void paintRectangle(int x, int y, Color color) {
        List<BorderPane> borderPanes = getAllBorderPanes();
        borderPanes.forEach(borderPane -> {
            String borderPaneId = "BorderPane-" + x + "" + y;
            if (borderPane.getId().equals(borderPaneId)) {
                Rectangle rectangle = getRectangleOfBorderPane(borderPane);
                selectedCells.add(new SelectedCells((Color) rectangle.getFill(), new Position(x, y)));
                rectangle.setFill(color);
            }
        });
    }

    public static void unPaintRectangle() {
        selectedCells.forEach(selected -> {
            List<BorderPane> borderPanes = getAllBorderPanes();
            borderPanes.forEach(borderPane -> {
                String borderPaneId = "BorderPane-" + selected.getPosition().getxPosition() + "" + selected.getPosition().getyPosition();
                if (borderPane.getId().equals(borderPaneId)) {
                    Rectangle rectangle = getRectangleOfBorderPane(borderPane);
                    rectangle.setFill(selected.getColor());
                }
            });
        });
    }

    public static void clearBoard() {
        List<BorderPane> borderPanes = getAllBorderPanes();
        for (BorderPane pane : borderPanes) {
            if (isCellOccupied(pane)) {
                pane.setCenter(null);
            }
        }
    }


    public static HBox createComboBox(int id) {
        ObservableList<String> figureNameTypes = FXCollections.observableArrayList(
                FigureName.KING.toString(),
                FigureName.QUEEN.toString(),
                FigureName.BISHOP.toString(),
                FigureName.ROOK.toString(),
                FigureName.KNIGHT.toString());
        ComboBox<String> figureNameBox = new ComboBox<>(figureNameTypes);
        figureNameBox.setValue(FigureName.KING.toString());

        ObservableList<String> figureColors = FXCollections.observableArrayList(FigureColor.BLACK.toString(), FigureColor.WHITE.toString());
        ComboBox<String> figureColorsBox = new ComboBox<>(figureColors);
        figureColorsBox.setValue(FigureColor.BLACK.toString());

        TextField xPosition = new TextField();
        xPosition.setPrefWidth(30);
        xPosition.setAlignment(Pos.CENTER);
        xPosition.setPromptText("X");
        xPosition.setText("0");
        xPosition.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                xPosition.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (newValue.equals("9") || newValue.equals("8")) {
                xPosition.setText("");
            }
        });
        EventHandler<KeyEvent> xkeyEvent = keyEvent1 -> xPosition.setText("");
        xPosition.addEventHandler(KeyEvent.KEY_PRESSED, xkeyEvent);

        TextField yPosition = new TextField();
        yPosition.setPrefWidth(30);
        yPosition.setAlignment(Pos.CENTER);
        yPosition.setPromptText("Y");
        yPosition.setText("0");
        yPosition.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                yPosition.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (newValue.equals("9") || newValue.equals("8")) {
                yPosition.setText("");
            }
        });
        EventHandler<KeyEvent> ykeyEvent = keyEvent1 -> yPosition.setText("");
        yPosition.addEventHandler(KeyEvent.KEY_PRESSED, ykeyEvent);

        Button button = new Button("ADD");
        button.setOnAction(actionEvent -> {
            ChessFigure chessFigure = new ChessFigure();
            chessFigure.setPosition(new Position(Integer.parseInt(xPosition.getText()), Integer.parseInt(yPosition.getText())));
            chessFigure.setName(FigureName.valueOf(figureNameBox.getValue()));
            chessFigure.setColor(FigureColor.valueOf(figureColorsBox.getValue()));
            GameService.writeFigureToFile(chessFigure);
        });
        HBox hBox = new HBox(10, figureNameBox, figureColorsBox, xPosition, yPosition, button);
        hBox.setPadding(new Insets(20, 0, 0, 20));
        hBox.setId("ADD-FORM-" + id);
        return hBox;
    }

    //ACTIONS
    public static void onHoverFigure(MouseEvent e) {
        if (!isCellSelected) {
            BorderPane borderPane = findBorderPaneById(((BorderPane) e.getSource()).getId());
            System.out.println(borderPane);
            if (borderPane.getCenter() instanceof ImageView) {
                isCellSelected = true;
                Rectangle rectangle = getRectangleOfBorderPane(borderPane);
                selectedCellColor = (Color) rectangle.getFill();
                rectangle.setFill(Color.GREEN);

                Position position = new Position(Integer.parseInt(borderPane.getId().split("-")[1].split("")[0]),
                        Integer.parseInt(borderPane.getId().split("-")[1].split("")[1]));
                String figureId = getImageOfBorderPane(borderPane).getId();
                ChessFigure chessFigure = new ChessFigure(FigureName.valueOf(figureId.split("-")[1]), FigureColor.valueOf(figureId.split("-")[0]), position);
                int[][] figureTrajectory = GameService.getFigureTrajectory(chessFigure);
                paintFigurePath(figureTrajectory);
                System.out.println(figureId);
            }
        }
    }

    public static void onUnHooverFigure(MouseEvent e) {
        if (isCellSelected) {
            BorderPane borderPane = findBorderPaneById(((BorderPane) e.getSource()).getId());
            if (borderPane.getCenter() instanceof ImageView) {
                isCellSelected = false;
                Rectangle rectangle = getRectangleOfBorderPane(borderPane);
                rectangle.setFill(selectedCellColor);
                unPaintRectangle();
            }
        }
    }

    public static void onStartGame(MouseEvent e) {
        gameStarted = true;
        GameService.initGame();
    }

    public static void onClearField(MouseEvent e) {
        if (gameStarted) {
            gameStarted = false;
            isCellSelected = false;
            selectedCells = new ArrayList<>();
            GameService.clearGameField();
            clearBoard();
        }
    }

}
