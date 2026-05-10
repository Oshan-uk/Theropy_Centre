package lk.ijse.therapycenter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "patient")

@NoArgsConstructor
@AllArgsConstructor
@Data

public class Patient {

    @Id
    private String patientId;

    private String name;
    private String email;
    private String phone;
    private String address;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)

    private List<TherapySession> sessions;
}