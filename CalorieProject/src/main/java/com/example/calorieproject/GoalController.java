package com.example.calorieproject;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import java.io.*;

public class GoalController {

    @FXML private TextField calGoal;
    @FXML private TextField fatGoal;
    @FXML private TextField carbGoal;
    @FXML private TextField proteinGoal;

    @FXML
    public void goBack(ActionEvent event) throws IOException {
        CalorieApplication.setRoot("CalorieTracker.fxml");
    }

    @FXML
    public void save() {

        Account u = LoginController.loggedInUser;

        u.setGoals(
                Integer.parseInt(calGoal.getText()),
                Integer.parseInt(fatGoal.getText()),
                Integer.parseInt(carbGoal.getText()),
                Integer.parseInt(proteinGoal.getText())
        );
    }
}