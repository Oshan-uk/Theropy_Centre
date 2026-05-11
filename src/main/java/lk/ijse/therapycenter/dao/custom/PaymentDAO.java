package lk.ijse.therapycenter.dao.custom;

import lk.ijse.therapycenter.dao.CrudDAO;
import lk.ijse.therapycenter.entity.Payment;
import java.util.List;

public interface PaymentDAO extends CrudDAO<Payment, Integer> {
    List<Payment> findByPatient(int patientId);
    List<Payment> findPending();
    Payment findByInvoiceNumber(String invoiceNumber);
}