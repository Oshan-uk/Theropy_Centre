package lk.ijse.therapycenter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity

@NoArgsConstructor
@AllArgsConstructor
@Data

public class Therapist {

    @Id
    private String therapistId;

    private String name;
    private String specialization;
    private String availability;

    @OneToMany(mappedBy = "therapist", cascade = CascadeType.ALL)

    private List<TherapySession> sessions;
}