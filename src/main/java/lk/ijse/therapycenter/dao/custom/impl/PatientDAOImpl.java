package lk.ijse.therapycenter.dao.custom.impl;

import lk.ijse.therapycenter.config.HibernateUtil;
import lk.ijse.therapycenter.dao.custom.PatientDAO;
import lk.ijse.therapycenter.entity.Patient;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class PatientDAOImpl implements PatientDAO {

    @Override
    public boolean save(Patient patient) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSession()) {
            tx = session.beginTransaction();
            session.persist(patient);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Patient patient) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSession()) {
            tx = session.beginTransaction();
            session.merge(patient);
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
            Patient p = session.get(Patient.class, id);
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
    public Patient findById(Integer id) {
        try (Session session = HibernateUtil.getSession()) {
            return session.get(Patient.class, id);
        }
    }

    @Override
    public List<Patient> findAll() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("FROM Patient ORDER BY fullName", Patient.class).list();
        }
    }

    @Override
    public List<Patient> searchByName(String keyword) {
        try (Session session = HibernateUtil.getSession()) {
            Query<Patient> q = session.createQuery(
                    "FROM Patient p WHERE LOWER(p.fullName) LIKE :kw ORDER BY p.fullName", Patient.class);
            q.setParameter("kw", "%" + keyword.toLowerCase() + "%");
            return q.list();
        }
    }


    @Override
    public List<Patient> findPatientsEnrolledInAllPrograms() {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "SELECT p FROM Patient p " +
                    "WHERE (SELECT COUNT(DISTINCT s.therapyProgram.programId) " +
                    "       FROM TherapySession s WHERE s.patient = p) = " +
                    "      (SELECT COUNT(tp) FROM TherapyProgram tp)";

            return session.createQuery(hql, Patient.class).list();
        }
    }


    @Override
    public Patient findPatientWithPrograms(int patientId) {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "SELECT DISTINCT p FROM Patient p " +
                    "LEFT JOIN FETCH p.sessions s " +
                    "LEFT JOIN FETCH s.therapyProgram " +
                    "WHERE p.id = :pid";

            Query<Patient> q = session.createQuery(hql, Patient.class);
            q.setParameter("pid", patientId);
            return q.uniqueResult();
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        try (Session session = HibernateUtil.getSession()) {
            Query<Long> q = session.createQuery(
                    "SELECT COUNT(p) FROM Patient p WHERE p.email = :email", Long.class);
            q.setParameter("email", email);
            return q.uniqueResult() > 0;
        }
    }
}