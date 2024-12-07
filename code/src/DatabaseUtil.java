package src;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class DatabaseUtil {

    private static final String URL = "jdbc:mysql://localhost:3306/prescription_inventory";
    private static final String USER = "root"; 
    private static final String PASSWORD = "115320"; 

    private static List<Prescription> prescriptions = new ArrayList<>();

    private static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new SQLException("Failed to connect to the database.", e);
        }
    }

    // Method to get all prescriptions from the database
    public static List<Prescription> getAllPrescriptions() {
        prescriptions.clear();
        String query = "SELECT * FROM prescriptions";
    
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
    
            while (rs.next()) {
                Prescription prescription = new Prescription(
                        rs.getInt("prescription_id"),
                        rs.getString("patient_name"),
                        rs.getString("medication"),
                        rs.getString("dosage"),
                        rs.getString("issue_date"),
                        rs.getString("administered_by"),
                        rs.getString("prescribed_time"),
                        rs.getString("time_administered"),
                        rs.getString("classification"), // New field
                        rs.getString("symptoms") // New field
                );
                prescriptions.add(prescription);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prescriptions;
    }
    
    public static void addPrescription(Prescription newPrescription) {
        String query = "INSERT INTO prescriptions (prescription_id, patient_name, medication, dosage, issue_date, administered_by, prescribed_time, time_administered, classification, symptoms) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
    
            pstmt.setInt(1, newPrescription.getPrescriptionId());
            pstmt.setString(2, newPrescription.getPatientName());
            pstmt.setString(3, newPrescription.getMedication());
            pstmt.setString(4, newPrescription.getDosage());
            pstmt.setString(5, newPrescription.getIssueDate());
            pstmt.setString(6, newPrescription.getAdministeredBy());
            pstmt.setString(7, newPrescription.getPrescribedTime());
            pstmt.setString(8, newPrescription.getTimeAdministered());
            pstmt.setString(9, newPrescription.getClassification()); // New field
            pstmt.setString(10, newPrescription.getSymptoms()); // New field
    
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void updatePrescription(Prescription updatedPrescription) {
        String query = "UPDATE prescriptions SET patient_name = ?, medication = ?, dosage = ?, issue_date = ?, administered_by = ?, prescribed_time = ?, time_administered = ?, classification = ?, symptoms = ? WHERE prescription_id = ?";
    
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
    
            pstmt.setString(1, updatedPrescription.getPatientName());
            pstmt.setString(2, updatedPrescription.getMedication());
            pstmt.setString(3, updatedPrescription.getDosage());
            pstmt.setString(4, updatedPrescription.getIssueDate());
            pstmt.setString(5, updatedPrescription.getAdministeredBy());
            pstmt.setString(6, updatedPrescription.getPrescribedTime());
            pstmt.setString(7, updatedPrescription.getTimeAdministered());
            pstmt.setString(8, updatedPrescription.getClassification()); // New field
            pstmt.setString(9, updatedPrescription.getSymptoms()); // New field
            pstmt.setInt(10, updatedPrescription.getPrescriptionId());
    
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete a prescription from the database
    public static void deletePrescription(int prescriptionId) {
        String query = "DELETE FROM prescriptions WHERE prescription_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, prescriptionId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to authenticate a user
    public static boolean authenticateUser(String username, String password) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ? AND password = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to create a new user
    public static boolean createUser(String username, String password) {
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            int rowsAffected = pstmt.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void wipeAllPrescriptions() {
        String query = "DELETE FROM prescriptions";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        prescriptions.clear();
    }

    public static void sortPrescriptions(List<Prescription> prescriptions, List<String> sortBy) {
        Comparator<Prescription> comparator = (p1, p2) -> 0;
    
        for (String criteria : sortBy) {
            Comparator<Prescription> tempComparator;
    
            switch (criteria.toLowerCase()) {
                case "dosage":
                    tempComparator = Comparator.comparing(Prescription::getDosage).reversed();
                    break;
                case "patientname":
                    tempComparator = Comparator.comparing(Prescription::getPatientName);
                    break;
                case "medication":
                    tempComparator = Comparator.comparing(Prescription::getMedication);
                    break;
                case "date":
                    tempComparator = Comparator.comparing(Prescription::getIssueDate);
                    break;
                case "time":
                    tempComparator = Comparator.comparing(Prescription::getPrescribedTime);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid sort criteria: " + criteria);
            }
    
            comparator = comparator.thenComparing(tempComparator);
        }
    
        prescriptions.sort(comparator);
    }
    public static void saveQueueToDatabase() {
        String query = "INSERT INTO queue (prescription_data) VALUES (?)";
    
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
    
            for (String[][] prescriptionData : Prescription.prescriptionQueue) {
                pstmt.setString(1, Arrays.deepToString(prescriptionData));
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}