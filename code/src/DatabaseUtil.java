package src;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtil {

    private static final String URL = "jdbc:mysql://localhost:3306/prescription_inventory";
    private static final String USER = "root"; // Change to your MySQL username
    private static final String PASSWORD = "115320"; // Change to your MySQL password

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
        String query = "SELECT * FROM prescriptions"; // Adjust the table name as needed

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
                        rs.getString("timeframe_start"),
                        rs.getString("timeframe_end")
                );
                prescriptions.add(prescription);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prescriptions;
    }

    // Method to add a prescription to the database
    public static void addPrescription(Prescription newPrescription) {
        String query = "INSERT INTO prescriptions (prescription_id, patient_name, medication, dosage, issue_date, administered_by, timeframe_start, timeframe_end) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, newPrescription.getPrescriptionId());
            pstmt.setString(2, newPrescription.getPatientName());
            pstmt.setString(3, newPrescription.getMedication());
            pstmt.setString(4, newPrescription.getDosage());
            pstmt.setString(5, newPrescription.getIssueDate());
            pstmt.setString(6, newPrescription.getAdministeredBy());
            pstmt.setString(7, newPrescription.getTimeframeStart());
            pstmt.setString(8, newPrescription.getTimeframeEnd());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update a prescription in the database
    public static void updatePrescription(Prescription updatedPrescription) {
        String query = "UPDATE prescriptions SET patient_name = ?, medication = ?, dosage = ?, issue_date = ?, administered_by = ?, timeframe_start = ?, timeframe_end = ? WHERE prescription_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, updatedPrescription.getPatientName());
            pstmt.setString(2, updatedPrescription.getMedication());
            pstmt.setString(3, updatedPrescription.getDosage());
            pstmt.setString(4, updatedPrescription.getIssueDate());
            pstmt.setString(5, updatedPrescription.getAdministeredBy());
            pstmt.setString(6, updatedPrescription.getTimeframeStart());
            pstmt.setString(7, updatedPrescription.getTimeframeEnd());
            pstmt.setInt(8, updatedPrescription.getPrescriptionId());

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
}