package lk.ijse.therapycenter.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.therapycenter.bo.*;
import lk.ijse.therapycenter.bo.custom.PatientBO;
import lk.ijse.therapycenter.bo.custom.TherapistBO;
import lk.ijse.therapycenter.bo.custom.TherapyProgramBO;
import lk.ijse.therapycenter.bo.custom.TherapySessionBO;
import lk.ijse.therapycenter.dto.*;
import lk.ijse.therapycenter.exception.SessionConflictException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TherapySessionController {

    @FXML private ComboBox<PatientDTO> cmbPatient;
    @FXML private ComboBox<TherapistDTO> cmbTherapist;
    @FXML private ComboBox<TherapyProgramDTO> cmbProgram;
    @FXML private DatePicker dpSessionDate;
    @FXML private TextField txtSessionTime;
    @FXML private TextArea txtNotes;
    @FXML private Label lblMessage;

    @FXML private TableView<TherapySessionDTO> tblSessions;
    @FXML private TableColumn<TherapySessionDTO, Integer> colId;
    @FXML private TableColumn<TherapySessionDTO, String> colPatient;
    @FXML private TableColumn<TherapySessionDTO, String> colTherapist;
    @FXML private TableColumn<TherapySessionDTO, String> colProgram;
    @FXML private TableColumn<TherapySessionDTO, LocalDate> colDate;
    @FXML private TableColumn<TherapySessionDTO, String> colStatus;

    private final TherapySessionBO sessionBO = BOFactory.getBO(BOFactory.BOTypes.THERAPY_SESSION);
    private final PatientBO patientBO = BOFactory.getBO(BOFactory.BOTypes.PATIENT);
    private final TherapistBO therapistBO = BOFactory.getBO(BOFactory.BOTypes.THERAPIST);
    private final TherapyProgramBO programBO = BOFactory.getBO(BOFactory.BOTypes.THERAPY_PROGRAM);

    private int selectedSessionId = -1;

    @FXML
    public void initialize() {
        setupTable();
        loadDropdowns();
        loadSessions();

        tblSessions.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, selected) -> { if (selected != null) fillForm(selected); }
        );
    }

    @FXML
    public void onBookClicked(ActionEvent event) {
        TherapySessionDTO dto = buildDTO();
        if (dto == null) return;

        try {
            boolean ok;
            if (selectedSessionId == -1) {
                ok = sessionBO.bookSession(dto);
                showMessage(ok ? "Session booked." : "Booking failed.", ok);
            } else {
                ok = sessionBO.rescheduleSession(selectedSessionId, dto);
                showMessage(ok ? "Session rescheduled." : "Reschedule failed.", ok);
            }
            if (ok) { clearForm(); loadSessions(); }
        } catch (SessionConflictException e) {
            showMessage(e.getMessage(), false);
        }
    }

    @FXML
    public void onCancelSessionClicked(ActionEvent event) {
        if (selectedSessionId == -1) {
            showMessage("Select a session to cancel.", false);
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Cancel this session?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(r -> {
            if (r == ButtonType.YES) {
                boolean ok = sessionBO.cancelSession(selectedSessionId);
                showMessage(ok ? "Session cancelled." : "Failed to cancel.", ok);
                if (ok) { clearForm(); loadSessions(); }
            }
        });
    }


    private void setupTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPatient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colTherapist.setCellValueFactory(new PropertyValueFactory<>("therapistName"));
        colProgram.setCellValueFactory(new PropertyValueFactory<>("programName"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("sessionDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadDropdowns() {
        List<PatientDTO> patients = patientBO.findAll();
        List<TherapistDTO> therapists = therapistBO.findAvailable();
        List<TherapyProgramDTO> programs = programBO.findAll();

        cmbPatient.setItems(FXCollections.observableArrayList(patients));
        cmbTherapist.setItems(FXCollections.observableArrayList(therapists));
        cmbProgram.setItems(FXCollections.observableArrayList(programs));

        cmbPatient.setCellFactory(lv -> new ListCell<>() {
            protected void updateItem(PatientDTO p, boolean empty) {
                super.updateItem(p, empty);
                setText(empty || p == null ? null : p.getFullName());
            }
        });
        cmbPatient.setButtonCell(cmbPatient.getCellFactory().call(null));

        cmbTherapist.setCellFactory(lv -> new ListCell<>() {
            protected void updateItem(TherapistDTO t, boolean empty) {
                super.updateItem(t, empty);
                setText(empty || t == null ? null : t.getFullName());
            }
        });
        cmbTherapist.setButtonCell(cmbTherapist.getCellFactory().call(null));

        cmbProgram.setCellFactory(lv -> new ListCell<>() {
            protected void updateItem(TherapyProgramDTO tp, boolean empty) {
                super.updateItem(tp, empty);
                setText(empty || tp == null ? null : tp.getProgramName());
            }
        });
        cmbProgram.setButtonCell(cmbProgram.getCellFactory().call(null));
    }

    private void loadSessions() {
        tblSessions.setItems(FXCollections.observableArrayList(sessionBO.findAll()));
    }

    private TherapySessionDTO buildDTO() {
        PatientDTO patient = cmbPatient.getValue();
        TherapistDTO therapist = cmbTherapist.getValue();
        TherapyProgramDTO program = cmbProgram.getValue();
        LocalDate date = dpSessionDate.getValue();
        String timeText = txtSessionTime.getText().trim();

        if (patient == null || therapist == null || program == null || date == null || timeText.isEmpty()) {
            showMessage("Please fill in all session details.", false);
            return null;
        }

        LocalTime time;
        try {
            time = LocalTime.parse(timeText);
        } catch (Exception e) {
            showMessage("Time must be in HH:mm format (e.g. 09:30).", false);
            return null;
        }

        TherapySessionDTO dto = new TherapySessionDTO();
        dto.setPatientId(patient.getId());
        dto.setTherapistId(therapist.getId());
        dto.setProgramId(program.getProgramId());
        dto.setSessionDate(date);
        dto.setSessionTime(time);
        dto.setNotes(txtNotes.getText().trim());
        return dto;
    }

    private void fillForm(TherapySessionDTO dto) {
        selectedSessionId = dto.getId();
        dpSessionDate.setValue(dto.getSessionDate());
        txtSessionTime.setText(dto.getSessionTime().toString());
        txtNotes.setText(dto.getNotes() != null ? dto.getNotes() : "");
    }

    private void clearForm() {
        selectedSessionId = -1;
        cmbPatient.setValue(null);
        cmbTherapist.setValue(null);
        cmbProgram.setValue(null);
        dpSessionDate.setValue(null);
        txtSessionTime.clear();
        txtNotes.clear();
        lblMessage.setText("");
        tblSessions.getSelectionModel().clearSelection();
    }

    private void showMessage(String msg, boolean ok) {
        lblMessage.setText(msg);
        lblMessage.setStyle(ok ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
    }
}