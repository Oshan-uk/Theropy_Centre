package lk.ijse.therapycenter.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lk.ijse.therapycenter.bo.BOFactory;
import lk.ijse.therapycenter.bo.custom.UserBO;
import lk.ijse.therapycenter.dto.UserDTO;
import lk.ijse.therapycenter.exception.LoginException;

public class LoginController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private TextField txtPasswordVisible;
    @FXML private CheckBox chkShowPassword;
    @FXML private Label lblError;

    private final UserBO userBO = BOFactory.getBO(BOFactory.BOTypes.USER);

    @FXML
    public void initialize() {
        txtPasswordVisible.textProperty().bindBidirectional(
                txtPassword.textProperty() instanceof javafx.beans.property.StringProperty ?
                        (javafx.beans.property.StringProperty) txtPassword.textProperty() : null
        );

        chkShowPassword.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                txtPasswordVisible.setText(txtPassword.getText());
                txtPassword.setVisible(false);
                txtPasswordVisible.setVisible(true);
                txtPasswordVisible.requestFocus();
            } else {
                txtPassword.setText(txtPasswordVisible.getText());
                txtPasswordVisible.setVisible(false);
                txtPassword.setVisible(true);
                txtPassword.requestFocus();
            }
        });
    }

    @FXML
    public void onLoginClicked(ActionEvent event) {
        String username = txtUsername.getText().trim();
        String password = chkShowPassword.isSelected() ?
                txtPasswordVisible.getText() : txtPassword.getText();

        lblError.setText("");

        try {
            UserDTO loggedIn = userBO.login(username, password);

            String fxmlFile = "ADMIN".equals(loggedIn.getRole()) ?
                    "/lk/ijse/therapycenter/AdminDashboard.fxml" :
                    "/lk/ijse/therapycenter/ReceptionistDashboard.fxml";

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            if ("ADMIN".equals(loggedIn.getRole())) {
                AdminDashboardController admin = loader.getController();
                admin.setCurrentUser(loggedIn);
            } else {
                ReceptionistDashboardController rec = loader.getController();
                rec.setCurrentUser(loggedIn);
            }

            Stage stage = (Stage) txtUsername.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Serenity Therapy Center");

        } catch (LoginException e) {
            lblError.setText(e.getMessage());
        } catch (Exception e) {
            lblError.setText("Something went wrong. Please try again.");
            e.printStackTrace();
        }
    }

    @FXML
    public void onClearClicked(ActionEvent event) {
        txtUsername.clear();
        txtPassword.clear();
        txtPasswordVisible.clear();
        lblError.setText("");
        txtUsername.requestFocus();
    }
}