package com.example.filmsearcher;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class FilmController implements Initializable {
    public Label filmTitle;
    public ImageView filmCover;
    public Label releaseDate;
    public Label voteAverage;
    public Label origLang;
    public TextArea overview;
    public StackPane stack;

    public void setFilm(FilmResult film) throws Exception {
        filmTitle.setText(film.getTitle());
        releaseDate.setText(releaseDate.getText() + film.getReleaseDate());
        voteAverage.setText(voteAverage.getText() + film.getVoteAverage());
        origLang.setText(origLang.getText() + film.getOriginalLanguage());
        overview.setText(overview.getText() + film.getOverview());

        ProgressIndicator progressIndicator = new ProgressIndicator();
        stack.getChildren().add(progressIndicator);
        progressIndicator.setProgress(-1);
        HttpImageService service = new HttpImageService(FilmResult.imgURL + film.getPosterPath(), film.getTitle() + ".jpg");
        service.setOnSucceeded(workerStateEvent -> {
            stack.getChildren().remove(progressIndicator);
            filmCover.setImage(new Image("file:"+ service.getValue()));
        });
        progressIndicator.progressProperty().bind(service.createTask().progressProperty());
        service.start();
    }

    public void reset() {
        filmTitle.setText("Film Title");
        releaseDate.setText("Release date: ");
        voteAverage.setText("Vote average: ");
        origLang.setText("Original language: ");
        overview.setText("Overview: ");
        filmCover.setImage(null);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
