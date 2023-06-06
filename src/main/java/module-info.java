module com.example.filmsearcher {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires java.json;


    opens com.example.filmsearcher to javafx.fxml;
    exports com.example.filmsearcher;
}