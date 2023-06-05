package com.example.filmsearcher;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    public TextField filmNameField;
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        HelloApplication.firstStage.hide();
        HelloApplication.mainStage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}