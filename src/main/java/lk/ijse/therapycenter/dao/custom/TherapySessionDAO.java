package lk.ijse.therapycenter.dao.custom;

import lk.ijse.therapycenter.dao.CrudDAO;
import lk.ijse.therapycenter.entity.TherapySession;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface TherapySessionDAO extends CrudDAO<TherapySession, Integer> {
    List<TherapySession> findByPatient(int patientId);
    List<TherapySession> findByTherapist(int therapistId);

    boolean hasConflict(int therapistId, LocalDate date, LocalTime time, int excludeSessionId);
}