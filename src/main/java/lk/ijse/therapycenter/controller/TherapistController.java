package lk.ijse.therapycenter.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.therapycenter.bo.BOFactory;
import lk.ijse.therapycenter.bo.custom.TherapistBO;
import lk.ijse.therapycenter.dto.TherapistDTO;
import lk.ijse.therapycenter.exception.RegistrationException;

public class TherapistController {

    @FXML private TextField txtName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhone;
    @FXML private TextField txtSpecialization;
    @FXML private CheckBox chkAvailable;
    @FXML private Label lblMessage;

    @FXML private TableView<TherapistDTO> tblTherapists;
    @FXML private TableColumn<TherapistDTO, Integer> colId;
    @FXML private TableColumn<TherapistDTO, String> colName;
    @FXML private TableColumn<TherapistDTO, String> colEmail;
    @FXML private TableColumn<TherapistDTO, String> colPhone;
    @FXML private TableColumn<TherapistDTO, String> colSpec;
    @FXML private TableColumn<TherapistDTO, Boolean> colAvailable;

    private final TherapistBO therapistBO = BOFactory.getBO(BOFactory.BOTypes.THERAPIST);
    private int selectedId = -1;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colSpec.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        colAvailable.setCellValueFactory(new PropertyValueFactory<>("available"));

        loadAll();

        tblTherapists.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, selected) -> { if (selected != null) fillForm(selected); }
        );
    }

    @FXML
    public void onSaveClicked(ActionEvent event) {
        TherapistDTO dto = buildDTO();
        if (dto == null) return;

        try {
            boolean ok;
            if (selectedId == -1) {
                ok = therapistBO.addTherapist(dto);
                showMessage(ok ? "Therapist added." : "Failed to add.", ok);
            } else {
                dto.setId(selectedId);
                ok = therapistBO.updateTherapist(dto);
                showMessage(ok ? "Therapist updated." : "Update failed.", ok);
            }
            if (ok) { clearForm(); loadAll(); }
        } catch (RegistrationException e) {
            showMessage(e.getMessage(), false);
        }
    }

    @FXML
    public void onDeleteClicked(ActionEvent event) {
        if (selectedId == -1) { showMessage("Select a therapist first.", false); return; }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete this therapist?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(r -> {
            if (r == ButtonType.YES) {
                boolean ok = therapistBO.deleteTherapist(selectedId);
                showMessage(ok ? "Deleted." : "Delete failed.", ok);
                if (ok) { clearForm(); loadAll(); }
            }
        });
    }

    @FXML
    public void onClearClicked(ActionEvent event) {
        clearForm();
    }

    private void loadAll() {
        tblTherapists.setItems(FXCollections.observableArrayList(therapistBO.findAll()));
    }

    private TherapistDTO buildDTO() {
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        String spec = txtSpecialization.getText().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || spec.isEmpty()) {
            showMessage("All fields are required.", false);
            return null;
        }
        return new TherapistDTO(0, name, email, phone, spec, chkAvailable.isSelected());
    }

    private void fillForm(TherapistDTO dto) {
        selectedId = dto.getId();
        txtName.setText(dto.getFullName());
        txtEmail.setText(dto.getEmail());
        txtPhone.setText(dto.getPhone());
        txtSpecialization.setText(dto.getSpecialization());
        chkAvailable.setSelected(dto.isAvailable());
    }

    private void clearForm() {
        selectedId = -1;
        txtName.clear(); txtEmail.clear(); txtPhone.clear(); txtSpecialization.clear();
        chkAvailable.setSelected(true);
        lblMessage.setText("");
        tblTherapists.getSelectionModel().clearSelection();
    }

    private void showMessage(String msg, boolean ok) {
        lblMessage.setText(msg);
        lblMessage.setStyle(ok ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
    }
}