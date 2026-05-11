package lk.ijse.therapycenter.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@Entity
@Table(name = "therapy_programs")
public class TherapyProgram {

    @Id
    @Column(length = 10)
    private String programId;

    @Column(nullable = false, length = 150)
    private String programName;

    @Column(nullable = false, length = 50)
    private String duration;

    @Column(nullable = false)
    private double fee;

    @Column(length = 500)
    private String description;

    @OneToMany(mappedBy = "therapyProgram", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TherapySession> sessions = new ArrayList<>();

    public TherapyProgram() {}

    public TherapyProgram(String programId, String programName, String duration, double fee, String description) {
        this.programId = programId;
        this.programName = programName;
        this.duration = duration;
        this.fee = fee;
        this.description = description;
    }

}