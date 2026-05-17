package lk.ijse.therapycenter.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.therapycenter.dto.UserDTO;


public class AdminDashboardController {

    @FXML private Label lblWelcome;
    @FXML private AnchorPane contentPane;

    private UserDTO currentUser;

    public void setCurrentUser(UserDTO user) {
        this.currentUser = user;
        lblWelcome.setText("Welcome, " + user.getUsername() + "  |  Role: " + user.getRole());
    }


    @FXML
    public void onTherapistsClicked(ActionEvent event) {
        loadScreen("/view/Therapist.fxml");
    }
    @FXML
    public void onProgramsClicked(ActionEvent event) {
        loadScreen("/view/TherapyProgram.fxml");
    }
    @FXML
    public void onReportsClicked(ActionEvent event) {
        loadScreen("/view/AdminReport.fxml");
    }
    @FXML
    public void onProfileClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserProfile.fxml"));
            Parent root = loader.load();

            UserProfileController profileCtrl = loader.getController();
            profileCtrl.setCurrentUser(currentUser);

            contentPane.getChildren().setAll(root);
            AnchorPane.setTopAnchor(root, 0.0);
            AnchorPane.setBottomAnchor(root, 0.0);
            AnchorPane.setLeftAnchor(root, 0.0);
            AnchorPane.setRightAnchor(root, 0.0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onLogoutClicked(ActionEvent event) {
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));

            Stage stage = (Stage) lblWelcome.getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
            stage.setTitle("Serenity Therapy Center - Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadScreen(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentPane.getChildren().setAll(root);
            AnchorPane.setTopAnchor(root, 0.0);
            AnchorPane.setBottomAnchor(root, 0.0);
            AnchorPane.setLeftAnchor(root, 0.0);
            AnchorPane.setRightAnchor(root, 0.0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}