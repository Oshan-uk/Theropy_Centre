package lk.ijse.therapycenter.bo.custom.impl;

import lk.ijse.therapycenter.bo.custom.UserBO;
import lk.ijse.therapycenter.dao.DAOFactory;
import lk.ijse.therapycenter.dao.custom.UserDAO;
import lk.ijse.therapycenter.dao.custom.impl.UserDAOImpl;
import lk.ijse.therapycenter.dto.UserDTO;
import lk.ijse.therapycenter.entity.User;
import lk.ijse.therapycenter.exception.LoginException;
import lk.ijse.therapycenter.exception.RegistrationException;
import lk.ijse.therapycenter.util.PasswordUtil;
import lk.ijse.therapycenter.util.ValidationUtil;
import org.mindrot.jbcrypt.BCrypt;

public class UserBOImpl implements UserBO {

    private final UserDAO userDAO = DAOFactory.getDAO(DAOFactory.DAOTypes.USER);

    @Override
    public UserDTO login(String username, String plainPassword) throws LoginException {
        if (username == null || username.trim().isEmpty()) {
            throw new LoginException("Username cannot be empty.");
        }
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new LoginException("Password cannot be empty.");
        }

        User user = userDAO.findByUsername(username.trim());

        if (user == null) {
            throw new LoginException("No account found with that username.");
        }

        if (!PasswordUtil.verifyPassword(plainPassword, user.getPassword())) {
            throw new LoginException("Incorrect password. Please try again.");
        }

        return toDTO(user);
    }

    @Override
    public boolean register(UserDTO dto) throws RegistrationException {
        if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
            throw new RegistrationException("Username is required.");
        }
        if (!ValidationUtil.isValidEmail(dto.getEmail())) {
            throw new RegistrationException("Please enter a valid email address.");
        }
        if (dto.getPassword() == null || dto.getPassword().length() < 6) {
            throw new RegistrationException("Password must be at least 6 characters.");
        }

        if (userDAO.existsByUsername(dto.getUsername())) {
            throw new RegistrationException("That username is already taken. Please choose another.");
        }
        if (userDAO.existsByEmail(dto.getEmail())) {
            throw new RegistrationException("An account with that email already exists.");
        }

        String hashedPassword = PasswordUtil.hashPassword(dto.getPassword());

        User user = new User(dto.getUsername(), hashedPassword, dto.getRole(), dto.getEmail());
        return userDAO.save(user);
    }

    @Override
    public boolean updateUsername(int userId, String newUsername) throws RegistrationException {
        if (newUsername == null || newUsername.trim().isEmpty()) {
            throw new RegistrationException("New username cannot be empty.");
        }
        if (userDAO.existsByUsername(newUsername)) {
            throw new RegistrationException("That username is already taken.");
        }
        User user = userDAO.findById(userId);
        if (user == null) return false;

        user.setUsername(newUsername.trim());
        return userDAO.update(user);
    }

    @Override
    public boolean updatePassword(int userId, String oldPlain, String newPlain) throws LoginException {
        User user = userDAO.findById(userId);
        if (user == null) throw new LoginException("User not found.");

        if (!BCrypt.checkpw(oldPlain, user.getPassword())) {
            throw new LoginException("Current password is incorrect.");
        }
        if (newPlain == null || newPlain.length() < 6) {
            throw new LoginException("New password must be at least 6 characters.");
        }

        if (!PasswordUtil.verifyPassword(oldPlain, user.getPassword())) {
            throw new LoginException("Current password is incorrect.");
        }
        user.setPassword(PasswordUtil.hashPassword(newPlain));
        return userDAO.update(user);
    }

    @Override
    public UserDTO findById(int id) {
        User user = userDAO.findById(id);
        return user != null ? toDTO(user) : null;
    }

    private UserDTO toDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), null, user.getRole(), user.getEmail());
    }
}