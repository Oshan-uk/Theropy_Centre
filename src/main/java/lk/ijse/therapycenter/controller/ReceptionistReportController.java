package lk.ijse.therapycenter.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.therapycenter.bo.BOFactory;
import lk.ijse.therapycenter.bo.custom.PaymentBO;
import lk.ijse.therapycenter.bo.custom.PatientBO;
import lk.ijse.therapycenter.dto.PaymentDTO;
import lk.ijse.therapycenter.dto.PatientDTO;

import java.time.LocalDate;
import java.util.List;


public class ReceptionistReportController {

    @FXML private TableView<PaymentDTO> tblPayments;
    @FXML private TableColumn<PaymentDTO, String> colInvoice;
    @FXML private TableColumn<PaymentDTO, String> colPatient;
    @FXML private TableColumn<PaymentDTO, Double> colAmount;
    @FXML private TableColumn<PaymentDTO, LocalDate> colDate;
    @FXML private TableColumn<PaymentDTO, String> colStatus;
    @FXML private TableColumn<PaymentDTO, String> colMethod;


    @FXML private TableView<PatientDTO> tblPatients;
    @FXML private TableColumn<PatientDTO, String> colPatientName;
    @FXML private TableColumn<PatientDTO, LocalDate> colRegDate;

    @FXML private Label lblTotalRevenue;
    @FXML private Label lblPendingCount;
    @FXML private Label lblPaidCount;
    @FXML private ComboBox<PatientDTO> cmbFilterPatient;

    private final PaymentBO paymentBO = BOFactory.getBO(BOFactory.BOTypes.PAYMENT);
    private final PatientBO patientBO = BOFactory.getBO(BOFactory.BOTypes.PATIENT);

    @FXML
    public void initialize() {

        colInvoice.setCellValueFactory(new PropertyValueFactory<>("invoiceNumber"));
        colPatient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
        colMethod.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));


        colPatientName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colRegDate.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));


        List<PatientDTO> patients = patientBO.findAll();
        cmbFilterPatient.setItems(FXCollections.observableArrayList(patients));
        cmbFilterPatient.setCellFactory(lv -> new ListCell<>() {
            protected void updateItem(PatientDTO p, boolean empty) {
                super.updateItem(p, empty);
                setText(empty || p == null ? null : p.getFullName());
            }
        });
        cmbFilterPatient.setButtonCell(cmbFilterPatient.getCellFactory().call(null));

        loadAllData();
    }

    @FXML
    public void onShowAllClicked(ActionEvent event) {
        loadAllData();
    }

    @FXML
    public void onShowPendingClicked(ActionEvent event) {
        List<PaymentDTO> pending = paymentBO.findPending();
        tblPayments.setItems(FXCollections.observableArrayList(pending));
        updateSummary(paymentBO.findAll());
    }

    @FXML
    public void onFilterByPatientClicked(ActionEvent event) {
        PatientDTO selected = cmbFilterPatient.getValue();
        if (selected == null) {
            loadAllData();
            return;
        }
        List<PaymentDTO> filtered = paymentBO.findByPatient(selected.getId());
        tblPayments.setItems(FXCollections.observableArrayList(filtered));
        updateSummary(filtered);
    }

    @FXML
    public void onRefreshClicked(ActionEvent event) {
        loadAllData();
    }

    private void loadAllData() {
        List<PaymentDTO> all = paymentBO.findAll();
        tblPayments.setItems(FXCollections.observableArrayList(all));
        updateSummary(all);


        tblPatients.setItems(FXCollections.observableArrayList(patientBO.findAll()));
    }

    private void updateSummary(List<PaymentDTO> payments) {
        double total = payments.stream()
                .filter(p -> "PAID".equals(p.getPaymentStatus()))
                .mapToDouble(PaymentDTO::getAmount)
                .sum();

        long paid = payments.stream().filter(p -> "PAID".equals(p.getPaymentStatus())).count();
        long pending = payments.stream().filter(p -> "PENDING".equals(p.getPaymentStatus())).count();

        lblTotalRevenue.setText(String.format("Total Revenue: LKR %.2f", total));
        lblPaidCount.setText("Paid: " + paid);
        lblPendingCount.setText("Pending: " + pending);
    }
}