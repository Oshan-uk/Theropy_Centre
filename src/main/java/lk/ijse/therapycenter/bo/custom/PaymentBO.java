package lk.ijse.therapycenter.bo.custom;

import lk.ijse.therapycenter.dto.PaymentDTO;
import lk.ijse.therapycenter.exception.PaymentException;

import java.util.List;

public interface PaymentBO {
    boolean processPayment(PaymentDTO dto) throws PaymentException;
    boolean markAsPaid(int paymentId) throws PaymentException;
    List<PaymentDTO> findByPatient(int patientId);
    List<PaymentDTO> findPending();
    List<PaymentDTO> findAll();
    PaymentDTO findByInvoice(String invoiceNumber);
}