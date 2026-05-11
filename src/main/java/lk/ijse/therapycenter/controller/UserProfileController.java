package lk.ijse.therapycenter.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lk.ijse.therapycenter.bo.BOFactory;
import lk.ijse.therapycenter.bo.custom.UserBO;
import lk.ijse.therapycenter.dto.UserDTO;
import lk.ijse.therapycenter.exception.LoginException;
import lk.ijse.therapycenter.exception.RegistrationException;


public class UserProfileController {

    @FXML private Label lblCurrentUser;
    @FXML private Label lblRole;

    @FXML private TextField txtNewUsername;
    @FXML private Label lblUsernameMsg;

    @FXML private PasswordField txtOldPassword;
    @FXML private PasswordField txtNewPassword;
    @FXML private PasswordField txtConfirmPassword;
    @FXML private TextField txtOldPasswordVisible;
    @FXML private TextField txtNewPasswordVisible;
    @FXML private CheckBox chkShowOldPass;
    @FXML private CheckBox chkShowNewPass;
    @FXML private Label lblPasswordMsg;

    private final UserBO userBO = BOFactory.getBO(BOFactory.BOTypes.USER);
    private UserDTO currentUser;

    public void setCurrentUser(UserDTO user) {
        this.currentUser = user;
        lblCurrentUser.setText("Username: " + user.getUsername());
        lblRole.setText("Role: " + user.getRole());
    }

    @FXML
    public void initialize() {

        chkShowOldPass.selectedProperty().addListener((obs, was, now) -> {
            if (now) {
                txtOldPasswordVisible.setText(txtOldPassword.getText());
                toggleVisibility(txtOldPassword, txtOldPasswordVisible, true);
            } else {
                txtOldPassword.setText(txtOldPasswordVisible.getText());
                toggleVisibility(txtOldPassword, txtOldPasswordVisible, false);
            }
        });

        chkShowNewPass.selectedProperty().addListener((obs, was, now) -> {
            if (now) {
                txtNewPasswordVisible.setText(txtNewPassword.getText());
                toggleVisibility(txtNewPassword, txtNewPasswordVisible, true);
            } else {
                txtNewPassword.setText(txtNewPasswordVisible.getText());
                toggleVisibility(txtNewPassword, txtNewPasswordVisible, false);
            }
        });
    }

    @FXML
    public void onChangeUsernameClicked(ActionEvent event) {
        String newName = txtNewUsername.getText().trim();
        if (newName.isEmpty()) {
            showMsg(lblUsernameMsg, "New username cannot be empty.", false);
            return;
        }

        try {
            boolean ok = userBO.updateUsername(currentUser.getId(), newName);
            if (ok) {
                currentUser.setUsername(newName);
                lblCurrentUser.setText("Username: " + newName);
                showMsg(lblUsernameMsg, "Username updated successfully.", true);
                txtNewUsername.clear();
            } else {
                showMsg(lblUsernameMsg, "Update failed. Please try again.", false);
            }
        } catch (RegistrationException e) {
            showMsg(lblUsernameMsg, e.getMessage(), false);
        }
    }

    @FXML
    public void onChangePasswordClicked(ActionEvent event) {
        String oldPass = chkShowOldPass.isSelected() ?
                txtOldPasswordVisible.getText() : txtOldPassword.getText();
        String newPass = chkShowNewPass.isSelected() ?
                txtNewPasswordVisible.getText() : txtNewPassword.getText();
        String confirm = txtConfirmPassword.getText();

        if (oldPass.isEmpty() || newPass.isEmpty() || confirm.isEmpty()) {
            showMsg(lblPasswordMsg, "Please fill in all password fields.", false);
            return;
        }
        if (!newPass.equals(confirm)) {
            showMsg(lblPasswordMsg, "New password and confirmation do not match.", false);
            return;
        }
        if (newPass.length() < 6) {
            showMsg(lblPasswordMsg, "Password must be at least 6 characters.", false);
            return;
        }

        try {
            boolean ok = userBO.updatePassword(currentUser.getId(), oldPass, newPass);
            showMsg(lblPasswordMsg, ok ? "Password changed successfully." : "Update failed.", ok);
            if (ok) clearPasswordFields();
        } catch (LoginException e) {
            showMsg(lblPasswordMsg, e.getMessage(), false);
        }
    }

    private void toggleVisibility(PasswordField hidden, TextField visible, boolean showVisible) {
        hidden.setVisible(!showVisible);
        hidden.setManaged(!showVisible);
        visible.setVisible(showVisible);
        visible.setManaged(showVisible);
    }

    private void clearPasswordFields() {
        txtOldPassword.clear(); txtNewPassword.clear(); txtConfirmPassword.clear();
        txtOldPasswordVisible.clear(); txtNewPasswordVisible.clear();
    }

    private void showMsg(Label label, String msg, boolean ok) {
        label.setText(msg);
        label.setStyle(ok ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
    }
}