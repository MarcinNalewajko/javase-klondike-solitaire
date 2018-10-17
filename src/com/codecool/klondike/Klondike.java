package com.codecool.klondike;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Klondike extends Application {

    private static final double WINDOW_WIDTH = 1400;
    private static final double WINDOW_HEIGHT = 900;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Card.loadCardImages();
        Game game = new Game();
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Menu");
        VBox vBox = new VBox(menuBar);
        menuBar.getMenus().add(menu);




        MenuItem restart = new MenuItem("Restart");
        MenuItem changeBG = new MenuItem("Change Backgorund");
        MenuItem changeFront = new MenuItem("Change Front of Cards");
        MenuItem changeBack = new MenuItem("Change Back of Cards");



        menu.getItems().add(restart);
        menu.getItems().add(changeBG);
        menu.getItems().add(changeFront);
        menu.getItems().add(changeBack);


        Button restartButton = new Button("Restart");
        restartButton.setId("restartButton");
        restartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("1");
                start(primaryStage);
            }
        });
        game.getChildren().add(menuBar);
        game.setTableBackground(new Image("/table/3.png",1400,900,false,true));

        primaryStage.setTitle("Klondike Solitaire");
        primaryStage.setScene(new Scene(game, WINDOW_WIDTH, WINDOW_HEIGHT));
        primaryStage.show();
    }

}
