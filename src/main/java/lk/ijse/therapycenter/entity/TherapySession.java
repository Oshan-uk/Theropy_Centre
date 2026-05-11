package lk.ijse.therapycenter.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;


@Setter
@Getter
@Entity
@Table(name = "therapy_sessions")
public class TherapySession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "therapist_id", nullable = false)
    private Therapist therapist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", nullable = false)
    private TherapyProgram therapyProgram;

    @Column(nullable = false)
    private LocalDate sessionDate;

    @Column(nullable = false)
    private LocalTime sessionTime;

    @Column(nullable = false, length = 20)
    private String status = "SCHEDULED";

    @Column(length = 500)
    private String notes;

    public TherapySession() {}

    public TherapySession(Patient patient, Therapist therapist, TherapyProgram therapyProgram, LocalDate sessionDate, LocalTime sessionTime) {
        this.patient = patient;
        this.therapist = therapist;
        this.therapyProgram = therapyProgram;
        this.sessionDate = sessionDate;
        this.sessionTime = sessionTime;
    }

}