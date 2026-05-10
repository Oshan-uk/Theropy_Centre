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

public class Payment {

    @Id
    private String paymentId;

    private double amount;

    private String paymentDate;
}