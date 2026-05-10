package lk.ijse.therapycenter.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity

@NoArgsConstructor
@AllArgsConstructor
@Data

public class TherapyProgram {

    @Id
    private String programId;

    private String name;
    private String duration;
    private double fee;
}