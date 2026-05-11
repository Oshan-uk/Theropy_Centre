package lk.ijse.therapycenter.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;


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

    public String getProgramId() { return programId; }
    public void setProgramId(String programId) { this.programId = programId; }

    public String getProgramName() { return programName; }
    public void setProgramName(String programName) { this.programName = programName; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public double getFee() { return fee; }
    public void setFee(double fee) { this.fee = fee; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<TherapySession> getSessions() { return sessions; }
    public void setSessions(List<TherapySession> sessions) { this.sessions = sessions; }
}