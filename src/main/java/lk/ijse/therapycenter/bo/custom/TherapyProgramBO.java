package lk.ijse.therapycenter.bo.custom;

import lk.ijse.therapycenter.dto.TherapyProgramDTO;
import lk.ijse.therapycenter.exception.RegistrationException;

import java.util.List;

public interface TherapyProgramBO {
    boolean addProgram(TherapyProgramDTO dto) throws RegistrationException;
    boolean updateProgram(TherapyProgramDTO dto);
    boolean deleteProgram(String programId);
    TherapyProgramDTO findById(String programId);
    List<TherapyProgramDTO> findAll();
}