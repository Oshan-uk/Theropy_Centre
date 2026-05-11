package lk.ijse.therapycenter.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Setter
@Getter
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private TherapySession therapySession;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private LocalDate paymentDate;

    @Column(nullable = false, length = 20)
    private String paymentStatus = "PENDING";

    @Column(length = 30)
    private String paymentMethod;

    @Column(unique = true, length = 20)
    private String invoiceNumber;

    public Payment() {}

    public Payment(Patient patient, TherapySession therapySession, double amount, String paymentMethod) {
        this.patient = patient;
        this.therapySession = therapySession;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentDate = LocalDate.now();
        this.invoiceNumber = "INV-" + System.currentTimeMillis();
    }

}