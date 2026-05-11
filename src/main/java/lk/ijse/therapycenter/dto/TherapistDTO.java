package lk.ijse.therapycenter.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TherapistDTO {
    private int id;
    private String fullName;
    private String email;
    private String phone;
    private String specialization;
    private boolean available;

    public TherapistDTO() {}

    public TherapistDTO(int id, String fullName, String email, String phone, String specialization, boolean available) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.specialization = specialization;
        this.available = available;
    }

}