package lk.ijse.therapycenter.dao.custom;

import lk.ijse.therapycenter.entity.User;

public interface UserDAO {

    User searchByUsername(String username);
}