package com.chess.chessgame.services;

import com.chess.chessgame.domain.figures.ChessFigure;
import com.chess.chessgame.domain.figures.Position;
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

public class GameFieldService {
    public static Scene createGameScene() {
        Group rootGroup = new Group();

        paintGameBoard(rootGroup);
        paintBorders(rootGroup, 0, 80);
        paintBorders(rootGroup, 0, 900);
        paintBorders(rootGroup, 80, 0);
        paintBorders(rootGroup, 900, 0);

        ChessFigure chessFigure = new ChessFigure(FigureName.KING, FigureColor.BLACK, new Position(0, 7));
        ChessFigure chessFigure2 = new ChessFigure(FigureName.QUEEN, FigureColor.WHITE, new Position(0, 7));
        ChessFigure chessFigure3 = new ChessFigure(FigureName.BISHOP, FigureColor.BLACK, new Position(0, 7));

        setFigureOnBoard(rootGroup, chessFigure);
        setFigureOnBoard(rootGroup, chessFigure2);
        setFigureOnBoard(rootGroup, chessFigure3);

        Scene scene = new Scene(rootGroup, 1000, 1000, Color.GRAY);
        return scene;
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

                EventHandler<MouseEvent> eventHandler = GameFieldService::onClick;
                borderPane.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
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

    public static void setFigureOnBoard(Group group, ChessFigure chessFigure) {
        try {
            ImageView imageView = loadFigureImage(chessFigure.getColor(), chessFigure.getName());
            imageView.setId(chessFigure.getColor().toString().toLowerCase(Locale.ROOT) + "-" + chessFigure.getName());
            imageView.setX(50);
            imageView.setY(50);
//            group.getChildren().add(imageView);

            List<BorderPane> borderPanes = getAllBorderPanes(group);
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
//    public static Rectangle getPaneRectangle(BorderPane borderPane){
//        return (Rectangle) borderPane.getChildren().stream().filter(node -> node.getId().equals(borderPane.getId())).findFirst()
//                .orElseThrow(() -> new RuntimeException("rectangle not found"));
//    }

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

    public static void onClick(MouseEvent e) {
        System.out.println("CLICKED!!!!"+ e.toString());
    }
}
