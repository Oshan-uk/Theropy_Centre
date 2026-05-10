package lk.ijse.therapycenter.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "patient")
public class Patient {

    @Id
    private String patientId;

    @Setter
    @Getter
    private String name;
    private String email;
    private String phone;
    private String address;

    public Patient() {
    }

    public Patient(String patientId, String name, String email, String phone, String address) {

        this.patientId = patientId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

}