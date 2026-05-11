package lk.ijse.therapycenter.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
@NoArgsConstructor
public class TherapySessionDTO {
    private int id;
    private int patientId;
    private String patientName;
    private int therapistId;
    private String therapistName;
    private String programId;
    private String programName;
    private LocalDate sessionDate;
    private LocalTime sessionTime;
    private String status;
    private String notes;


}