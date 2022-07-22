package com.example.oop.controller;

import com.example.oop.Primary;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    @FXML
    public ProgressBar process;
    @FXML
    public AnchorPane ap;
    @FXML
    public Label text;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Task<Void> task = new Task<>() {
            @Override
            public Void call() throws InterruptedException {
                final int max = 100;
                updateProgress(0, max);
                Thread.sleep(1000);
                for (int i = 1; i <= 80; i++) {
                    Thread.sleep((long) (5 + 5 * Math.random()));
                    updateProgress(i, max);
                }
                for (int i = 81; i <= 95; i++) {
                    Thread.sleep((long) (20 + 20 * Math.random()));
                    updateProgress(i, max);
                }
                for (int i = 96; i <= max; i++) {
                    if (isCancelled()) {
                        break;
                    }
                    Thread.sleep((long) (50 + 50 * Math.random()));
                    updateProgress(i, max);
                }
                Thread.sleep(300);

                Platform.runLater(() -> {
                    try {
                        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainapp.fxml")));
                        Scene sc = new Scene(root);
                        sc.setFill(Color.TRANSPARENT);
                        Primary.getStage().setScene(sc);
                        Primary.getStage().setAlwaysOnTop(false);
                        Primary.getStage().centerOnScreen();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                return null;
            }
        };
        process.progressProperty().bind(task.progressProperty());
        new Thread(task).start();


    }

}

