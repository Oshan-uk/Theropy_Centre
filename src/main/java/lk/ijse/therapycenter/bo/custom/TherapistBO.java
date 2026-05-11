package lk.ijse.therapycenter.bo.custom;

import lk.ijse.therapycenter.dto.TherapistDTO;
import lk.ijse.therapycenter.exception.RegistrationException;

import java.util.List;

public interface TherapistBO {
    boolean addTherapist(TherapistDTO dto) throws RegistrationException;
    boolean updateTherapist(TherapistDTO dto) throws RegistrationException;
    boolean deleteTherapist(int id);
    TherapistDTO findById(int id);
    List<TherapistDTO> findAll();
    List<TherapistDTO> findAvailable();
}