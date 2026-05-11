package lk.ijse.therapycenter.dao.custom.impl;

import lk.ijse.therapycenter.config.FactoryConfiguration;
import lk.ijse.therapycenter.dao.custom.TherapyProgramDAO;
import lk.ijse.therapycenter.entity.TherapyProgram;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class TherapyProgramDAOImpl implements TherapyProgramDAO {

    @Override
    public boolean save(TherapyProgram program) {
        Transaction tx = null;
        try (Session session = FactoryConfiguration.getSession()) {
            tx = session.beginTransaction();
            session.persist(program);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(TherapyProgram program) {
        Transaction tx = null;
        try (Session session = FactoryConfiguration.getSession()) {
            tx = session.beginTransaction();
            session.merge(program);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String programId) {
        Transaction tx = null;
        try (Session session = FactoryConfiguration.getSession()) {
            tx = session.beginTransaction();
            TherapyProgram p = session.get(TherapyProgram.class, programId);
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
    public TherapyProgram findById(String programId) {
        try (Session session = FactoryConfiguration.getSession()) {
            return session.get(TherapyProgram.class, programId);
        }
    }

    @Override
    public List<TherapyProgram> findAll() {
        try (Session session = FactoryConfiguration.getSession()) {
            return session.createQuery("FROM TherapyProgram ORDER BY programName", TherapyProgram.class).list();
        }
    }

    @Override
    public boolean existsById(String programId) {
        try (Session session = FactoryConfiguration.getSession()) {
            return session.get(TherapyProgram.class, programId) != null;
        }
    }
}