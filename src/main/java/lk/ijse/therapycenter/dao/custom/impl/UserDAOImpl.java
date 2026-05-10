package lk.ijse.therapycenter.dao.custom.impl;

import lk.ijse.therapycenter.config.FactoryConfiguration;
import lk.ijse.therapycenter.dao.custom.UserDAO;
import lk.ijse.therapycenter.entity.User;
import org.hibernate.Session;

public class UserDAOImpl implements UserDAO {

    @Override
    public User searchByUsername(String username) {

        Session session = FactoryConfiguration.getInstance().getSession();

        return session.createQuery("FROM User WHERE username=:username", User.class).setParameter("username", username).uniqueResult();
    }
}