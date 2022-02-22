package com.chess.chessgame.services;

import com.chess.chessgame.domain.figures.ChessFigure;
import com.chess.chessgame.domain.figures.Position;
import com.chess.chessgame.enums.FigureColor;
import com.chess.chessgame.enums.FigureName;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Locale;

public class GameFieldService {

    //    public static Scene createGameBoard(){
//
//    }
    public static Scene createGameScene() {
        Group rootGroup = new Group();
        Scene scene = new Scene(rootGroup, 1000, 1000, Color.GRAY);

        paintGameBoard(rootGroup);
        paintBorders(rootGroup, 0, 50);
        paintBorders(rootGroup, 0, 900);
        paintBorders(rootGroup, 50, 0);
        paintBorders(rootGroup, 900, 0);

        ChessFigure chessFigure = new ChessFigure();
        chessFigure.setColor(FigureColor.WHITE);
        chessFigure.setName(FigureName.KING);
        chessFigure.setPosition(new Position(1,1));

        setFigureOnBoard(rootGroup,chessFigure);

        return scene;
    }

    public static void paintGameBoard(Group group){
        boolean isSecond = false;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Rectangle rectangle = new Rectangle();
                rectangle.setX((i * 100) + 100);
                rectangle.setY((j * 100) + 100);
                rectangle.setWidth(100);
                rectangle.setHeight(100);
                if (isSecond) {
                    rectangle.setFill(Color.YELLOWGREEN);
                } else {
                    rectangle.setFill(Color.ANTIQUEWHITE);
                }
                group.getChildren().add(rectangle);
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
                rectangle.setHeight(50);
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
                rectangle.setWidth(50);
                rectangle.setHeight(100);
                Text text = new Text(String.valueOf(i + 1));
                stack.getChildren().addAll(rectangle, text);
                group.getChildren().add(stack);
            }
        }
    }

    public static void setFigureOnBoard(Group group, ChessFigure chessFigure){
        try {
            ImageView imageView = loadFigureImage(chessFigure.getColor(), chessFigure.getName());
            imageView.setX((chessFigure.getPosition().getxPosition()*100)+100);
            imageView.setY((chessFigure.getPosition().getyPosition()*100)+100);
            group.getChildren().add(imageView);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static ImageView loadFigureImage(FigureColor figureColor, FigureName figureName) throws FileNotFoundException {
        String path = "images/" + figureColor.toString().toLowerCase(Locale.ROOT)
                + figureName.toString().substring(0, 1).toUpperCase(Locale.ROOT)
                + figureName.toString().substring(1).toLowerCase(Locale.ROOT)+".png";
        File file = new File(path);
        Image image = new Image(new FileInputStream(file));
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        return imageView;
    }
}
