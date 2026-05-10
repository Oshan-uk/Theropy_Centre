package lk.ijse.therapycenter.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lk.ijse.therapycenter.dao.custom.UserDAO;
import lk.ijse.therapycenter.dao.custom.impl.UserDAOImpl;
import lk.ijse.therapycenter.entity.User;
import lk.ijse.therapycenter.util.BCryptUtil;

public class LoginFormController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private CheckBox chkShowPassword;

    @FXML
    private Button btnLogin;

    private final UserDAO userDAO = new UserDAOImpl();

    @FXML
    public void initialize() {

        System.out.println("Login Form Loaded");
    }

    @FXML
    void btnLoginOnAction(ActionEvent event) {

        String username = txtUsername.getText();

        String password = txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {

            new Alert(Alert.AlertType.ERROR, "Fill all fields").show();
            return;
        }

        User user = userDAO.searchByUsername(username);

        if (user == null) {
            new Alert(Alert.AlertType.ERROR, "User not found").show();
            return;
        }

        boolean isCorrect = BCryptUtil.check(password, user.getPassword());

        if (isCorrect) {

            new Alert(Alert.AlertType.INFORMATION, "Login Success").show();

        } else {

            new Alert(Alert.AlertType.ERROR, "Wrong Password").show();
        }
    }
}