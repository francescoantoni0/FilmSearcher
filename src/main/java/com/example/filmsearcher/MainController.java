package com.example.filmsearcher;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.skin.ProgressBarSkin;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    public TextField filmNameField;
    public CheckBox isAdult;
    public ChoiceBox<String> languageChoice;
    public TextField filmYearField;
    public Button goButton;
    public StackPane pannello;

    private String url =
            "https://api.themoviedb.org/3/search/movie?query=";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        languageChoice.getItems().addAll("en-US", "it-IT", "fr-FR", "es-ES", "de-DE",
                "pt-PT", "ru-RU", "ja-JP", "ko-KR", "hi-IN", "zh-CN", "ar-SA");
        languageChoice.setValue("en-US");
        goButton.setOnAction(this::doSearch);
    }

    public void doSearch(Event event) {
        boolean adult = isAdult.isSelected();
        String adultString = adult ? "true" : "false";
        url += filmNameField.getText() + "&include_adult=" + adultString + "&language=" + languageChoice.getValue() + "&page=1";
        ProgressIndicator progressIndicator = new ProgressIndicator();
        pannello.getChildren().add(progressIndicator);
        progressIndicator.setProgress(-1);
        Thread thread = new Thread(() -> {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.themoviedb.org/3/search/movie?query=gatsby&include_adult=true&language=en-US&page=1"))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI3N2JmOTYzMGNlM2FkYjJlZWZjMWYyNDNkZjA5M2VmMSIsInN1YiI6IjY0N2RmNzQ4OTM4MjhlMDBkY2RkZWIyOSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.4uRczNbXzc7Sm1wHEvJlU7c15tkh2B8d20XB6ahiROc")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = null;
            try {
                response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(response.body());
        });
    }
}
