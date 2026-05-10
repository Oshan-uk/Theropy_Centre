package lk.ijse.therapycenter.bo.custom.impl;

import lk.ijse.therapycenter.bo.custom.PatientBO;
import lk.ijse.therapycenter.dao.custom.PatientDAO;
import lk.ijse.therapycenter.dao.custom.impl.PatientDAOImpl;
import lk.ijse.therapycenter.dto.PatientDTO;
import lk.ijse.therapycenter.entity.Patient;

public class PatientBOImpl implements PatientBO {

    PatientDAO patientDAO =
            new PatientDAOImpl();

    @Override
    public boolean savePatient(PatientDTO dto) {

        Patient patient = new Patient(
                dto.getPatientId(),
                dto.getName(),
                dto.getEmail(),
                dto.getPhone(),
                dto.getAddress()
        );

        return patientDAO.save(patient);
    }
}