package com.example.filmsearcher;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class HttpReqService extends Service<Boolean> {

    final MainController m = HelloApplication.mainController;
    private String url;

    private String apiKey = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI3N2JmOTYzMGNl" +
            "M2FkYjJlZWZjMWYyNDNkZjA5M2VmMSIsInN1YiI6IjY0N2RmNzQ4OTM4MjhlMDBkY2RkZWIyOSIsInNjb3BlcyI6Wy" +
            "JhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.4uRczNbXzc7Sm1wHEvJlU7c15tkh2B8d20XB6ahiROc";
    @Override
    protected Task<Boolean> createTask() {
        return new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                URL url1 = new URL(url);
                HttpsURLConnection service = (HttpsURLConnection) url1.openConnection();
                service.setRequestProperty("Host", "api.themoviedb.org");
                service.setRequestProperty("Accept", "application/json");
                service.setRequestProperty("Authorization", "Bearer " + apiKey);
                service.setRequestMethod("GET");
                service.setDoOutput(true);
                if (service.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : " + service.getResponseCode());
                }
                Files.copy(service.getInputStream(), new File("response.json").toPath(),
                        StandardCopyOption.REPLACE_EXISTING);
                service.disconnect();
                return true;
            }
        };
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
