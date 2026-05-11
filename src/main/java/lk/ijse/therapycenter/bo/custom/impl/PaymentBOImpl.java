package lk.ijse.therapycenter.bo.custom.impl;

import lk.ijse.therapycenter.bo.custom.PaymentBO;
import lk.ijse.therapycenter.dao.custom.PatientDAO;
import lk.ijse.therapycenter.dao.custom.PaymentDAO;
import lk.ijse.therapycenter.dao.custom.TherapySessionDAO;
import lk.ijse.therapycenter.dao.custom.impl.PatientDAOImpl;
import lk.ijse.therapycenter.dao.custom.impl.PaymentDAOImpl;
import lk.ijse.therapycenter.dao.custom.impl.TherapySessionDAOImpl;
import lk.ijse.therapycenter.dto.PaymentDTO;
import lk.ijse.therapycenter.entity.Patient;
import lk.ijse.therapycenter.entity.Payment;
import lk.ijse.therapycenter.entity.TherapySession;
import lk.ijse.therapycenter.exception.PaymentException;

import java.util.List;
import java.util.stream.Collectors;

public class PaymentBOImpl implements PaymentBO {

    private final PaymentDAO paymentDAO = new PaymentDAOImpl();
    private final PatientDAO patientDAO = new PatientDAOImpl();
    private final TherapySessionDAO sessionDAO = new TherapySessionDAOImpl();

    @Override
    public boolean processPayment(PaymentDTO dto) throws PaymentException {
        if (dto.getAmount() <= 0) {
            throw new PaymentException("Payment amount must be greater than zero.");
        }
        if (dto.getPaymentMethod() == null || dto.getPaymentMethod().trim().isEmpty()) {
            throw new PaymentException("Payment method is required.");
        }

        Patient patient = patientDAO.findById(dto.getPatientId());
        TherapySession session = sessionDAO.findById(dto.getSessionId());

        if (patient == null) throw new PaymentException("Patient not found.");
        if (session == null) throw new PaymentException("Therapy session not found.");
        if ("CANCELLED".equals(session.getStatus())) {
            throw new PaymentException("Cannot process payment for a cancelled session.");
        }

        Payment payment = new Payment(patient, session, dto.getAmount(), dto.getPaymentMethod());
        payment.setPaymentStatus("PAID");
        return paymentDAO.save(payment);
    }

    @Override
    public boolean markAsPaid(int paymentId) throws PaymentException {
        Payment payment = paymentDAO.findById(paymentId);
        if (payment == null) throw new PaymentException("Payment record not found.");
        if ("PAID".equals(payment.getPaymentStatus())) {
            throw new PaymentException("This payment has already been marked as paid.");
        }
        payment.setPaymentStatus("PAID");
        return paymentDAO.update(payment);
    }

    @Override
    public List<PaymentDTO> findByPatient(int patientId) {
        return paymentDAO.findByPatient(patientId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<PaymentDTO> findPending() {
        return paymentDAO.findPending().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<PaymentDTO> findAll() {
        return paymentDAO.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public PaymentDTO findByInvoice(String invoiceNumber) {
        Payment p = paymentDAO.findByInvoiceNumber(invoiceNumber);
        return p != null ? toDTO(p) : null;
    }

    private PaymentDTO toDTO(Payment p) {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(p.getId());
        dto.setPatientId(p.getPatient().getId());
        dto.setPatientName(p.getPatient().getFullName());
        dto.setSessionId(p.getTherapySession().getId());
        dto.setAmount(p.getAmount());
        dto.setPaymentDate(p.getPaymentDate());
        dto.setPaymentStatus(p.getPaymentStatus());
        dto.setPaymentMethod(p.getPaymentMethod());
        dto.setInvoiceNumber(p.getInvoiceNumber());
        return dto;
    }
}