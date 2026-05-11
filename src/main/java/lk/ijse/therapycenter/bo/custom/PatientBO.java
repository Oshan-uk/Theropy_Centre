package lk.ijse.therapycenter.bo.custom;

import lk.ijse.therapycenter.dto.PatientDTO;
import lk.ijse.therapycenter.dto.TherapyProgramDTO;
import lk.ijse.therapycenter.exception.RegistrationException;

import java.util.List;
import java.util.Map;

public interface PatientBO {
    boolean addPatient(PatientDTO dto) throws RegistrationException;
    boolean updatePatient(PatientDTO dto) throws RegistrationException;
    boolean deletePatient(int id);
    PatientDTO findById(int id);
    List<PatientDTO> findAll();
    List<PatientDTO> searchByName(String keyword);

    Map<PatientDTO, List<TherapyProgramDTO>> getPatientsWithPrograms();

    List<PatientDTO> getPatientsEnrolledInAllPrograms();
}