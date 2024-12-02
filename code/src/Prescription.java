package src;

public class Prescription {
    private int prescriptionId;
    private String patientName;
    private String medication;
    private String dosage;
    private String issueDate;
    private String administeredBy;
    private String timeframeStart;
    private String timeframeEnd;

    public Prescription(int prescriptionId, String patientName, String medication, String dosage, String issueDate, String administeredBy, String timeframeStart, String timeframeEnd) {
        this.prescriptionId = prescriptionId;
        this.patientName = patientName;
        this.medication = medication;
        this.dosage = dosage;
        this.issueDate = issueDate;
        this.administeredBy = administeredBy;
        this.timeframeStart = timeframeStart;
        this.timeframeEnd = timeframeEnd;
    }

    // Getters and Setters
    public int getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(int prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getMedication() {
        return medication;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getAdministeredBy() {
        return administeredBy;
    }

    public void setAdministeredBy(String administeredBy) {
        this.administeredBy = administeredBy;
    }

    public String getTimeframeStart() {
        return timeframeStart;
    }

    public void setTimeframeStart(String timeframeStart) {
        this.timeframeStart = timeframeStart;
    }

    public String getTimeframeEnd() {
        return timeframeEnd;
    }

    public void setTimeframeEnd(String timeframeEnd) {
        this.timeframeEnd = timeframeEnd;
    }
}