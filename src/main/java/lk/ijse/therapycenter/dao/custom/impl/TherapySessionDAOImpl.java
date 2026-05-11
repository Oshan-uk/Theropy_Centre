package lk.ijse.therapycenter.dao.custom.impl;

import lk.ijse.therapycenter.config.FactoryConfiguration;
import lk.ijse.therapycenter.dao.custom.TherapySessionDAO;
import lk.ijse.therapycenter.entity.TherapySession;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TherapySessionDAOImpl implements TherapySessionDAO {

    @Override
    public boolean save(TherapySession ts) {
        Transaction tx = null;
        try (Session session = FactoryConfiguration.getSession()) {
            tx = session.beginTransaction();
            session.persist(ts);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(TherapySession ts) {
        Transaction tx = null;
        try (Session session = FactoryConfiguration.getSession()) {
            tx = session.beginTransaction();
            session.merge(ts);
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
        try (Session session = FactoryConfiguration.getSession()) {
            tx = session.beginTransaction();
            TherapySession ts = session.get(TherapySession.class, id);
            if (ts != null) {
                session.remove(ts);
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
    public TherapySession findById(Integer id) {
        try (Session session = FactoryConfiguration.getSession()) {
            return session.get(TherapySession.class, id);
        }
    }

    @Override
    public List<TherapySession> findAll() {
        try (Session session = FactoryConfiguration.getSession()) {
            return session.createQuery(
                    "FROM TherapySession ORDER BY sessionDate DESC", TherapySession.class).list();
        }
    }

    @Override
    public List<TherapySession> findByPatient(int patientId) {
        try (Session session = FactoryConfiguration.getSession()) {
            Query<TherapySession> q = session.createQuery(
                    "FROM TherapySession ts WHERE ts.patient.id = :pid ORDER BY ts.sessionDate DESC",
                    TherapySession.class);
            q.setParameter("pid", patientId);
            return q.list();
        }
    }

    @Override
    public List<TherapySession> findByTherapist(int therapistId) {
        try (Session session = FactoryConfiguration.getSession()) {
            Query<TherapySession> q = session.createQuery(
                    "FROM TherapySession ts WHERE ts.therapist.id = :tid ORDER BY ts.sessionDate",
                    TherapySession.class);
            q.setParameter("tid", therapistId);
            return q.list();
        }
    }

    @Override
    public boolean hasConflict(int therapistId, LocalDate date, LocalTime time, int excludeSessionId) {
        try (Session session = FactoryConfiguration.getSession()) {
            Query<Long> q = session.createQuery(
                    "SELECT COUNT(ts) FROM TherapySession ts " +
                            "WHERE ts.therapist.id = :tid " +
                            "AND ts.sessionDate = :date " +
                            "AND ts.sessionTime = :time " +
                            "AND ts.id != :excludeId " +
                            "AND ts.status != 'CANCELLED'",
                    Long.class);
            q.setParameter("tid", therapistId);
            q.setParameter("date", date);
            q.setParameter("time", time);
            q.setParameter("excludeId", excludeSessionId);
            return q.uniqueResult() > 0;
        }
    }
}