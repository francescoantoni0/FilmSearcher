module com.example.filmsearcher {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;


    opens com.example.filmsearcher to javafx.fxml;
    exports com.example.filmsearcher;
}