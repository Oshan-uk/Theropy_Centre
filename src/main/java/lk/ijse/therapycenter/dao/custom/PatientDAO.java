package lk.ijse.therapycenter.dao.custom;

import lk.ijse.therapycenter.dao.CrudDAO;
import lk.ijse.therapycenter.entity.Patient;
import java.util.List;

public interface PatientDAO extends CrudDAO<Patient, Integer> {
    List<Patient> searchByName(String keyword);

    List<Patient> findPatientsEnrolledInAllPrograms();

    Patient findPatientWithPrograms(int patientId);

    boolean existsByEmail(String email);
}