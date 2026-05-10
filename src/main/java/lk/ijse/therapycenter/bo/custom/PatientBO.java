package lk.ijse.therapycenter.bo.custom;

import lk.ijse.therapycenter.dto.PatientDTO;

public interface PatientBO {

    boolean savePatient(PatientDTO dto);
}