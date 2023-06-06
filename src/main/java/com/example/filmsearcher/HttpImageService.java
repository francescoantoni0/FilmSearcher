package com.example.filmsearcher;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpImageService extends Service<String> {

    private String imageUrl;
    private String outputFilePath;

    public HttpImageService(String imageUrl, String outputFilePath) {
        this.imageUrl = imageUrl;
        this.outputFilePath = outputFilePath;
    }

    @Override
    protected Task<String> createTask() {
        return new Task<>() {
            @Override
            protected String call() throws Exception {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    try (InputStream inputStream = connection.getInputStream();
                         FileOutputStream outputStream = new FileOutputStream(outputFilePath)) {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                        succeeded();
                        return outputFilePath;
                    }
                } else {
                    failed();
                    throw new RuntimeException("Failed to download image. Response code: " + responseCode);
                }
            }
        };
    }
}
