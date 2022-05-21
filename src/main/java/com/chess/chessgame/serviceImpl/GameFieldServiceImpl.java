package com.chess.chessgame.serviceImpl;

import com.chess.chessgame.domain.board.GameBoardCell;
import com.chess.chessgame.domain.board.GameBoard;
import com.chess.chessgame.domain.figures.*;
import com.chess.chessgame.enums.FigureColor;
import com.chess.chessgame.enums.FigureName;
import com.chess.chessgame.enums.NotificationStatus;
import com.chess.chessgame.services.GameFieldService;
import com.chess.chessgame.services.GameFileService;
import com.chess.chessgame.services.GameMenuService;
import com.chess.chessgame.services.GameService;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;


public class GameFieldServiceImpl implements GameFieldService {
    private final GameFileService gameFileService;
    private final GameService gameService;
    private final GameBoard gameBoard;
    public Group borderPanesGroup;

    public GameFieldServiceImpl() {
        this.gameFileService = new GameFileServiceImpl();
        this.gameService = new GameServiceImpl();
        this.gameBoard = new GameBoard();
        this.borderPanesGroup = new Group();
    }

    public Group createGameGroup() {
        createGameBoard();
        Group hBox = new Group(createButtons());
        BorderPane.setAlignment(hBox, Pos.TOP_RIGHT);
        BorderPane.setAlignment(borderPanesGroup, Pos.CENTER);
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(borderPanesGroup);
        borderPane.setStyle("-fx-background-color: #312e2b");
        borderPane.setRight(hBox);
        return new Group(borderPane);
    }

    private void createGameBoard() {
        paintGameBoard();
        paintBorders(0, 60);
        paintBorders(0, 720);
        paintBorders(60, 0);
        paintBorders(720, 0);
    }

    private VBox createButtons() {
        EventHandler<MouseEvent> onSavedGameResults = this::onSavedGameResults;
        Button savedGameResults = new Button("Save game");
        ImageView imageView = new ImageView(gameFileService.loadImageByPath("images/save_icon.png"));
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        savedGameResults.setGraphic(imageView);
        savedGameResults.addEventHandler(MouseEvent.MOUSE_CLICKED, onSavedGameResults);
        savedGameResults.getStyleClass().setAll("text", "info", "sm");

        EventHandler<MouseEvent> onUploadFile = this::onUploadChessPosition;
        Button randomChessPosition = new Button("Upload");
        ImageView imageView1 = new ImageView(gameFileService.loadImageByPath("images/upload.png"));
        imageView1.setFitWidth(30);
        imageView1.setFitHeight(30);
        randomChessPosition.setGraphic(imageView1);
        randomChessPosition.addEventHandler(MouseEvent.MOUSE_CLICKED, onUploadFile);
        randomChessPosition.getStyleClass().setAll("text", "success", "sm");

        EventHandler<MouseEvent> onClearField = this::onClearField;
        Button clearField = new Button("Clear field");
        clearField.addEventHandler(MouseEvent.MOUSE_CLICKED, onClearField);
        clearField.getStyleClass().setAll("text", "danger", "sm");

        EventHandler<MouseEvent> onFigureAttacks = this::onFigureAttacks;
        Button figureAttacks = new Button("Show attacks");
        figureAttacks.addEventHandler(MouseEvent.MOUSE_CLICKED, onFigureAttacks);
        figureAttacks.getStyleClass().setAll("text", "warning", "sm");

        Text headerText = gameBoard.getWorkingFileName();
        headerText.setId("fileName");
        headerText.setFill(Color.WHITE);
        headerText.setFont(Font.font("Gill Sans Ultra Bold", 15));

        Rectangle rectangle = new Rectangle();
        rectangle.setWidth(150);
        rectangle.setHeight(40);
        rectangle.setStrokeWidth(2);
        rectangle.setStrokeType(StrokeType.CENTERED);
        rectangle.setStroke(Color.BLACK);
        rectangle.setFill(Color.web("#26211b"));
        BorderPane borderPane = new BorderPane();
        borderPane.setId("fileNamePane");
        borderPane.setMinWidth(120);
        borderPane.setMinHeight(40);
        borderPane.getChildren().add(rectangle);
        borderPane.setCenter(headerText);


        Text fileContent = gameBoard.getWorkingFileContent();
        fileContent.setId("fileContent");
        fileContent.setFill(Color.WHITE);
        fileContent.setFont(Font.font("Gill Sans Ultra Bold", 14));

        Rectangle fileText = new Rectangle();
        fileText.setWidth(150);
        fileText.setHeight(240);
        fileText.setStrokeWidth(2);
        fileText.setStrokeType(StrokeType.CENTERED);
        fileText.setStroke(Color.BLACK);
        fileText.setFill(Color.web("#26211b"));
        BorderPane textPane = new BorderPane();
        textPane.setId("textFilePane");
        textPane.setMinWidth(120);
        textPane.setMinHeight(240);
        textPane.getChildren().add(fileText);
        textPane.setCenter(fileContent);


        VBox vBox1 = new VBox(10, savedGameResults, figureAttacks, clearField);
        VBox vBox2 = new VBox(10, textPane,borderPane,randomChessPosition);
        vBox2.setId("textVbox");
        vBox1.setPadding(new Insets(0,0,120,0));
        VBox vBox = new VBox(10,vBox1,vBox2);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20, 20, 20, 20));
        return vBox;
    }

    private void paintGameBoard() {
        boolean isSecond = false;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                BorderPane borderPane = new BorderPane();
                borderPane.setMinWidth(80);
                borderPane.setMinHeight(80);
                borderPane.setLayoutX((j * 80) + 80);
                borderPane.setLayoutY((i * 80) + 80);
                Rectangle rectangle = new Rectangle();
                rectangle.setWidth(80);
                rectangle.setHeight(80);
                rectangle.setStrokeWidth(2);
                rectangle.setStrokeType(StrokeType.CENTERED);
                rectangle.setStroke(Color.BLACK);
                if (isSecond) {
                    rectangle.setFill(Color.web("#789655"));
                } else {
                    rectangle.setFill(Color.web("#ecedd1"));
                }
                borderPane.setId("BorderPane-" + i + j);
                if (gameBoard.isGameStarted()) {
                    borderPane.setCursor(Cursor.HAND);
                }
                borderPane.getChildren().add(rectangle);
                EventHandler<MouseEvent> selectFigure = this::onHoverFigure;
                EventHandler<MouseEvent> unselectFigure = this::onUnHooverFigure;
                borderPane.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, selectFigure);
                borderPane.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, unselectFigure);
                borderPane.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                    if (mouseEvent.getButton() == MouseButton.SECONDARY && gameBoard.isGameStarted()) {
                        BorderPane selectedPane = findBorderPaneById(((BorderPane) mouseEvent.getSource()).getId());
                        ContextMenu contextMenu = openAvailableFiguresMenu(selectedPane);
                        contextMenu.show(rectangle, mouseEvent.getScreenX(), mouseEvent.getScreenY());
                    }
                });
                borderPanesGroup.getChildren().add(borderPane);
                isSecond = !isSecond;
            }
            isSecond = !isSecond;
        }
    }

    private void paintBorders(int x, int y) {
        char[] characters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        if (y != 0) {
            for (int i = 0; i < 8; i++) {
                StackPane stack = new StackPane();
                stack.setLayoutX((i * 80) + 80);
                stack.setLayoutY(y);
                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.web("26211b"));
                rectangle.setWidth(80);
                rectangle.setHeight(20);
                Text text = new Text(String.valueOf(characters[i]));
                text.setFont(Font.font("Verdana", 15));
                text.setFill(Color.WHITE);
                stack.getChildren().addAll(rectangle, text);
                borderPanesGroup.getChildren().add(stack);
            }
        }
        if (x != 0) {
            for (int i = 0; i < 8; i++) {
                StackPane stack = new StackPane();
                stack.setLayoutX(x);
                stack.setLayoutY((i * 80) + 80);
                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.web("26211b"));
                rectangle.setWidth(20);
                rectangle.setHeight(80);
                Text text = new Text(String.valueOf(i + 1));
                text.setFont(Font.font("Verdana", 15));
                text.setFill(Color.WHITE);
                stack.getChildren().addAll(rectangle, text);
                borderPanesGroup.getChildren().add(stack);
            }
        }
    }

    private void setFigureOnBoard(ChessFigure chessFigure) {
        ImageView imageView = loadFigureImage(chessFigure.getColor(), chessFigure.getName());
        imageView.setId(chessFigure.getColor().toString().toUpperCase(Locale.ROOT) + "-" + chessFigure.getName());
        imageView.setX(50);
        imageView.setY(50);

        List<BorderPane> borderPanes = getAllBorderPanes();
        for (BorderPane pane : borderPanes) {
            String paneId = "BorderPane-" + chessFigure.getPosition().getxPosition() + "" + chessFigure.getPosition().getyPosition();
            if (!isCellOccupied(pane) && pane.getId().equals(paneId)) {
                pane.setCenter(imageView);
                break;
            }
        }
    }

    private boolean isCellOccupied(BorderPane borderPane) {
        AtomicBoolean isUsed = new AtomicBoolean(false);
        borderPane.getChildren().forEach(node -> {
            if (node instanceof ImageView) {
                isUsed.set(true);
            }
        });
        return isUsed.get();
    }

    private List<BorderPane> getAllBorderPanes() {
        List<BorderPane> borderPanes = new ArrayList<>();
        borderPanesGroup.getChildren().forEach(node -> {
            if (node instanceof BorderPane) {
                borderPanes.add((BorderPane) node);
            }
        });
        return borderPanes;
    }

    private Rectangle getRectangleOfBorderPane(BorderPane borderPane) {
        AtomicReference<Rectangle> rectangle = new AtomicReference<>(new Rectangle());
        borderPane.getChildren().forEach(content -> {
            if (content instanceof Rectangle) {
                rectangle.set((Rectangle) content);
            }
        });
        return rectangle.get();
    }

    private ImageView getImageOfBorderPane(BorderPane borderPane) {
        AtomicReference<ImageView> imageView = new AtomicReference<>(new ImageView());
        borderPane.getChildren().forEach(content -> {
            if (content instanceof ImageView) {
                imageView.set((ImageView) content);
            }
        });
        return imageView.get();
    }

    private ImageView loadFigureImage(FigureColor figureColor, FigureName figureName) {
        String path = "images/" + figureColor.toString().toLowerCase(Locale.ROOT) + "-" + figureName.toString().toLowerCase() + ".png";
        return new ImageView(gameFileService.loadImageByPath(path));
    }

    private BorderPane findBorderPaneById(String borderPaneId) {
        return getAllBorderPanes().stream().filter(borderPane -> borderPane.getId().equals(borderPaneId)).findFirst().orElse(new BorderPane());
    }

    private void paintFigurePath(int[][] matrix, FigureColor figureColor) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (matrix[i][j] == 1) {
                    paintRectangle(i, j, Color.web("#f6f87a"), figureColor, false);
                }
                if (matrix[i][j] == 10) {
                    paintRectangle(i, j, Color.RED, figureColor, true);
                }
            }
        }
    }

    private void paintRectangle(int x, int y, Color color, FigureColor figureColor, boolean isAttack) {
        BorderPane borderPane = findBorderPaneById("BorderPane-" + x + "" + y);
        Rectangle rectangle = getRectangleOfBorderPane(borderPane);
        if (isAttack) {
            ImageView imageView = getImageOfBorderPane(borderPane);
            String cellFigureColor = imageView.getId().split("-")[0];
            if (cellFigureColor.equals(figureColor.toString().toUpperCase(Locale.ROOT))) {
                color = (Color) rectangle.getFill();
            }
        }
        gameBoard.getSelectedCells().add(new GameBoardCell((Color) rectangle.getFill(), rectangle));
        rectangle.setFill(color);
    }

    private void unPaintRectangle() {
        gameBoard.getSelectedCells().forEach(selected -> selected.getRectangle().setFill(selected.getColor()));
    }

    private void clearBoard() {
        List<BorderPane> borderPanes = getAllBorderPanes();
        for (BorderPane pane : borderPanes) {
            if (isCellOccupied(pane)) {
                pane.setCenter(null);
            }
        }
    }

    private void removeFigureById(String paneId) {
        List<BorderPane> borderPanes = getAllBorderPanes();
        for (BorderPane pane : borderPanes) {
            if (isCellOccupied(pane) && pane.getId().equals(paneId)) {
                pane.setCenter(null);
            }
        }
    }



    private void openDialogWindow() {
        Map<ChessFigure, List<ChessFigure>> chessFigureListMap = gameService.getAttackMap();
        VBox vBox = loadFigureList(chessFigureListMap);
        vBox.setStyle("-fx-background-color: #312e2b");
        vBox.setAlignment(Pos.CENTER);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(vBox);
        scrollPane.setPannable(true);
        scrollPane.setStyle("-fx-background-color: #312e2b");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Figure attacks");
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setHeaderText(null);
        dialog.setGraphic(null);
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(gameFileService.loadImageByPath("images/mainIcon.png"));

        Text headerText = new Text("Figure attacks");
        headerText.setFill(Color.WHITE);
        headerText.setFont(Font.font("Gill Sans Ultra Bold", 20));

        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: #26211b");
        BorderPane.setAlignment(headerText, Pos.CENTER);
        BorderPane.setAlignment(scrollPane, Pos.CENTER);
        borderPane.setTop(headerText);
        borderPane.setCenter(scrollPane);
        borderPane.getCenter().setStyle("-fx-background-color: #312e2b");
        BorderPane.setMargin(headerText, new Insets(0, 0, 10, 0));

        DialogPane dialogPane = new DialogPane();
        dialogPane.getStyleClass().add("availableDialog");
        dialogPane.setContentText("All figures attacks");
        dialogPane.setContent(borderPane);
        if (!chessFigureListMap.isEmpty() && chessFigureListMap.size() > 4) {
            dialogPane.setPrefHeight(600);
        }
        dialogPane.setStyle("-fx-background-color: #26211b");
        ButtonType okButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialogPane.getButtonTypes().add(okButton);
        dialog.setHeight(600);
        dialog.setDialogPane(dialogPane);
        dialog.setResizable(false);
        dialog.show();
    }

    private VBox getLabelBox(ChessFigure chessFigure) {
        Label nameLabel = new Label(chessFigure.getName().toString());
        nameLabel.setTextFill(Color.WHITE);
        nameLabel.setStyle("-fx-font-weight: bold");
        char[] characters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        Label positionLabel = new Label("(" + (chessFigure.getPosition().getxPosition() + 1) + "," + characters[chessFigure.getPosition().getyPosition()] + ")");
        positionLabel.setTextFill(Color.WHITE);
        positionLabel.setStyle("-fx-font-weight: bold");
        VBox labelBox = new VBox(nameLabel, positionLabel);
        labelBox.setAlignment(Pos.CENTER);
        return labelBox;
    }

    private VBox loadFigureList(Map<ChessFigure, List<ChessFigure>> chessFigureListMap) {
        VBox figuresList = new VBox();
        figuresList.setStyle("-fx-background-color: #312e2b");


        chessFigureListMap.forEach((chessFigure, chessFigures) -> {
            if (chessFigures.size() > 0) {
                VBox labelBox = getLabelBox(chessFigure);
                ImageView imageView = loadFigureImage(chessFigure.getColor(), chessFigure.getName());
                ImageView attackArrow = new ImageView(gameFileService.loadImageByPath("images/attackArrow1.png"));
                attackArrow.getStyleClass().add("attackIcon");

                StackPane attackArrowPane = new StackPane(attackArrow);

                BorderPane figureBox = new BorderPane();
                figureBox.setPrefHeight(80);
                figureBox.setPrefWidth(70);
                BorderPane.setAlignment(labelBox, Pos.CENTER);
                BorderPane.setAlignment(imageView, Pos.CENTER);
                figureBox.setCenter(imageView);
                figureBox.setBottom(labelBox);
                figureBox.setStyle("-fx-background-color: #789655");

                HBox mainRow = new HBox(10, figureBox, attackArrowPane, loadNestedFigures(chessFigures));
                mainRow.setStyle("-fx-border-style: solid; -fx-border-width : 0 0 2; -fx-border-color: white;");
                mainRow.setPadding(new Insets(10, 10, 10, 10));
                figuresList.getChildren().add(mainRow);
            }
        });
        if (figuresList.getChildren().size() == 0) {
            Text text = new Text("No attacks was found");
            text.setFill(Color.WHITE);
            text.setFont(Font.font("Gill Sans Ultra Bold", 20));
            ImageView imageView = new ImageView(gameFileService.loadImageByPath("images/mainIcon.png"));
            imageView.setFitWidth(120);
            imageView.setFitHeight(120);
            BorderPane borderPane = new BorderPane();
            borderPane.setStyle("-fx-background-color: #312e2b");
            borderPane.setMinHeight(Region.USE_PREF_SIZE);
            borderPane.setMinWidth(Region.USE_PREF_SIZE);
            BorderPane.setAlignment(imageView, Pos.CENTER);
            BorderPane.setAlignment(text, Pos.CENTER);
            borderPane.setCenter(imageView);
            borderPane.setBottom(text);
            figuresList.getChildren().add(borderPane);
        }
        return figuresList;
    }

    private HBox loadNestedFigures(List<ChessFigure> chessFigures) {
        HBox hBox = new HBox();
        hBox.setStyle("-fx-background-color: #dc3545bd");
        hBox.setSpacing(20);
        chessFigures.forEach(chessFigure -> {
            VBox labelBox = getLabelBox(chessFigure);
            BorderPane figureBox = new BorderPane();
            figureBox.setPrefHeight(80);
            figureBox.setPrefWidth(70);

            ImageView imageView = loadFigureImage(chessFigure.getColor(), chessFigure.getName());
            BorderPane.setAlignment(imageView, Pos.CENTER);
            figureBox.setCenter(imageView);

            BorderPane.setAlignment(labelBox, Pos.CENTER);
            figureBox.setBottom(labelBox);
            hBox.getChildren().add(figureBox);
        });
        return hBox;
    }

    private ContextMenu openAvailableFiguresMenu(BorderPane borderPane) {
        List<ChessFigure> availableFigures = gameService.getAvailableFigures();
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getStyleClass().add("contextMenu");
        availableFigures.forEach(chessFigure -> {
            if (!isCellOccupied(borderPane)) {
                ImageView imageView = loadFigureImage(chessFigure.getColor(), chessFigure.getName());
                imageView.setFitHeight(40);
                imageView.setFitWidth(40);
                MenuItem menuItem = new MenuItem(chessFigure.getName().toString(), imageView);
                menuItem.getStyleClass().add("menu-item");
                menuItem.setOnAction(actionEvent -> onSelectContextMenuItem(chessFigure, borderPane));
                contextMenu.getItems().add(menuItem);
            }
        });
        if (contextMenu.getItems().size() == 0) {
            if (isCellOccupied(borderPane)) {
                ImageView imageView = new ImageView(gameFileService.loadImageByPath("images/trash.png"));
                imageView.setFitWidth(20);
                imageView.setFitHeight(20);
                MenuItem menuItem = new MenuItem("Remove figure", imageView);
                menuItem.setOnAction(actionEvent -> onRemoveFigureFromBoard(borderPane));
                contextMenu.getItems().add(menuItem);
            } else {
                ImageView imageView = new ImageView(gameFileService.loadImageByPath("images/mainIcon.png"));
                imageView.setFitWidth(60);
                imageView.setFitHeight(60);
                MenuItem menuItem = new MenuItem("No available figures for cell", imageView);
                contextMenu.getItems().add(menuItem);
            }
        }
        return contextMenu;
    }

    private void createHoverEffects(BorderPane borderPane) {
        if (!gameBoard.isGameStarted()) {
            ImageCursor imageCursor = new ImageCursor(gameFileService.loadImageByPath("images/stopCursor.png"));
            borderPane.setCursor(imageCursor);
        } else {
            borderPane.setCursor(Cursor.HAND);
        }
    }


    public void createNotification(String title, String text, NotificationStatus notificationStatus) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        switch (notificationStatus) {
            case ERROR: {
                alert = new Alert(Alert.AlertType.ERROR);
                break;
            }
            case WARNING: {
                alert = new Alert(Alert.AlertType.WARNING);
                break;
            }
        }
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    //ACTIONS
    private void onHoverFigure(MouseEvent e) {
        GameBoardCell gameBoardCell = gameBoard.getChessCell();
        if (!gameBoardCell.isInFocus()) {
            BorderPane borderPane = findBorderPaneById(((BorderPane) e.getSource()).getId());
            createHoverEffects(borderPane);
            if (isCellOccupied(borderPane)) {
                gameBoardCell.setInFocus(true);
                Rectangle rectangle = getRectangleOfBorderPane(borderPane);
                gameBoardCell.setColor((Color) rectangle.getFill());
                gameBoardCell.setRectangle(rectangle);
                rectangle.setFill(Color.web("#bacd33"));

                String figureId = getImageOfBorderPane(borderPane).getId();
                Position position = new Position(Integer.parseInt(borderPane.getId().split("-")[1].split("")[0]),
                        Integer.parseInt(borderPane.getId().split("-")[1].split("")[1]));
                FigureName figureName = FigureName.valueOf(figureId.split("-")[1]);
                FigureColor figureColor = FigureColor.valueOf(figureId.split("-")[0]);

                paintFigurePath(gameService.getFigureTrajectory(position, figureName, figureColor), figureColor);
            }
        }
    }

    private void onUnHooverFigure(MouseEvent e) {
        GameBoardCell gameBoardCell = gameBoard.getChessCell();
        if (gameBoardCell.isInFocus()) {
            BorderPane borderPane = findBorderPaneById(((BorderPane) e.getSource()).getId());
            createHoverEffects(borderPane);
            if (isCellOccupied(borderPane)) {
                gameBoardCell.setInFocus(false);
                Rectangle rectangle = getRectangleOfBorderPane(borderPane);
                rectangle.setFill(gameBoardCell.getColor());
                unPaintRectangle();
            }
        }
    }

    private void onFigureAttacks(MouseEvent e) {
        if (gameBoard.isGameStarted()) {
            openDialogWindow();
        }
    }

    private void onClearField(MouseEvent e) {
        if (gameBoard.isGameStarted()) {
            gameService.clearGameBoard(gameBoard.getWorkingFileDirectory());
            gameBoard.setGameBoardCell(new GameBoardCell());
            gameBoard.setSelectedCells(new ArrayList<>());
            clearBoard();
            refreshGame(gameBoard.getWorkingFileDirectory());
        }
    }

    private void onSelectContextMenuItem(ChessFigure chessFigure, BorderPane borderPane) {
        Position position = new Position();
        if (borderPane != null) {
            position.setyPosition(Integer.parseInt(borderPane.getId().split("-")[1].split("")[0]));
            position.setxPosition(Integer.parseInt(borderPane.getId().split("-")[1].split("")[1]));
        }
        chessFigure.setPosition(position);
        gameService.addNewFigure(chessFigure,gameBoard.getWorkingFileDirectory());
        refreshGame(gameBoard.getWorkingFileDirectory());
    }

    private void onRemoveFigureFromBoard(BorderPane borderPane) {
        Position position = new Position(Integer.parseInt(borderPane.getId().split("-")[1].split("")[0]),
                Integer.parseInt(borderPane.getId().split("-")[1].split("")[1]));
        gameService.removeFigure(position, gameBoard.getWorkingFileDirectory());
        removeFigureById(borderPane.getId());
        refreshGame(gameBoard.getWorkingFileDirectory());
    }

    private void onSavedGameResults(MouseEvent e) {
        GameFieldService gameFieldService = new GameFieldServiceImpl();

        DirectoryChooser directoryChooser = new DirectoryChooser();
        Node node = (Node) e.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        File file = directoryChooser.showDialog(stage);

        System.out.println(file.getPath());
        boolean isSaved = gameService.saveGameResults(file.getPath());
        if (isSaved) {
            gameFieldService.createNotification( "Saved results", "Figure attacks successfully saved", NotificationStatus.INFO);
        } else {
            gameFieldService.createNotification("Error saving results", "Figure attacks saving error", NotificationStatus.ERROR);
        }
    }

    private void onUploadChessPosition(MouseEvent e) {
        GameMenuServiceImpl gameMenuService = new GameMenuServiceImpl();
        Node node = (Node) e.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        File file = gameMenuService.selectFileDialog(stage);
        stage.setTitle("Chess game!");
        if(file!=null){
            if(gameFileService.gameFileValidator(file.getPath())){
                gameBoard.setGameBoardCell(new GameBoardCell());
                gameBoard.setSelectedCells(new ArrayList<>());
                clearBoard();
                gameBoard.getWorkingFileName().setText(file.getName());
                gameBoard.setWorkingFileDirectory(file.getPath());
            }else {
                createNotification( "Wrong file",
                        "The format of " + file.getName() + " file is wrong", NotificationStatus.ERROR);
            }
        }
        refreshGame(gameBoard.getWorkingFileDirectory());
    }

    public void onStartGame() {
        try {
            gameBoard.getWorkingFileName().setText("init.txt");
            gameBoard.setWorkingFileDirectory(System.getProperty("user.dir")+"\\"+"init.txt");
            gameFileService.createWorkingFiles();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        refreshGame(gameBoard.getWorkingFileDirectory());
    }

    public void onLoadGame(File file) {
        gameService.initGame(file.getPath());
        gameBoard.setGameStarted(true);
        gameBoard.getWorkingFileName().setText(file.getName());
        gameBoard.setWorkingFileDirectory(file.getPath());
        gameService.getFiguresForSetup().forEach(this::setFigureOnBoard);
    }

    private void refreshGame(String directory) {
        gameService.initGame(directory);
        gameBoard.setGameStarted(true);
        gameService.getFiguresForSetup().forEach(this::setFigureOnBoard);
        gameBoard.getWorkingFileContent().setText(gameFileService.getFileContent(directory));
    }
}
