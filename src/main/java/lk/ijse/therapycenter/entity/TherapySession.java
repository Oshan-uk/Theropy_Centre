package lk.ijse.therapycenter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity

@NoArgsConstructor
@AllArgsConstructor
@Data

public class TherapySession {

    @Id
    private String sessionId;

    private String sessionDate;
    private String sessionTime;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "therapist_id")
    private Therapist therapist;

    @ManyToOne
    @JoinColumn(name = "program_id")
    private TherapyProgram program;
}