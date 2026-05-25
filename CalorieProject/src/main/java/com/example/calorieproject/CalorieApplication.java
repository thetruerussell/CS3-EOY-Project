package com.example.calorieproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

import java.io.IOException;

public class CalorieApplication extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {

        primaryStage = stage;

        FXMLLoader loader = new FXMLLoader(
                CalorieApplication.class.getResource("Login.fxml")
        );

        Parent root = loader.load();

        Scene scene = new Scene(root);

        stage.setTitle("NutriTrack");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
    public static void setRoot(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    CalorieApplication.class.getResource(fxml)
            );

            Parent root = loader.load();

            Scene scene = primaryStage.getScene();

            animateTransition(root);

            scene.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void animateTransition(Parent root) {

        root.setOpacity(0);
        root.setTranslateY(20);

        FadeTransition fade = new FadeTransition(Duration.millis(200), root);
        fade.setFromValue(0);
        fade.setToValue(1);

        javafx.animation.TranslateTransition slide =
                new javafx.animation.TranslateTransition(Duration.millis(200), root);
        slide.setFromY(20);
        slide.setToY(0);

        fade.play();
        slide.play();
    }
}