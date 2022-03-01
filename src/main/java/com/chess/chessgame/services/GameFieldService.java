package com.chess.chessgame.services;

import com.chess.chessgame.domain.board.SelectedCells;
import com.chess.chessgame.domain.figures.*;
import com.chess.chessgame.enums.FigureColor;
import com.chess.chessgame.enums.FigureName;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
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
        VBox vBox = createFormsModule();
        BorderPane.setAlignment(vBox, Pos.CENTER);
        BorderPane.setAlignment(borderPanesGroup, Pos.CENTER);
        borderPane.setCenter(borderPanesGroup);
        borderPane.setBottom(vBox);

        rootGroup.getChildren().add(borderPane);
        Scene scene = new Scene(rootGroup, 1000, 1000, Color.GRAY);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        return scene;
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
//        for (int i = 0; i < 10; i++) {
//            vBox.getChildren().add(createComboBox(i));
//        }
        return vBox;
    }


    public static HBox createButtons() {
        EventHandler<MouseEvent> onStartGame = GameFieldService::onStartGame;
        Button startGame = new Button("Start game");
        startGame.addEventHandler(MouseEvent.MOUSE_CLICKED, onStartGame);
        startGame.getStyleClass().setAll("btn-lg", "btn-success");

        EventHandler<MouseEvent> onClearField = GameFieldService::onClearField;
        Button clearField = new Button("Clear field");
        clearField.addEventHandler(MouseEvent.MOUSE_CLICKED, onClearField);
        clearField.getStyleClass().setAll("btn-lg", "btn-danger");

        EventHandler<MouseEvent> onFigureAttacks = GameFieldService::onFigureAttacks;
        Button figureAttacks = new Button("Figure attacks");
        figureAttacks.addEventHandler(MouseEvent.MOUSE_CLICKED, onFigureAttacks);
        figureAttacks.getStyleClass().setAll("btn-lg", "btn-warning");

        HBox hBox = new HBox(10, startGame, clearField, figureAttacks);
        hBox.setPadding(new Insets(20, 0, 0, 20));
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
                    rectangle.setFill(Color.web("#789655"));
                } else {
                    rectangle.setFill(Color.web("#ecedd1"));
                }
                borderPane.setId("BorderPane-" + i + j);
                if (gameStarted) {
                    borderPane.setCursor(Cursor.HAND);
                }
                borderPane.getChildren().add(rectangle);
                EventHandler<MouseEvent> selectFigure = GameFieldService::onHoverFigure;
                EventHandler<MouseEvent> unselectFigure = GameFieldService::onUnHooverFigure;
                borderPane.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, selectFigure);
                borderPane.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, unselectFigure);
                borderPane.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                    if (mouseEvent.getButton() == MouseButton.SECONDARY && gameStarted) {
                        BorderPane selectedPane = findBorderPaneById(((BorderPane) mouseEvent.getSource()).getId());
                        ContextMenu contextMenu = openAvailableFiguresMenu(selectedPane);
                        contextMenu.show(rectangle, mouseEvent.getScreenX(), mouseEvent.getScreenY());
                    }
                });
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

    public static Image loadImageByPath(String path) {
        Image image = null;
        try {
            File file = new File(path);
            image = new Image(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static ImageView loadFigureImage(FigureColor figureColor, FigureName figureName) {
        String path = "D:\\PROJECTS\\chessGame\\src\\main\\resources\\images\\" + figureColor.toString().toLowerCase(Locale.ROOT)
                + figureName.toString().substring(0, 1).toUpperCase(Locale.ROOT)
                + figureName.toString().substring(1).toLowerCase(Locale.ROOT) + ".png";
        Image image = null;
        try {
            File file = new File(path);
            image = new Image(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        return imageView;
    }

    public static BorderPane findBorderPaneById(String borderPaneId) {
        List<BorderPane> borderPanes = getAllBorderPanes();
        return borderPanes.stream().filter(borderPane -> borderPane.getId().equals(borderPaneId)).findFirst().orElse(new BorderPane());
    }

    public static void paintFigurePath(int[][] matrix) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (matrix[i][j] == 1) {
                    paintRectangle(i, j, Color.web("#f6f87a"));
                }
                if (matrix[i][j] == 10) {
                    paintRectangle(i, j, Color.RED);
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

    public static void openDialogWindow() {
        Map<ChessFigure, List<ChessFigure>> chessFigureListMap = GameService.getPassMap();
        VBox vBox = loadFigureList(chessFigureListMap);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(vBox);
        scrollPane.setPannable(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Figure attacks");
        dialog.setHeaderText(null);
        dialog.setGraphic(null);

        Text headerText = new Text("Figure attacks");
        headerText.setFont(Font.font("Verdana", 20));
        BorderPane borderPane = new BorderPane();
        BorderPane.setAlignment(headerText, Pos.CENTER);
        BorderPane.setAlignment(scrollPane, Pos.CENTER);
        borderPane.setTop(headerText);
        borderPane.setCenter(scrollPane);
        borderPane.setPrefHeight(600);

        ButtonType okButton = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
        DialogPane dialogPane = new DialogPane();
        dialogPane.setContentText("All figures attacks");
        dialogPane.setContent(borderPane);
        dialogPane.getButtonTypes().add(okButton);
        dialog.setDialogPane(dialogPane);

        dialog.show();
    }

    public static VBox loadFigureList(Map<ChessFigure, List<ChessFigure>> chessFigureListMap) {
        VBox figuresList = new VBox();
        chessFigureListMap.forEach((chessFigure, chessFigures) -> {
            if (chessFigures.size() > 0) {
                Label label = new Label(chessFigure.getName() + "- (" + chessFigure.getPosition().getxPosition() + "," + chessFigure.getPosition().getyPosition() + ")");
                ImageView imageView = loadFigureImage(chessFigure.getColor(), chessFigure.getName());

                BorderPane figureBox = new BorderPane();
                BorderPane.setAlignment(label, Pos.CENTER);
                BorderPane.setAlignment(imageView, Pos.CENTER);
                figureBox.setCenter(imageView);
                figureBox.setBottom(label);
                figureBox.setStyle("-fx-border-style: solid; -fx-border-width : 0 0 4; -fx-border-color: #28a745;");

                HBox mainRow = new HBox(10, figureBox, loadNestedFigures(chessFigures));
                mainRow.setStyle("-fx-border-style: solid; -fx-border-width : 0 0 2; -fx-border-color: #gray;");
                mainRow.setPadding(new Insets(10, 10, 10, 10));
                figuresList.getChildren().add(mainRow);
            }
        });
        if (figuresList.getChildren().size() == 0) {
            File file = new File("D:\\PROJECTS\\chessGame\\src\\main\\resources\\images\\sadSmile.png");
            try {
                Text text = new Text("No attacks was found");
                text.setFont(Font.font("Verdana", 20));
                ImageView imageView = new ImageView(new Image(new FileInputStream(file)));
                imageView.setFitWidth(120);
                imageView.setFitHeight(120);
                BorderPane borderPane = new BorderPane();
                BorderPane.setAlignment(imageView, Pos.CENTER);
                BorderPane.setAlignment(text, Pos.CENTER);
                borderPane.setCenter(imageView);
                borderPane.setBottom(text);
                figuresList.getChildren().add(borderPane);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return figuresList;
    }

    public static HBox loadNestedFigures(List<ChessFigure> chessFigures) {
        HBox hBox = new HBox();
        chessFigures.forEach(chessFigure -> {
            BorderPane figureBox = new BorderPane();
            figureBox.setStyle("-fx-border-style: solid; -fx-border-width : 0 0 4; -fx-border-color: red;");
            Label label = new Label(chessFigure.getName() + "- (" + chessFigure.getPosition().getxPosition() + "," + chessFigure.getPosition().getyPosition() + ")");

            ImageView imageView = loadFigureImage(chessFigure.getColor(), chessFigure.getName());
            BorderPane.setAlignment(imageView, Pos.CENTER);
            figureBox.setCenter(imageView);

            BorderPane.setAlignment(label, Pos.CENTER);
            figureBox.setBottom(label);
            hBox.getChildren().add(figureBox);
            hBox.setSpacing(10);
        });
        return hBox;
    }

    public static ContextMenu openAvailableFiguresMenu(BorderPane borderPane) {
        List<ChessFigure> availableFigures = GameService.getAvailableFigures();
        ContextMenu contextMenu = new ContextMenu();
        availableFigures.forEach(chessFigure -> {
            if (!isCellOccupied(borderPane)) {
                ImageView imageView = loadFigureImage(chessFigure.getColor(), chessFigure.getName());
                String label = chessFigure.getColor().toString() + "-" + chessFigure.getName();
                MenuItem menuItem = new MenuItem(label, imageView);

                menuItem.setOnAction(actionEvent -> onSelectContextMenuItem(actionEvent, chessFigure, borderPane));
                contextMenu.getItems().add(menuItem);
            }
        });
        if (contextMenu.getItems().size() == 0) {
            if (isCellOccupied(borderPane)) {
                ImageView imageView = new ImageView(loadImageByPath("D:\\PROJECTS\\chessGame\\src\\main\\resources\\images\\trash.png"));
                imageView.setFitWidth(20);
                imageView.setFitHeight(20);
                MenuItem menuItem = new MenuItem("Remove figure", imageView);
                menuItem.setOnAction(actionEvent -> onDeleteFigureFromBoard(actionEvent, borderPane));
                contextMenu.getItems().add(menuItem);
            } else {
                ImageView imageView = new ImageView(loadImageByPath("D:\\PROJECTS\\chessGame\\src\\main\\resources\\images\\sadSmile.png"));
                imageView.setFitWidth(60);
                imageView.setFitHeight(60);
                MenuItem menuItem = new MenuItem("No available figures for cell", imageView);
                contextMenu.getItems().add(menuItem);
            }
        }
        return contextMenu;
    }

    public static void createHoverEffects(BorderPane borderPane) {
        if (!gameStarted) {
            ImageCursor imageCursor = new ImageCursor(loadImageByPath("D:\\PROJECTS\\chessGame\\src\\main\\resources\\images\\stopCursor.png"));
            borderPane.setCursor(imageCursor);
        } else {
            borderPane.setCursor(Cursor.HAND);
        }
    }

    //ACTIONS
    public static void onHoverFigure(MouseEvent e) {
        if (!isCellSelected) {
            BorderPane borderPane = findBorderPaneById(((BorderPane) e.getSource()).getId());
            createHoverEffects(borderPane);
            if (borderPane.getCenter() instanceof ImageView) {
                isCellSelected = true;
                Rectangle rectangle = getRectangleOfBorderPane(borderPane);
                selectedCellColor = (Color) rectangle.getFill();
                rectangle.setFill(Color.web("#bacd33"));

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
            createHoverEffects(borderPane);
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

    public static void onFigureAttacks(MouseEvent e) {
        if (gameStarted) {
            openDialogWindow();
        }
    }

    public static void onClearField(MouseEvent e) {
        if (gameStarted) {
            gameStarted = false;
            isCellSelected = false;
            selectedCells = new ArrayList<>();
            GameService.clearGameField();
            clearBoard();
            gameStarted = true;
            GameService.initGame();
        }
    }

    public static void onSelectContextMenuItem(ActionEvent e, ChessFigure chessFigure, BorderPane borderPane) {
        Position position = new Position();
        if (borderPane != null) {
            position.setxPosition(Integer.parseInt(borderPane.getId().split("-")[1].split("")[0]));
            position.setyPosition(Integer.parseInt(borderPane.getId().split("-")[1].split("")[1]));
        }
        chessFigure.setPosition(position);
        GameService.writeFigureToFile(chessFigure);
        GameService.initGame();
        gameStarted = true;
    }

    public static void onDeleteFigureFromBoard(ActionEvent actionEvent, BorderPane borderPane) {
        Position position = new Position(Integer.parseInt(borderPane.getId().split("-")[1].split("")[1]),
                Integer.parseInt(borderPane.getId().split("-")[1].split("")[0]));
        GameService.removeFigure(position);
        GameService.initGame();
        gameStarted = true;
    }
}
