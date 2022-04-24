package com.chess.chessgame.serviceImpl;

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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class GameMenuService {
    private final GameFieldServiceImpl gameFieldService;
    private final GameFileService gameFileService;
    private Group rootGroup;

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

    private HBox createExitButton() {
        EventHandler<MouseEvent> onExitGame = this::onCloseGame;
        Button exitField = new Button("Exit game");
        exitField.addEventHandler(MouseEvent.MOUSE_CLICKED, onExitGame);
        exitField.getStyleClass().setAll("text", "clear-field");
        return new HBox(10, exitField);
    }

    private Group createNavigationMenu() {
        EventHandler<MouseEvent> onStartGame = this::onStartGame;
        Button startGame = new Button("START GAME");
        startGame.addEventHandler(MouseEvent.MOUSE_CLICKED, onStartGame);
        startGame.getStyleClass().setAll("text", "success","lg");

        EventHandler<MouseEvent> onSavedGames = this::onSavedGames;
        Button figureAttacks = new Button("SAVED GAMES");
        figureAttacks.addEventHandler(MouseEvent.MOUSE_CLICKED, onSavedGames);
        figureAttacks.getStyleClass().setAll("text", "warning","lg");

        EventHandler<MouseEvent> onExitGame = this::onExitGame;
        Button exitField = new Button("EXIT GAME");
        exitField.addEventHandler(MouseEvent.MOUSE_CLICKED, onExitGame);
        exitField.getStyleClass().setAll("text", "danger","lg");


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

    private void onCloseGame(MouseEvent e) {
        rootGroup.getChildren().clear();
        rootGroup.getChildren().add(createNavigationMenu());
    }

    private void onStartGame(MouseEvent e) {
        rootGroup.getChildren().clear();
        Group gameFieldGroup = gameFieldService.createGameGroup();
//        gameFieldGroup.getChildren().add(createExitButton());
        rootGroup.getChildren().add(gameFieldGroup);
        gameFieldService.onStartGame();
    }

    private void onSavedGames(MouseEvent e) {
        gameFieldService.createGameGroup();
        gameFieldService.onStartGame();
    }

    private void onExitGame(MouseEvent e) {
        Platform.exit();
    }
}
