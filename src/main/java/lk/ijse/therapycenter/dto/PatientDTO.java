package lk.ijse.therapycenter.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class PatientDTO {
    private int id;
    private String fullName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private LocalDate registrationDate;
    private String medicalHistory;
    private String address;

    public PatientDTO() {}

    public PatientDTO(int id, String fullName, String email, String phone,
                      LocalDate dateOfBirth, String address) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.registrationDate = LocalDate.now();
    }

}