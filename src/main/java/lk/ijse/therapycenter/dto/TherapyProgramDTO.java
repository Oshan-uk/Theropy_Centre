package lk.ijse.therapycenter.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TherapyProgramDTO {
    private String programId;
    private String programName;
    private String duration;
    private double fee;
    private String description;

    public TherapyProgramDTO() {}

    public TherapyProgramDTO(String programId, String programName, String duration, double fee, String description) {
        this.programId = programId;
        this.programName = programName;
        this.duration = duration;
        this.fee = fee;
        this.description = description;
    }

}