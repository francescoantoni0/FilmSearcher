package com.example.filmsearcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class HelloApplication extends Application {

    public static Stage mainStage, firstStage, filmStage;
    public static HelloController helloController;
    public static MainController mainController;

    public static FilmController filmController;

    @Override
    public void start(Stage stage) throws IOException {
        firstStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Parent root = fxmlLoader.load();
        helloController = fxmlLoader.getController();
        firstStage.setScene(new Scene(root));
        firstStage.setTitle("Film Searcher");
        firstStage.show();

        var fxmlLoader2 = new FXMLLoader(HelloApplication.class.getResource("main-view.fxml"));
        mainStage = new Stage();
        Parent root1 = fxmlLoader2.load();
        mainController = fxmlLoader2.getController();
        mainStage.setScene(new Scene(root1));
        mainStage.setTitle("Film Searcher");

        var fxmlLoader3 = new FXMLLoader(HelloApplication.class.getResource("film-view.fxml"));
        filmStage = new Stage();
        Parent root2 = fxmlLoader3.load();
        filmController = fxmlLoader3.getController();
        filmStage.setScene(new Scene(root2));
        filmStage.setTitle("Film details");
        filmStage.setOnCloseRequest(windowEvent -> {
            filmController.reset();
        });
    }

    public static void main(String[] args) {
        launch();
    }
}