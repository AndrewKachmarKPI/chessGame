package com.chess.chessgame.serviceImpl;

import com.chess.chessgame.enums.NotificationStatus;
import com.chess.chessgame.services.GameFieldService;
import com.chess.chessgame.services.GameFileService;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class GameMenuService {
    private final GameFieldService gameFieldService;
    private final GameFileService gameFileService;
    private final Group rootGroup;

    public GameMenuService() {
        gameFieldService = new GameFieldServiceImpl();
        gameFileService = new GameFileServiceImpl();
        rootGroup = createNavigationMenu();
    }

    public Scene createMainScene() {
        Scene scene = new Scene(rootGroup, Color.web("312e2b"));
        scene.getStylesheets().clear();
        scene.getStylesheets().add("page.css");
        return scene;
    }

    private Group createNavigationMenu() {
        EventHandler<MouseEvent> onStartGame = this::onStartGame;
        Button startGame = new Button("START GAME");
        startGame.addEventHandler(MouseEvent.MOUSE_CLICKED, onStartGame);
        startGame.getStyleClass().setAll("text", "success", "lg");

        EventHandler<MouseEvent> onSavedGames = this::onSavedGames;
        Button figureAttacks = new Button("LOAD GAME FILE");
        figureAttacks.addEventHandler(MouseEvent.MOUSE_CLICKED, onSavedGames);
        figureAttacks.getStyleClass().setAll("text", "warning", "lg");

        EventHandler<MouseEvent> onExitGame = this::onExitGame;
        Button exitField = new Button("EXIT GAME");
        exitField.addEventHandler(MouseEvent.MOUSE_CLICKED, onExitGame);
        exitField.getStyleClass().setAll("text", "danger", "lg");


        VBox vBox = new VBox(10, startGame, figureAttacks, exitField);
        vBox.setPadding(new Insets(20, 20, 20, 20));
        vBox.setAlignment(Pos.CENTER);

        HBox buttonBox = new HBox(10, vBox);
        buttonBox.setAlignment(Pos.CENTER);

        ImageView imageView = new ImageView(gameFileService.loadImageByPath("images/logo.png"));
        HBox imageBox = new HBox(10, imageView);
        imageBox.setAlignment(Pos.CENTER);

        VBox mainBox = new VBox(10, imageView, vBox);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setMinHeight(680);
        mainBox.setMinWidth(800);

        BorderPane borderPane = new BorderPane();
        BorderPane.setAlignment(mainBox, Pos.CENTER);
        borderPane.setCenter(mainBox);
        borderPane.setMinHeight(680);
        borderPane.setMinWidth(900);
        return new Group(borderPane);
    }


    private void onStartGame(MouseEvent e) {
        closeStartMenu();
        gameFieldService.onStartGame();
    }

    private void closeStartMenu() {
        rootGroup.getChildren().clear();
        Group gameFieldGroup = gameFieldService.createGameGroup();
        rootGroup.getChildren().add(gameFieldGroup);
    }

    private void onSavedGames(MouseEvent e) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        Stage stage = (Stage) rootGroup.getScene().getWindow();
        stage.setTitle("Select game file");
        stage.getIcons().add(gameFileService.loadImageByPath("images/mainIcon.png"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            if (gameFileService.gameFileValidator(file.getName())) {
                closeStartMenu();
                gameFieldService.onLoadGame(file);
            } else {
                gameFieldService.createNotification(3000, "Wrong file",
                        "The format of " + file.getName() + " file is wrong", NotificationStatus.ERROR);
            }
        }
    }

    private void onExitGame(MouseEvent e) {
        Platform.exit();
    }
}
