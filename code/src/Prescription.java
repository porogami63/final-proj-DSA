package src;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Prescription {
    private int prescriptionId;
    private String patientName;
    private String medication;
    private String dosage;
    private String issueDate;
    private String administeredBy;
    private String prescribedTime;
    private String timeAdministered;

    public Prescription(int prescriptionId, String patientName, String medication, String dosage, String issueDate, String administeredBy, String prescribedTime, String timeAdministered) {
        this.prescriptionId = prescriptionId;
        this.patientName = patientName;
        this.medication = medication;
        this.dosage = dosage;
        this.issueDate = issueDate;
        this.administeredBy = administeredBy;
        this.prescribedTime = prescribedTime;
        this.timeAdministered = timeAdministered;
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

    public String getPrescribedTime() {
        return prescribedTime;
    }

    public void setPrescribedTime(String prescribedTime) {
        this.prescribedTime = prescribedTime;
    }

    public String getTimeAdministered() {
        return timeAdministered;
    }

    public void setTimeAdministered(String timeAdministered) {
        this.timeAdministered = timeAdministered;
    }

    static Queue<String[][]> prescriptionQueue = new LinkedList<>();

    public static void addToQueue(String[][] prescriptionData) {
        prescriptionQueue.add(prescriptionData);
    }

    public static String[][] removeFromQueue() {
        return prescriptionQueue.poll();
    }

    public static boolean isQueueEmpty() {
        return prescriptionQueue.isEmpty();
    }

    public static void printQueue() {
        for (String[][] data : prescriptionQueue) {
            System.out.println(Arrays.deepToString(data));
        }
    }

    // Remove elements based on timeframe
    public static void removeElementsBasedOnTimeframe() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        Queue<String[][]> queue = prescriptionQueue;
        queue.removeIf(data -> {
            try {
                Date date = sdf.parse(data[0][4]);
                // Add your logic to check the timeframe
                return false;
            } catch (ParseException e) {
                System.err.println("Unparseable date: " + data[0][4]);
                return true; // Remove if date is unparseable
            }
        });
    }
}