package lk.ijse.therapycenter.dao;

import lk.ijse.therapycenter.dao.custom.impl.*;

public class DAOFactory {

    private DAOFactory() {}

    public enum DAOTypes {
        USER, PATIENT, THERAPIST, THERAPY_PROGRAM, THERAPY_SESSION, PAYMENT
    }

    @SuppressWarnings("unchecked")
    public static <T> T getDAO(DAOTypes type) {
        return switch (type) {
            case USER            -> (T) new UserDAOImpl();
            case PATIENT         -> (T) new PatientDAOImpl();
            case THERAPIST       -> (T) new TherapistDAOImpl();
            case THERAPY_PROGRAM -> (T) new TherapyProgramDAOImpl();
            case THERAPY_SESSION -> (T) new TherapySessionDAOImpl();
            case PAYMENT         -> (T) new PaymentDAOImpl();
        };
    }
}