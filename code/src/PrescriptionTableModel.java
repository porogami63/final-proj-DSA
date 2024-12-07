package src;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class PrescriptionTableModel extends AbstractTableModel {
    private final String[] columnNames = {
        "ID", "Patient Name", "Medication", "Dosage", "Issue Date", "Administered By", "Prescribed Time", "Time Administered", "Classification", "Symptoms"
    };
    private List<Prescription> prescriptions;

    public PrescriptionTableModel() {
        this.prescriptions = DatabaseUtil.getAllPrescriptions();
    }

    public void setPrescriptions(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return prescriptions.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Prescription prescription = prescriptions.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return prescription.getPrescriptionId();
            case 1:
                return prescription.getPatientName();
            case 2:
                return prescription.getMedication();
            case 3:
                return prescription.getDosage();
            case 4:
                return prescription.getIssueDate();
            case 5:
                return prescription.getAdministeredBy();
            case 6:
                return prescription.getPrescribedTime();
            case 7:
                return prescription.getTimeAdministered();
            case 8:
                return prescription.getClassification(); // New field
            case 9:
                return prescription.getSymptoms(); // New field
            default:
                return null;
        }
    }

    public Prescription getPrescriptionAt(int rowIndex) {
        return prescriptions.get(rowIndex);
    }
}