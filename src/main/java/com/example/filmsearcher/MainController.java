package com.example.filmsearcher;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import javax.json.*;

public class MainController implements Initializable {

    public TextField filmNameField;
    public CheckBox isAdult;
    public ChoiceBox<String> languageChoice;
    public TextField filmYearField;
    public Button goButton;
    public StackPane pannello;

    private String adultString, languageString, yearString, filmNameString, filename;

    private String url = "https://api.themoviedb.org/3/search/movie?query=";

    public ListView<FilmResult> listView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        languageChoice.getItems().addAll("en-US", "it-IT", "fr-FR", "es-ES", "de-DE",
                "pt-PT", "ru-RU", "ja-JP", "ko-KR", "hi-IN", "zh-CN", "ar-SA");
        languageChoice.setValue("en-US");
        goButton.setOnAction(this::doSearch);

    }

    public void doSearch(Event event) {
        filename = "response.json";
        adultString = isAdult.isSelected() ? "true" : "false";
        languageString = languageChoice.getValue();
        yearString = filmYearField.getText();
        filmNameString = filmNameField.getText();
        String reqUrl = url + filmNameField.getText() + "&include_adult=" + adultString + "&language=" + languageChoice.getValue()
                + "&page=1";
        HttpReqService service = new HttpReqService();
        ProgressIndicator progressIndicator = new ProgressIndicator();
        service.setUrl(reqUrl);
        pannello.getChildren().add(progressIndicator);
        progressIndicator.setProgress(-1);
        progressIndicator.progressProperty().bind(service.createTask().progressProperty());
        service.start();
        service.setOnSucceeded(event1 -> {
            pannello.getChildren().remove(progressIndicator);
            try {
                HelloApplication.mainStage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
            jsonParser(filename);
        });

        service.setOnFailed(e -> {
            pannello.getChildren().remove(progressIndicator);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("An error occurred while searching for the film");
            alert.showAndWait();
        });

        listView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                FilmResult film = listView.getSelectionModel().getSelectedItem();
                FilmController controller = HelloApplication.filmController;
                try {
                    controller.setFilm(film);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                HelloApplication.filmStage.show();
            }
        });
    }

    private void jsonParser(String filename) {
        try (InputStream input = new FileInputStream(filename)) {
            JsonReader reader = Json.createReader(input);
            JsonObject root = reader.readObject();
            JsonArray results = root.getJsonArray("results");
            if (results.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No results found");
                alert.setContentText("No results found for the given parameters");
                alert.showAndWait();
                return;
            }
            ObservableList<FilmResult> filmList = FXCollections.observableArrayList();
            for (JsonValue j : results) {
                JsonObject film = (JsonObject) j;
                String title = film.getString("title");
                String overview = film.getString("overview");
                String posterPath = film.get("poster_path").toString().replace("\"", "");
                String releaseDate = film.getString("release_date");
                String voteAverage = film.getJsonNumber("vote_average").toString();
                String originalLanguage = film.getString("original_language");
                FilmResult filmResult = new FilmResult(title, overview, posterPath, releaseDate, voteAverage,
                        originalLanguage);
                filmList.add(filmResult);
            }
            listView.setCellFactory(new Callback<>() {
                @Override
                public ListCell<FilmResult> call(ListView<FilmResult> filmResultListView) {
                    return new ListCell<>() {
                        @Override
                        protected void updateItem(FilmResult filmResult, boolean empty) {
                            super.updateItem(filmResult, empty);
                            if (filmResult == null || empty) {
                                setText(null);
                            } else {
                                setText(filmResult.getTitle());
                            }
                        }
                    };
                }
            });
            listView.refresh();
            listView.setItems(filmList);
            listView.refresh();

        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String getAdultString() {
        return adultString;
    }

    public String getLanguageString() {
        return languageString;
    }

    public String getYearString() {
        return yearString;
    }

    public String getFilmNameString() {
        return filmNameString;
    }

    public String getUrl() {
        return url;
    }
}
