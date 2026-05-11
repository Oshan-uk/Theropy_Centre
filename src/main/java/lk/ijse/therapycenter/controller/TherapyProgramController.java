package lk.ijse.therapycenter.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.therapycenter.bo.BOFactory;
import lk.ijse.therapycenter.bo.custom.TherapyProgramBO;
import lk.ijse.therapycenter.dto.TherapyProgramDTO;
import lk.ijse.therapycenter.exception.RegistrationException;

public class TherapyProgramController {

    @FXML private TextField txtProgramId;
    @FXML private TextField txtProgramName;
    @FXML private TextField txtDuration;
    @FXML private TextField txtFee;
    @FXML private TextArea txtDescription;
    @FXML private Label lblMessage;

    @FXML private TableView<TherapyProgramDTO> tblPrograms;
    @FXML private TableColumn<TherapyProgramDTO, String> colId;
    @FXML private TableColumn<TherapyProgramDTO, String> colName;
    @FXML private TableColumn<TherapyProgramDTO, String> colDuration;
    @FXML private TableColumn<TherapyProgramDTO, Double> colFee;

    private final TherapyProgramBO programBO = BOFactory.getBO(BOFactory.BOTypes.THERAPY_PROGRAM);
    private boolean editMode = false;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("programId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("programName"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colFee.setCellValueFactory(new PropertyValueFactory<>("fee"));

        loadAll();

        tblPrograms.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, selected) -> {
                    if (selected != null) {
                        fillForm(selected);

                        txtProgramId.setDisable(true);
                        editMode = true;
                    }
                }
        );
    }

    @FXML
    public void onSaveClicked(ActionEvent event) {
        try {
            TherapyProgramDTO dto = buildDTO();
            if (dto == null) return;

            boolean ok;
            if (!editMode) {
                ok = programBO.addProgram(dto);
                showMessage(ok ? "Program added." : "Failed.", ok);
            } else {
                ok = programBO.updateProgram(dto);
                showMessage(ok ? "Program updated." : "Update failed.", ok);
            }
            if (ok) { clearForm(); loadAll(); }
        } catch (RegistrationException e) {
            showMessage(e.getMessage(), false);
        }
    }

    @FXML
    public void onDeleteClicked(ActionEvent event) {
        TherapyProgramDTO selected = tblPrograms.getSelectionModel().getSelectedItem();
        if (selected == null) { showMessage("Select a program to delete.", false); return; }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete program: " + selected.getProgramName() + "?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(r -> {
            if (r == ButtonType.YES) {
                boolean ok = programBO.deleteProgram(selected.getProgramId());
                showMessage(ok ? "Program deleted." : "Delete failed.", ok);
                if (ok) { clearForm(); loadAll(); }
            }
        });
    }

    @FXML
    public void onClearClicked(ActionEvent event) {
        clearForm();
    }

    private void loadAll() {
        tblPrograms.setItems(FXCollections.observableArrayList(programBO.findAll()));
    }

    private TherapyProgramDTO buildDTO() {
        String id = txtProgramId.getText().trim().toUpperCase();
        String name = txtProgramName.getText().trim();
        String duration = txtDuration.getText().trim();
        String feeText = txtFee.getText().trim();

        if (id.isEmpty() || name.isEmpty() || duration.isEmpty() || feeText.isEmpty()) {
            showMessage("Program ID, name, duration, and fee are required.", false);
            return null;
        }

        double fee;
        try {
            fee = Double.parseDouble(feeText);
            if (fee <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showMessage("Fee must be a positive number.", false);
            return null;
        }

        return new TherapyProgramDTO(id, name, duration, fee, txtDescription.getText().trim());
    }

    private void fillForm(TherapyProgramDTO dto) {
        txtProgramId.setText(dto.getProgramId());
        txtProgramName.setText(dto.getProgramName());
        txtDuration.setText(dto.getDuration());
        txtFee.setText(String.valueOf(dto.getFee()));
        txtDescription.setText(dto.getDescription() != null ? dto.getDescription() : "");
    }

    private void clearForm() {
        editMode = false;
        txtProgramId.setDisable(false);
        txtProgramId.clear(); txtProgramName.clear();
        txtDuration.clear(); txtFee.clear(); txtDescription.clear();
        lblMessage.setText("");
        tblPrograms.getSelectionModel().clearSelection();
    }

    private void showMessage(String msg, boolean ok) {
        lblMessage.setText(msg);
        lblMessage.setStyle(ok ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
    }
}