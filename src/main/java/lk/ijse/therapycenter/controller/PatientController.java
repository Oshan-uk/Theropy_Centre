package lk.ijse.therapycenter.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.therapycenter.bo.BOFactory;
import lk.ijse.therapycenter.bo.custom.PatientBO;
import lk.ijse.therapycenter.dto.PatientDTO;
import lk.ijse.therapycenter.exception.RegistrationException;

import java.time.LocalDate;
import java.util.List;

public class PatientController {

    @FXML private TextField txtName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhone;
    @FXML private DatePicker dpDOB;
    @FXML private TextField txtAddress;
    @FXML private TextArea txtMedicalHistory;
    @FXML private TextField txtSearch;
    @FXML private Label lblMessage;

    @FXML private TableView<PatientDTO> tblPatients;
    @FXML private TableColumn<PatientDTO, Integer> colId;
    @FXML private TableColumn<PatientDTO, String> colName;
    @FXML private TableColumn<PatientDTO, String> colEmail;
    @FXML private TableColumn<PatientDTO, String> colPhone;
    @FXML private TableColumn<PatientDTO, LocalDate> colDOB;
    @FXML private TableColumn<PatientDTO, LocalDate> colRegDate;

    private final PatientBO patientBO = BOFactory.getBO(BOFactory.BOTypes.PATIENT);

    private int selectedPatientId = -1;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colDOB.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        colRegDate.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));

        loadAllPatients();

        tblPatients.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, selected) -> {
                    if (selected != null) fillForm(selected);
                }
        );
    }

    @FXML
    public void onSaveClicked(ActionEvent event) {
        PatientDTO dto = buildDTOFromForm();
        if (dto == null) return;

        try {
            boolean success;
            if (selectedPatientId == -1) {

                success = patientBO.addPatient(dto);
                showMessage(success ? "Patient registered successfully." : "Failed to save patient.", success);
            } else {
                dto.setId(selectedPatientId);
                success = patientBO.updatePatient(dto);
                showMessage(success ? "Patient updated." : "Update failed.", success);
            }
            if (success) {
                clearForm();
                loadAllPatients();
            }
        } catch (RegistrationException e) {
            showMessage(e.getMessage(), false);
        }
    }

    @FXML
    public void onDeleteClicked(ActionEvent event) {
        if (selectedPatientId == -1) {
            showMessage("Please select a patient to delete.", false);
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete this patient? All their sessions will also be removed.", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                boolean removed = patientBO.deletePatient(selectedPatientId);
                showMessage(removed ? "Patient deleted." : "Delete failed.", removed);
                if (removed) { clearForm(); loadAllPatients(); }
            }
        });
    }

    @FXML
    public void onSearchClicked(ActionEvent event) {
        String keyword = txtSearch.getText().trim();
        List<PatientDTO> results = keyword.isEmpty() ?
                patientBO.findAll() : patientBO.searchByName(keyword);
        tblPatients.setItems(FXCollections.observableArrayList(results));
    }

    @FXML
    public void onClearClicked(ActionEvent event) {
        clearForm();
        loadAllPatients();
    }


    private void loadAllPatients() {
        tblPatients.setItems(FXCollections.observableArrayList(patientBO.findAll()));
    }

    private PatientDTO buildDTOFromForm() {
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        LocalDate dob = dpDOB.getValue();
        String address = txtAddress.getText().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || dob == null || address.isEmpty()) {
            showMessage("Please fill in all required fields.", false);
            return null;
        }

        PatientDTO dto = new PatientDTO(0, name, email, phone, dob, address);
        dto.setMedicalHistory(txtMedicalHistory.getText().trim());
        return dto;
    }

    private void fillForm(PatientDTO dto) {
        selectedPatientId = dto.getId();
        txtName.setText(dto.getFullName());
        txtEmail.setText(dto.getEmail());
        txtPhone.setText(dto.getPhone());
        dpDOB.setValue(dto.getDateOfBirth());
        txtAddress.setText(dto.getAddress());
        txtMedicalHistory.setText(dto.getMedicalHistory() != null ? dto.getMedicalHistory() : "");
    }

    private void clearForm() {
        selectedPatientId = -1;
        txtName.clear(); txtEmail.clear(); txtPhone.clear();
        txtAddress.clear(); txtMedicalHistory.clear();
        dpDOB.setValue(null);
        lblMessage.setText("");
        tblPatients.getSelectionModel().clearSelection();
    }

    private void showMessage(String msg, boolean success) {
        lblMessage.setText(msg);
        lblMessage.setStyle(success ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
    }
}