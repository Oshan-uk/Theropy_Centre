package lk.ijse.therapycenter.bo.custom.impl;

import lk.ijse.therapycenter.bo.custom.TherapistBO;
import lk.ijse.therapycenter.dao.custom.TherapistDAO;
import lk.ijse.therapycenter.dao.custom.impl.TherapistDAOImpl;
import lk.ijse.therapycenter.dto.TherapistDTO;
import lk.ijse.therapycenter.entity.Therapist;
import lk.ijse.therapycenter.exception.RegistrationException;
import lk.ijse.therapycenter.util.ValidationUtil;

import java.util.List;
import java.util.stream.Collectors;

public class TherapistBOImpl implements TherapistBO {

    private final TherapistDAO therapistDAO = new TherapistDAOImpl();

    @Override
    public boolean addTherapist(TherapistDTO dto) throws RegistrationException {
        validate(dto);
        if (therapistDAO.existsByEmail(dto.getEmail())) {
            throw new RegistrationException("A therapist with that email already exists.");
        }
        Therapist t = new Therapist(dto.getFullName(), dto.getEmail(), dto.getPhone(), dto.getSpecialization());
        t.setAvailable(dto.isAvailable());
        return therapistDAO.save(t);
    }

    @Override
    public boolean updateTherapist(TherapistDTO dto) throws RegistrationException {
        validate(dto);
        Therapist existing = therapistDAO.findById(dto.getId());
        if (existing == null) return false;

        if (!existing.getEmail().equals(dto.getEmail()) && therapistDAO.existsByEmail(dto.getEmail())) {
            throw new RegistrationException("That email is already in use by another therapist.");
        }

        existing.setFullName(dto.getFullName());
        existing.setEmail(dto.getEmail());
        existing.setPhone(dto.getPhone());
        existing.setSpecialization(dto.getSpecialization());
        existing.setAvailable(dto.isAvailable());
        return therapistDAO.update(existing);
    }

    @Override
    public boolean deleteTherapist(int id) {
        return therapistDAO.delete(id);
    }

    @Override
    public TherapistDTO findById(int id) {
        Therapist t = therapistDAO.findById(id);
        return t != null ? toDTO(t) : null;
    }

    @Override
    public List<TherapistDTO> findAll() {
        return therapistDAO.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<TherapistDTO> findAvailable() {
        return therapistDAO.findAvailable().stream().map(this::toDTO).collect(Collectors.toList());
    }

    private void validate(TherapistDTO dto) throws RegistrationException {
        if (dto.getFullName() == null || dto.getFullName().trim().isEmpty()) {
            throw new RegistrationException("Therapist name is required.");
        }
        if (!ValidationUtil.isValidEmail(dto.getEmail())) {
            throw new RegistrationException("Please enter a valid email.");
        }
        if (!ValidationUtil.isValidSriLankaPhone(dto.getPhone())) {
            throw new RegistrationException("Please enter a valid phone number (e.g. 0771234567).");
        }
        if (dto.getSpecialization() == null || dto.getSpecialization().trim().isEmpty()) {
            throw new RegistrationException("Specialization is required.");
        }
    }

    private TherapistDTO toDTO(Therapist t) {
        return new TherapistDTO(t.getId(), t.getFullName(), t.getEmail(),
                t.getPhone(), t.getSpecialization(), t.isAvailable());
    }
}