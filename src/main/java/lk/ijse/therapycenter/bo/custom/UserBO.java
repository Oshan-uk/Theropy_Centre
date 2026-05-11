package lk.ijse.therapycenter.bo.custom;

import lk.ijse.therapycenter.dto.UserDTO;
import lk.ijse.therapycenter.exception.LoginException;
import lk.ijse.therapycenter.exception.RegistrationException;


public interface UserBO {
    UserDTO login(String username, String plainPassword) throws LoginException;

    boolean register(UserDTO userDTO) throws RegistrationException;

    boolean updateUsername(int userId, String newUsername) throws RegistrationException;

    boolean updatePassword(int userId, String oldPlain, String newPlain) throws LoginException;

    UserDTO findById(int id);
}