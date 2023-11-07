package com.jbunce.pvlpus;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PvlpusApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PvlpusApp.class.getResource("principal-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 910, 610);
        stage.setTitle("PROGRAMITA");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}