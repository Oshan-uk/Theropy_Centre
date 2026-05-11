package lk.ijse.therapycenter.dao.custom.impl;

import lk.ijse.therapycenter.config.HibernateUtil;
import lk.ijse.therapycenter.dao.custom.PaymentDAO;
import lk.ijse.therapycenter.entity.Payment;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class PaymentDAOImpl implements PaymentDAO {

    @Override
    public boolean save(Payment payment) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSession()) {
            tx = session.beginTransaction();
            session.persist(payment);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Payment payment) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSession()) {
            tx = session.beginTransaction();
            session.merge(payment);
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
            Payment p = session.get(Payment.class, id);
            if (p != null) {
                session.remove(p);
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
    public Payment findById(Integer id) {
        try (Session session = HibernateUtil.getSession()) {
            return session.get(Payment.class, id);
        }
    }

    @Override
    public List<Payment> findAll() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery(
                    "FROM Payment ORDER BY paymentDate DESC", Payment.class).list();
        }
    }

    @Override
    public List<Payment> findByPatient(int patientId) {
        try (Session session = HibernateUtil.getSession()) {
            Query<Payment> q = session.createQuery(
                    "FROM Payment p WHERE p.patient.id = :pid ORDER BY p.paymentDate DESC", Payment.class);
            q.setParameter("pid", patientId);
            return q.list();
        }
    }

    @Override
    public List<Payment> findPending() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery(
                    "FROM Payment p WHERE p.paymentStatus = 'PENDING'", Payment.class).list();
        }
    }

    @Override
    public Payment findByInvoiceNumber(String invoiceNumber) {
        try (Session session = HibernateUtil.getSession()) {
            Query<Payment> q = session.createQuery(
                    "FROM Payment p WHERE p.invoiceNumber = :inv", Payment.class);
            q.setParameter("inv", invoiceNumber);
            return q.uniqueResult();
        }
    }
}