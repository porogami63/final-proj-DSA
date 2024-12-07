package src;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;

public class Prescription {
    private int prescriptionId;
    private String patientName;
    private String medication;
    private String dosage;
    private String issueDate;
    private String administeredBy;
    private String prescribedTime;
    private String timeAdministered;
    private String classification; // New field
    private String symptoms; // New field

    public Prescription(int prescriptionId, String patientName, String medication, String dosage, String issueDate, String administeredBy, String prescribedTime, String timeAdministered, String symptoms, String classification) {
        this.prescriptionId = prescriptionId;
        this.patientName = patientName;
        this.medication = medication;
        this.dosage = dosage;
        this.issueDate = issueDate;
        this.administeredBy = administeredBy;
        this.prescribedTime = prescribedTime;
        this.timeAdministered = timeAdministered;
        this.classification = classification;
        this.symptoms = symptoms;
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

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    // Priority queue with custom comparator for prioritizing prescriptions
    static PriorityQueue<String[]> prescriptionQueue = new PriorityQueue<>(new Comparator<String[]>() {
        @Override
        public int compare(String[] p1, String[] p2) {
            String classification1 = p1[8];
            String classification2 = p2[8];
            return Integer.compare(getPriority(classification1), getPriority(classification2));
        }

        private int getPriority(String classification) {
            switch (classification.toUpperCase()) {
                case "CRITICAL":
                    return 1;
                case "MODERATE":
                    return 2;
                case "STABLE":
                    return 3;
                default:
                    return Integer.MAX_VALUE;
            }
        }
    });

    public static void addToQueue(String[][] prescriptionData) {
        for (String[] prescription : prescriptionData) {
            prescriptionQueue.add(prescription);
        }
    }

    public static String[] removeFromQueue() {
        return prescriptionQueue.poll();
    }

    public static boolean isQueueEmpty() {
        return prescriptionQueue.isEmpty();
    }

    public static PriorityQueue<String[]> getQueue() {
        return prescriptionQueue;
    }

    public static void printQueue() {
        for (String[] data : prescriptionQueue) {
            System.out.println(Arrays.toString(data));
        }
    }

    // Remove elements based on timeframe
    public static void removeElementsBasedOnTimeframe() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        Queue<String[]> queue = prescriptionQueue;
        queue.removeIf(data -> {
            try {
                Date date = sdf.parse(data[4]);
                // Add your logic to check the timeframe
                return false;
            } catch (ParseException e) {
                System.err.println("Unparseable date: " + data[4]);
                return true; // Remove if date is unparseable
            }
        });
    }

    public static void main(String[] args) {
        String[][] testPrescriptions = {
            {"1", "John Doe", "Med1", "10", "2023-10-01", "Dr. Smith", "08:00 AM", "09:00 AM", "STABLE"},
            {"2", "Jane Doe", "Med2", "20", "2023-10-02", "Dr. Smith", "08:00 AM", "09:00 AM", "CRITICAL"},
            {"3", "Jim Doe", "Med3", "30", "2023-10-03", "Dr. Smith", "08:00 AM", "09:00 AM", "MODERATE"}
        };

        Prescription.addToQueue(testPrescriptions);
        Prescription.printQueue();
    }
}
