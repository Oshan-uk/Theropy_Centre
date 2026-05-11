package lk.ijse.therapycenter.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.therapycenter.bo.BOFactory;
import lk.ijse.therapycenter.bo.custom.*;
import lk.ijse.therapycenter.dto.*;

import java.util.List;
import java.util.Map;


public class AdminReportController {

    @FXML private TableView<TherapistDTO> tblTherapists;
    @FXML private TableColumn<TherapistDTO, String> colTherapistName;
    @FXML private TableColumn<TherapistDTO, String> colSpecialization;
    @FXML private TableColumn<TherapistDTO, Boolean> colAvailable;

    @FXML private TableView<PatientDTO> tblAllProgramPatients;
    @FXML private TableColumn<PatientDTO, String> colPatientName;
    @FXML private TableColumn<PatientDTO, String> colEmail;
    @FXML private TableColumn<PatientDTO, String> colPhone;

    @FXML private TableView<PatientProgramRow> tblPatientPrograms;
    @FXML private TableColumn<PatientProgramRow, String> colPName;
    @FXML private TableColumn<PatientProgramRow, String> colProgramList;

    @FXML private Label lblTotalTherapists;
    @FXML private Label lblTotalPrograms;
    @FXML private Label lblTotalSessions;

    private final TherapistBO therapistBO = BOFactory.getBO(BOFactory.BOTypes.THERAPIST);
    private final PatientBO patientBO = BOFactory.getBO(BOFactory.BOTypes.PATIENT);
    private final TherapyProgramBO programBO = BOFactory.getBO(BOFactory.BOTypes.THERAPY_PROGRAM);
    private final TherapySessionBO sessionBO = BOFactory.getBO(BOFactory.BOTypes.THERAPY_SESSION);

    @FXML
    public void initialize() {

        colTherapistName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colSpecialization.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        colAvailable.setCellValueFactory(new PropertyValueFactory<>("available"));


        colPatientName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));


        colPName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colProgramList.setCellValueFactory(new PropertyValueFactory<>("programs"));

        loadReport();
    }

    @FXML
    public void onRefreshClicked(ActionEvent event) {
        loadReport();
    }

    private void loadReport() {

        List<TherapistDTO> therapists = therapistBO.findAll();
        tblTherapists.setItems(FXCollections.observableArrayList(therapists));


        List<PatientDTO> fullEnrolled = patientBO.getPatientsEnrolledInAllPrograms();
        tblAllProgramPatients.setItems(FXCollections.observableArrayList(fullEnrolled));


        Map<PatientDTO, List<TherapyProgramDTO>> patientPrograms = patientBO.getPatientsWithPrograms();
        java.util.List<PatientProgramRow> rows = new java.util.ArrayList<>();
        for (Map.Entry<PatientDTO, List<TherapyProgramDTO>> entry : patientPrograms.entrySet()) {
            StringBuilder programs = new StringBuilder();
            for (TherapyProgramDTO p : entry.getValue()) {
                if (programs.length() > 0) programs.append(", ");
                programs.append(p.getProgramName());
            }
            rows.add(new PatientProgramRow(entry.getKey().getFullName(), programs.toString()));
        }
        tblPatientPrograms.setItems(FXCollections.observableArrayList(rows));


        lblTotalTherapists.setText("Total Therapists: " + therapists.size());
        lblTotalPrograms.setText("Total Programs: " + programBO.findAll().size());
        lblTotalSessions.setText("Total Sessions: " + sessionBO.findAll().size());
    }



    public static class PatientProgramRow {
        private final String patientName;
        private final String programs;

        public PatientProgramRow(String patientName, String programs) {
            this.patientName = patientName;
            this.programs = programs;
        }

        public String getPatientName() { return patientName; }
        public String getPrograms() { return programs; }
    }
}