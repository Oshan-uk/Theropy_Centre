package lk.ijse.therapycenter.bo;

import lk.ijse.therapycenter.bo.custom.impl.*;


public class BOFactory {

    public enum BOTypes {
        USER, PATIENT, THERAPIST, THERAPY_PROGRAM, THERAPY_SESSION, PAYMENT
    }

    private BOFactory() {}

    @SuppressWarnings("unchecked")
    public static <T> T getBO(BOTypes type) {
        switch (type) {
            case USER:            return (T) new UserBOImpl();
            case PATIENT:         return (T) new PatientBOImpl();
            case THERAPIST:       return (T) new TherapistBOImpl();
            case THERAPY_PROGRAM: return (T) new TherapyProgramBOImpl();
            case THERAPY_SESSION: return (T) new TherapySessionBOImpl();
            case PAYMENT:         return (T) new PaymentBOImpl();
            default: throw new IllegalArgumentException("Unknown BO type: " + type);
        }
    }
}