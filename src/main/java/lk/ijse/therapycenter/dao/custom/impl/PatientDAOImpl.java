package lk.ijse.therapycenter.dao.custom.impl;

import lk.ijse.therapycenter.config.FactoryConfiguration;
import lk.ijse.therapycenter.dao.custom.PatientDAO;
import lk.ijse.therapycenter.entity.Patient;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class PatientDAOImpl implements PatientDAO {

    @Override
    public boolean save(Patient patient) {

        Session session = FactoryConfiguration.getInstance().getSession();

        Transaction transaction = session.beginTransaction();

        session.persist(patient);

        transaction.commit();

        session.close();

        return true;
    }

    @Override
    public boolean update(Patient patient) {
        return false;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    @Override
    public Patient search(String id) {
        return null;
    }

    @Override
    public List<Patient> getAll() {
        return null;
    }
}