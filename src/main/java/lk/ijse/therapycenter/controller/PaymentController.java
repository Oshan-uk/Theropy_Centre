package lk.ijse.therapycenter.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.therapycenter.bo.*;
import lk.ijse.therapycenter.bo.custom.PatientBO;
import lk.ijse.therapycenter.bo.custom.PaymentBO;
import lk.ijse.therapycenter.bo.custom.TherapySessionBO;
import lk.ijse.therapycenter.dto.*;
import lk.ijse.therapycenter.exception.PaymentException;

import java.time.LocalDate;
import java.util.List;

public class PaymentController {

    @FXML private ComboBox<PatientDTO> cmbPatient;
    @FXML private ComboBox<TherapySessionDTO> cmbSession;
    @FXML private TextField txtAmount;
    @FXML private ComboBox<String> cmbPaymentMethod;
    @FXML private Label lblInvoice;
    @FXML private Label lblMessage;

    @FXML private TableView<PaymentDTO> tblPayments;
    @FXML private TableColumn<PaymentDTO, Integer> colId;
    @FXML private TableColumn<PaymentDTO, String> colPatient;
    @FXML private TableColumn<PaymentDTO, Double> colAmount;
    @FXML private TableColumn<PaymentDTO, LocalDate> colDate;
    @FXML private TableColumn<PaymentDTO, String> colStatus;
    @FXML private TableColumn<PaymentDTO, String> colMethod;
    @FXML private TableColumn<PaymentDTO, String> colInvoice;

    private final PaymentBO paymentBO = BOFactory.getBO(BOFactory.BOTypes.PAYMENT);
    private final PatientBO patientBO = BOFactory.getBO(BOFactory.BOTypes.PATIENT);
    private final TherapySessionBO sessionBO = BOFactory.getBO(BOFactory.BOTypes.THERAPY_SESSION);

    @FXML
    public void initialize() {
        setupTable();
        loadPatients();
        cmbPaymentMethod.setItems(FXCollections.observableArrayList("Cash", "Card", "Bank Transfer"));
        loadPayments();

        cmbPatient.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) {
                List<TherapySessionDTO> sessions = sessionBO.findByPatient(selected.getId());
                cmbSession.setItems(FXCollections.observableArrayList(sessions));
                cmbSession.setCellFactory(lv -> new ListCell<>() {
                    protected void updateItem(TherapySessionDTO s, boolean empty) {
                        super.updateItem(s, empty);
                        setText(empty || s == null ? null :
                                "Session #" + s.getId() + " - " + s.getProgramName() + " (" + s.getSessionDate() + ")");
                    }
                });
                cmbSession.setButtonCell(cmbSession.getCellFactory().call(null));

                cmbSession.getSelectionModel().selectedItemProperty().addListener((o2, o3, sess) -> {
                    if (sess != null) txtAmount.setText("");
                });
            }
        });

        cmbPatient.setCellFactory(lv -> new ListCell<>() {
            protected void updateItem(PatientDTO p, boolean empty) {
                super.updateItem(p, empty);
                setText(empty || p == null ? null : p.getFullName());
            }
        });
        cmbPatient.setButtonCell(cmbPatient.getCellFactory().call(null));
    }

    @FXML
    public void onProcessPaymentClicked(ActionEvent event) {
        PatientDTO patient = cmbPatient.getValue();
        TherapySessionDTO session = cmbSession.getValue();
        String method = cmbPaymentMethod.getValue();
        String amountText = txtAmount.getText().trim();

        if (patient == null || session == null || method == null || amountText.isEmpty()) {
            showMessage("Please fill in all payment details.", false);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
            if (amount <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showMessage("Please enter a valid amount.", false);
            return;
        }

        PaymentDTO dto = new PaymentDTO();
        dto.setPatientId(patient.getId());
        dto.setSessionId(session.getId());
        dto.setAmount(amount);
        dto.setPaymentMethod(method);

        try {
            boolean ok = paymentBO.processPayment(dto);
            if (ok) {
                PaymentDTO saved = paymentBO.findByInvoice(
                        paymentBO.findByPatient(patient.getId()).stream()
                                .findFirst().map(PaymentDTO::getInvoiceNumber).orElse(""));
                showMessage("Payment processed. Invoice ready.", true);
                loadPayments();
                clearForm();
            } else {
                showMessage("Payment failed. Please try again.", false);
            }
        } catch (PaymentException e) {
            showMessage(e.getMessage(), false);
        }
    }

    @FXML
    public void onMarkPaidClicked(ActionEvent event) {
        PaymentDTO selected = tblPayments.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showMessage("Please select a payment to mark as paid.", false);
            return;
        }
        try {
            boolean ok = paymentBO.markAsPaid(selected.getId());
            showMessage(ok ? "Payment marked as paid." : "Update failed.", ok);
            if (ok) loadPayments();
        } catch (PaymentException e) {
            showMessage(e.getMessage(), false);
        }
    }

    @FXML
    public void onShowPendingClicked(ActionEvent event) {
        tblPayments.setItems(FXCollections.observableArrayList(paymentBO.findPending()));
    }

    @FXML
    public void onShowAllClicked(ActionEvent event) {
        loadPayments();
    }


    private void setupTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPatient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
        colMethod.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        colInvoice.setCellValueFactory(new PropertyValueFactory<>("invoiceNumber"));
    }

    private void loadPatients() {
        cmbPatient.setItems(FXCollections.observableArrayList(patientBO.findAll()));
    }

    private void loadPayments() {
        tblPayments.setItems(FXCollections.observableArrayList(paymentBO.findAll()));
    }

    private void clearForm() {
        cmbPatient.setValue(null);
        cmbSession.getItems().clear();
        cmbPaymentMethod.setValue(null);
        txtAmount.clear();
        lblMessage.setText("");
    }

    private void showMessage(String msg, boolean ok) {
        lblMessage.setText(msg);
        lblMessage.setStyle(ok ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
    }
}