package lk.ijse.therapycenter.dao.custom;

import lk.ijse.therapycenter.dao.CrudDAO;
import lk.ijse.therapycenter.entity.User;

public interface UserDAO extends CrudDAO<User, Integer> {
    User findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}