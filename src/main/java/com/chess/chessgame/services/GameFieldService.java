package com.chess.chessgame.services;

import com.chess.chessgame.domain.figures.*;
import com.chess.chessgame.enums.FigureColor;
import com.chess.chessgame.enums.FigureName;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
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
    public static boolean isCellSelected = false;
    public static Color selectedCellColor;

    public static Scene createGameScene() {
        paintGameBoard(rootGroup);
        paintBorders(rootGroup, 0, 80);
        paintBorders(rootGroup, 0, 900);
        paintBorders(rootGroup, 80, 0);
        paintBorders(rootGroup, 900, 0);
        GameService.initGame();
//        Rook rook = new Rook(FigureName.ROOK, FigureColor.BLACK, new Position(0,0));
//        rook.getMoveDirection();
        Queen rook = new Queen(FigureName.ROOK, FigureColor.BLACK, new Position(5,5));
        rook.getMoveDirection();
        return new Scene(rootGroup, 1000, 1000, Color.GRAY);
    }

    public static void paintGameBoard(Group group) {
        boolean isSecond = false;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                BorderPane borderPane = new BorderPane();
                borderPane.setMinWidth(100);
                borderPane.setMinHeight(100);
                borderPane.setLayoutX((i * 100) + 100);
                borderPane.setLayoutY((j * 100) + 100);
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
            imageView.setId(chessFigure.getColor().toString().toLowerCase(Locale.ROOT) + "-" + chessFigure.getName());
            imageView.setX(50);
            imageView.setY(50);

            List<BorderPane> borderPanes = getAllBorderPanes(rootGroup);
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

    public static List<BorderPane> getAllBorderPanes(Group group) {
        List<BorderPane> borderPanes = new ArrayList<>();
        group.getChildren().forEach(node -> {
            if (node instanceof BorderPane) {
                borderPanes.add((BorderPane) node);
            }
        });
        return borderPanes;
    }

    public static Rectangle getRectangleOfBorderPane(BorderPane borderPane){
        AtomicReference<Rectangle> rectangle = new AtomicReference<>(new Rectangle());
       borderPane.getChildren().forEach(content->{
           if(content instanceof Rectangle){
               rectangle.set((Rectangle) content);
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
        List<BorderPane> borderPanes = getAllBorderPanes(rootGroup);
        return borderPanes.stream().filter(borderPane -> borderPane.getId().equals(borderPaneId)).findFirst().orElse(new BorderPane());
    }

    public static void onHoverFigure(MouseEvent e) {
        if(!isCellSelected){
            BorderPane borderPane= findBorderPaneById(((BorderPane) e.getSource()).getId());
            if(borderPane.getCenter() instanceof ImageView){
                isCellSelected = true;
                Rectangle rectangle = getRectangleOfBorderPane(borderPane);
                selectedCellColor = (Color) rectangle.getFill();
                rectangle.setFill(Color.GREEN);
            }
        }
    }
    public static void onUnHooverFigure(MouseEvent e) {
        if(isCellSelected){
            BorderPane borderPane= findBorderPaneById(((BorderPane) e.getSource()).getId());
            if(borderPane.getCenter() instanceof ImageView){
                isCellSelected = false;
                Rectangle rectangle = getRectangleOfBorderPane(borderPane);
                rectangle.setFill(selectedCellColor);
            }
        }
    }

}
