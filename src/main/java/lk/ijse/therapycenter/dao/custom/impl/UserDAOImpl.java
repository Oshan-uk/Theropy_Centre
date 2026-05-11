package lk.ijse.therapycenter.dao.custom.impl;

import lk.ijse.therapycenter.config.HibernateUtil;
import lk.ijse.therapycenter.dao.custom.UserDAO;
import lk.ijse.therapycenter.entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class UserDAOImpl implements UserDAO {

    @Override
    public boolean save(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSession()) {
            tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSession()) {
            tx = session.beginTransaction();
            session.merge(user);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSession()) {
            tx = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
                tx.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User findById(Integer id) {
        try (Session session = HibernateUtil.getSession()) {
            return session.get(User.class, id);
        }
    }

    @Override
    public List<User> findAll() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("FROM User", User.class).list();
        }
    }

    @Override
    public User findByUsername(String username) {
        try (Session session = HibernateUtil.getSession()) {
            Query<User> q = session.createQuery("FROM User WHERE username = :uname", User.class);
            q.setParameter("uname", username);
            return q.uniqueResult();
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        try (Session session = HibernateUtil.getSession()) {
            Query<Long> q = session.createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :uname", Long.class);
            q.setParameter("uname", username);
            return q.uniqueResult() > 0;
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        try (Session session = HibernateUtil.getSession()) {
            Query<Long> q = session.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class);
            q.setParameter("email", email);
            return q.uniqueResult() > 0;
        }
    }
}