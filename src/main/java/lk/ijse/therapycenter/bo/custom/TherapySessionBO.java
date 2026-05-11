package lk.ijse.therapycenter.bo.custom;

import lk.ijse.therapycenter.dto.TherapySessionDTO;
import lk.ijse.therapycenter.exception.SessionConflictException;

import java.util.List;

public interface TherapySessionBO {
    boolean bookSession(TherapySessionDTO dto) throws SessionConflictException;
    boolean rescheduleSession(int sessionId, TherapySessionDTO dto) throws SessionConflictException;
    boolean cancelSession(int sessionId);
    TherapySessionDTO findById(int id);
    List<TherapySessionDTO> findByPatient(int patientId);
    List<TherapySessionDTO> findByTherapist(int therapistId);
    List<TherapySessionDTO> findAll();
}