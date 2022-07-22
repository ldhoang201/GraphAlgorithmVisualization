package com.example.oop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class    MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
        Parent root = loader.load();
        Primary.setStage(stage);
        stage.setTitle("WELCOME MY GRAPH");
        Scene scene = new Scene(root);
        Primary.getStage().setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        Primary.getStage().setAlwaysOnTop(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
