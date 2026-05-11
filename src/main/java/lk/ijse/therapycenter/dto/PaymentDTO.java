package lk.ijse.therapycenter.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class PaymentDTO {
    private int id;
    private int patientId;
    private String patientName;
    private int sessionId;
    private double amount;
    private LocalDate paymentDate;
    private String paymentStatus;
    private String paymentMethod;
    private String invoiceNumber;

    public PaymentDTO() {}

}