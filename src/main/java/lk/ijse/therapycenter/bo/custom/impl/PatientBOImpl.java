package lk.ijse.therapycenter.bo.custom.impl;

import lk.ijse.therapycenter.bo.custom.PatientBO;
import lk.ijse.therapycenter.dao.custom.PatientDAO;
import lk.ijse.therapycenter.dto.PatientDTO;
import lk.ijse.therapycenter.dto.TherapyProgramDTO;
import lk.ijse.therapycenter.entity.Patient;
import lk.ijse.therapycenter.entity.TherapySession;
import lk.ijse.therapycenter.exception.RegistrationException;
import lk.ijse.therapycenter.util.ValidationUtil;
import lk.ijse.therapycenter.dao.DAOFactory;
import lk.ijse.therapycenter.dao.DAOFactory.DAOTypes;

import java.util.*;
import java.util.stream.Collectors;

public class PatientBOImpl implements PatientBO {

    private final PatientDAO patientDAO = DAOFactory.getDAO(DAOTypes.PATIENT);

    @Override
    public boolean addPatient(PatientDTO dto) throws RegistrationException {
        validatePatient(dto);

        if (patientDAO.existsByEmail(dto.getEmail())) {
            throw new RegistrationException("A patient with that email is already registered.");
        }

        Patient patient = toEntity(dto);
        return patientDAO.save(patient);
    }

    @Override
    public boolean updatePatient(PatientDTO dto) throws RegistrationException {
        validatePatient(dto);
        Patient existing = patientDAO.findById(dto.getId());
        if (existing == null) return false;

        if (!existing.getEmail().equals(dto.getEmail()) && patientDAO.existsByEmail(dto.getEmail())) {
            throw new RegistrationException("That email is already used by another patient.");
        }

        existing.setFullName(dto.getFullName());
        existing.setEmail(dto.getEmail());
        existing.setPhone(dto.getPhone());
        existing.setDateOfBirth(dto.getDateOfBirth());
        existing.setAddress(dto.getAddress());
        existing.setMedicalHistory(dto.getMedicalHistory());
        return patientDAO.update(existing);
    }

    @Override
    public boolean deletePatient(int id) {
        return patientDAO.delete(id);
    }

    @Override
    public PatientDTO findById(int id) {
        Patient p = patientDAO.findById(id);
        return p != null ? toDTO(p) : null;
    }

    @Override
    public List<PatientDTO> findAll() {
        return patientDAO.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<PatientDTO> searchByName(String keyword) {
        return patientDAO.searchByName(keyword).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public Map<PatientDTO, List<TherapyProgramDTO>> getPatientsWithPrograms() {
        List<Patient> allPatients = patientDAO.findAll();
        Map<PatientDTO, List<TherapyProgramDTO>> result = new LinkedHashMap<>();

        for (Patient p : allPatients) {
            Patient fullPatient = patientDAO.findPatientWithPrograms(p.getId());
            if (fullPatient == null) continue;

            List<TherapyProgramDTO> programs = fullPatient.getSessions().stream()
                    .map(TherapySession::getTherapyProgram)
                    .filter(Objects::nonNull)
                    .distinct()
                    .map(prog -> new TherapyProgramDTO(
                            prog.getProgramId(), prog.getProgramName(),
                            prog.getDuration(), prog.getFee(), prog.getDescription()))
                    .collect(Collectors.toList());

            result.put(toDTO(fullPatient), programs);
        }
        return result;
    }

    @Override
    public List<PatientDTO> getPatientsEnrolledInAllPrograms() {
        return patientDAO.findPatientsEnrolledInAllPrograms().stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    private void validatePatient(PatientDTO dto) throws RegistrationException {
        if (dto.getFullName() == null || dto.getFullName().trim().isEmpty()) {
            throw new RegistrationException("Patient name is required.");
        }
        if (!ValidationUtil.isValidEmail(dto.getEmail())) {
            throw new RegistrationException("Please enter a valid email address.");
        }
        if (!ValidationUtil.isValidPhone(dto.getPhone())) {
            throw new RegistrationException("Please enter a valid Sri Lanka phone number (e.g. 0771234567).");
        }
        if (dto.getDateOfBirth() == null) {
            throw new RegistrationException("Date of birth is required.");
        }
        if (dto.getAddress() == null || dto.getAddress().trim().isEmpty()) {
            throw new RegistrationException("Address is required.");
        }
    }

    private PatientDTO toDTO(Patient p) {
        PatientDTO dto = new PatientDTO(p.getId(), p.getFullName(), p.getEmail(),
                p.getPhone(), p.getDateOfBirth(), p.getAddress());
        dto.setRegistrationDate(p.getRegistrationDate());
        dto.setMedicalHistory(p.getMedicalHistory());
        return dto;
    }

    private Patient toEntity(PatientDTO dto) {
        Patient p = new Patient(dto.getFullName(), dto.getEmail(), dto.getPhone(),
                dto.getDateOfBirth(), dto.getAddress());
        p.setMedicalHistory(dto.getMedicalHistory());
        return p;
    }
}