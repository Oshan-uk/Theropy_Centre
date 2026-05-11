package lk.ijse.therapycenter.bo.custom.impl;

import lk.ijse.therapycenter.bo.custom.TherapyProgramBO;
import lk.ijse.therapycenter.dao.custom.TherapyProgramDAO;
import lk.ijse.therapycenter.dao.custom.impl.TherapyProgramDAOImpl;
import lk.ijse.therapycenter.dto.TherapyProgramDTO;
import lk.ijse.therapycenter.entity.TherapyProgram;
import lk.ijse.therapycenter.exception.RegistrationException;

import java.util.List;
import java.util.stream.Collectors;

public class TherapyProgramBOImpl implements TherapyProgramBO {

    private final TherapyProgramDAO programDAO = new TherapyProgramDAOImpl();

    @Override
    public boolean addProgram(TherapyProgramDTO dto) throws RegistrationException {
        if (dto.getProgramId() == null || dto.getProgramId().trim().isEmpty()) {
            throw new RegistrationException("Program ID is required.");
        }
        if (dto.getProgramName() == null || dto.getProgramName().trim().isEmpty()) {
            throw new RegistrationException("Program name is required.");
        }
        if (dto.getFee() <= 0) {
            throw new RegistrationException("Program fee must be greater than zero.");
        }
        if (programDAO.existsById(dto.getProgramId())) {
            throw new RegistrationException("A program with ID " + dto.getProgramId() + " already exists.");
        }

        TherapyProgram program = new TherapyProgram(
                dto.getProgramId(), dto.getProgramName(),
                dto.getDuration(), dto.getFee(), dto.getDescription());
        return programDAO.save(program);
    }

    @Override
    public boolean updateProgram(TherapyProgramDTO dto) {
        TherapyProgram existing = programDAO.findById(dto.getProgramId());
        if (existing == null) return false;

        existing.setProgramName(dto.getProgramName());
        existing.setDuration(dto.getDuration());
        existing.setFee(dto.getFee());
        existing.setDescription(dto.getDescription());
        return programDAO.update(existing);
    }

    @Override
    public boolean deleteProgram(String programId) {
        return programDAO.delete(programId);
    }

    @Override
    public TherapyProgramDTO findById(String programId) {
        TherapyProgram p = programDAO.findById(programId);
        return p != null ? toDTO(p) : null;
    }

    @Override
    public List<TherapyProgramDTO> findAll() {
        return programDAO.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    private TherapyProgramDTO toDTO(TherapyProgram p) {
        return new TherapyProgramDTO(p.getProgramId(), p.getProgramName(),
                p.getDuration(), p.getFee(), p.getDescription());
    }
}