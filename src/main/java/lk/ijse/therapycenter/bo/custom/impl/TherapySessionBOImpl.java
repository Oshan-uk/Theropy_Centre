package lk.ijse.therapycenter.bo.custom.impl;

import lk.ijse.therapycenter.bo.custom.TherapySessionBO;
import lk.ijse.therapycenter.dao.*;
import lk.ijse.therapycenter.dao.custom.PatientDAO;
import lk.ijse.therapycenter.dao.custom.TherapistDAO;
import lk.ijse.therapycenter.dao.custom.TherapyProgramDAO;
import lk.ijse.therapycenter.dao.custom.TherapySessionDAO;
import lk.ijse.therapycenter.dao.custom.impl.*;
import lk.ijse.therapycenter.dto.TherapySessionDTO;
import lk.ijse.therapycenter.entity.*;
import lk.ijse.therapycenter.exception.SessionConflictException;

import java.util.List;
import java.util.stream.Collectors;

public class TherapySessionBOImpl implements TherapySessionBO {

    private final TherapySessionDAO sessionDAO = new TherapySessionDAOImpl();
    private final PatientDAO patientDAO = new PatientDAOImpl();
    private final TherapistDAO therapistDAO = new TherapistDAOImpl();
    private final TherapyProgramDAO programDAO = new TherapyProgramDAOImpl();

    @Override
    public boolean bookSession(TherapySessionDTO dto) throws SessionConflictException {
        if (sessionDAO.hasConflict(dto.getTherapistId(), dto.getSessionDate(), dto.getSessionTime(), 0)) {
            throw new SessionConflictException(
                    "That therapist already has a session at " + dto.getSessionDate() +
                            " " + dto.getSessionTime() + ". Please pick a different time.");
        }

        Patient patient = patientDAO.findById(dto.getPatientId());
        Therapist therapist = therapistDAO.findById(dto.getTherapistId());
        TherapyProgram program = programDAO.findById(dto.getProgramId());

        if (patient == null) throw new SessionConflictException("Patient not found.");
        if (therapist == null) throw new SessionConflictException("Therapist not found.");
        if (program == null) throw new SessionConflictException("Therapy program not found.");
        if (!therapist.isAvailable()) throw new SessionConflictException(
                therapist.getFullName() + " is currently unavailable.");

        TherapySession session = new TherapySession(patient, therapist, program,
                dto.getSessionDate(), dto.getSessionTime());
        session.setNotes(dto.getNotes());

        return sessionDAO.save(session);
    }

    @Override
    public boolean rescheduleSession(int sessionId, TherapySessionDTO dto) throws SessionConflictException {
        TherapySession existing = sessionDAO.findById(sessionId);
        if (existing == null) return false;

        if (sessionDAO.hasConflict(dto.getTherapistId(), dto.getSessionDate(), dto.getSessionTime(), sessionId)) {
            throw new SessionConflictException("The therapist is busy at that new time. Please pick another.");
        }

        existing.setSessionDate(dto.getSessionDate());
        existing.setSessionTime(dto.getSessionTime());
        existing.setNotes(dto.getNotes());
        return sessionDAO.update(existing);
    }

    @Override
    public boolean cancelSession(int sessionId) {
        TherapySession session = sessionDAO.findById(sessionId);
        if (session == null) return false;
        session.setStatus("CANCELLED");
        return sessionDAO.update(session);
    }

    @Override
    public TherapySessionDTO findById(int id) {
        TherapySession s = sessionDAO.findById(id);
        return s != null ? toDTO(s) : null;
    }

    @Override
    public List<TherapySessionDTO> findByPatient(int patientId) {
        return sessionDAO.findByPatient(patientId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<TherapySessionDTO> findByTherapist(int therapistId) {
        return sessionDAO.findByTherapist(therapistId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<TherapySessionDTO> findAll() {
        return sessionDAO.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    private TherapySessionDTO toDTO(TherapySession s) {
        TherapySessionDTO dto = new TherapySessionDTO();
        dto.setId(s.getId());
        dto.setPatientId(s.getPatient().getId());
        dto.setPatientName(s.getPatient().getFullName());
        dto.setTherapistId(s.getTherapist().getId());
        dto.setTherapistName(s.getTherapist().getFullName());
        dto.setProgramId(s.getTherapyProgram().getProgramId());
        dto.setProgramName(s.getTherapyProgram().getProgramName());
        dto.setSessionDate(s.getSessionDate());
        dto.setSessionTime(s.getSessionTime());
        dto.setStatus(s.getStatus());
        dto.setNotes(s.getNotes());
        return dto;
    }
}