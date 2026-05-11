package lk.ijse.therapycenter.dao.custom.impl;

import lk.ijse.therapycenter.config.HibernateUtil;
import lk.ijse.therapycenter.dao.custom.TherapistDAO;
import lk.ijse.therapycenter.entity.Therapist;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class TherapistDAOImpl implements TherapistDAO {

    @Override
    public boolean save(Therapist therapist) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSession()) {
            tx = session.beginTransaction();
            session.persist(therapist);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Therapist therapist) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSession()) {
            tx = session.beginTransaction();
            session.merge(therapist);
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
            Therapist t = session.get(Therapist.class, id);
            if (t != null) {
                session.remove(t);
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
    public Therapist findById(Integer id) {
        try (Session session = HibernateUtil.getSession()) {
            return session.get(Therapist.class, id);
        }
    }

    @Override
    public List<Therapist> findAll() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("FROM Therapist ORDER BY fullName", Therapist.class).list();
        }
    }

    @Override
    public List<Therapist> findAvailable() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery(
                    "FROM Therapist t WHERE t.available = true ORDER BY t.fullName", Therapist.class).list();
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        try (Session session = HibernateUtil.getSession()) {
            Query<Long> q = session.createQuery(
                    "SELECT COUNT(t) FROM Therapist t WHERE t.email = :email", Long.class);
            q.setParameter("email", email);
            return q.uniqueResult() > 0;
        }
    }
}