package com.codecool.klondike;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.sql.SQLOutput;

public class Klondike extends Application {
    private int backgroundAmount=3;
    private int currentBackground=1;
    private String frontPath="card_images/Front/";
    private int currentFront=1;
    private int frontAmount=3;
    private String backPath="card_images/Back/";
    private int currentBack=1;
    private int backAmount=6;
    private static final double WINDOW_WIDTH = 1400;
    private static final double WINDOW_HEIGHT = 900;
    private static Scene scene;

    public static void addEndLabel() {

        Node endGameId = ((Scene) scene).lookup("#endGame");
        Label endGameLabel = (Label) endGameId;
        endGameLabel.setText("Congratulations, you win!");
        endGameLabel.toFront();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        Card.loadCardImages(backPath+currentBack+"/", frontPath+currentFront+"/");
        Game game = new Game();
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Menu");
        VBox vBox = new VBox(menuBar);
        menuBar.getMenus().add(menu);

        Label endGameLabel = new Label();

        endGameLabel.setId("endGame");
        endGameLabel.setFont(new Font("System", 50));
        endGameLabel.setStyle("-fx-background-color: #e3e3e3");
        endGameLabel.setTranslateX(500);
        endGameLabel.setTranslateY(400);
        game.getChildren().add(endGameLabel);

        MenuItem restart = new MenuItem("Restart");
        MenuItem changeBG = new MenuItem("Change Backgorund");
        MenuItem changeFront = new MenuItem("Change Front of Cards");
        MenuItem changeBack = new MenuItem("Change Back of Cards");



        menu.getItems().add(restart);
        menu.getItems().add(changeBG);
        menu.getItems().add(changeFront);
        menu.getItems().add(changeBack);

        restart.setId("restartButton");
        restart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                start(primaryStage);
            }
        });

        changeBG.setId("ChangeBackgorund");
        changeBG.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (currentBackground<backgroundAmount){
                    currentBackground+=1;
                }
                else{currentBackground=1;}
                game.setTableBackground(new Image("/table/"+currentBackground+".png",1400,900,false,true));



            }
        });


        changeBack.setId("changeBack");
        changeBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (currentBack<backAmount){
                    currentBack+=1;
                }
                else{currentBack=1;}
                Card.loadCardImages(backPath+currentBack+"/", frontPath+currentFront+"/");//                primaryStage.show();
                start(primaryStage);
            }
        });



        changeFront.setId("changeFront");
        changeFront.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (currentFront<frontAmount){
                    currentFront+=1;
                }
                else{currentFront=1;}
                Card.loadCardImages(backPath+currentBack+"/", frontPath+currentFront+"/");//                primaryStage.show();
                start(primaryStage);
            }
        });



        game.getChildren().add(menuBar);
        game.setTableBackground(new Image("/table/"+currentBackground+".png",1400,900,false,true));

        primaryStage.setTitle("Klondike Solitaire");
        scene = new Scene(game, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
