package lk.ijse.therapycenter.dao.custom;

import lk.ijse.therapycenter.dao.CrudDAO;
import lk.ijse.therapycenter.entity.TherapyProgram;
import java.util.List;

public interface TherapyProgramDAO extends CrudDAO<TherapyProgram, String> {
    boolean existsById(String programId);
}