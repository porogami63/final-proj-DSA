package src;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

public class PrescriptionGUI extends JFrame {

    private JTable prescriptionTable;
    private PrescriptionTableModel tableModel;
    private JTextField idField, patientNameField, medicationField, dosageField, administeredByField;
    private JDatePickerImpl issueDatePicker;
    private JSpinner timeframeStartSpinner, timeframeEndSpinner;
    private JTextField searchField;

    public PrescriptionGUI() {
        setTitle("MyManager : Prescription Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize table model and pass it to the JTable
        tableModel = new PrescriptionTableModel();
        prescriptionTable = new JTable(tableModel);

        // Customize table header
        JTableHeader header = prescriptionTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(Color.LIGHT_GRAY);

        // Customize table rows
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        prescriptionTable.setDefaultRenderer(String.class, centerRenderer);

        // Add the table to a scroll pane for better display
        JScrollPane scrollPane = new JScrollPane(prescriptionTable);
        add(scrollPane, BorderLayout.CENTER);

        // Create a panel for form inputs and buttons using GroupLayout
        JPanel formPanel = new JPanel();
        GroupLayout layout = new GroupLayout(formPanel);
        formPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // Form fields for Prescription details
        JLabel idLabel = new JLabel("ID:");
        idField = new JTextField();
        JLabel patientNameLabel = new JLabel("Patient Name:");
        patientNameField = new JTextField();
        JLabel medicationLabel = new JLabel("Medication:");
        medicationField = new JTextField();
        JLabel dosageLabel = new JLabel("Dosage (ML):");
        dosageField = new JTextField();
        JLabel issueDateLabel = new JLabel("Issue Date:");

        // Initialize and configure the issue date picker
        UtilDateModel dateModel = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, p);
        issueDatePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        JLabel administeredByLabel = new JLabel("Administered By:");
        administeredByField = new JTextField();
        JLabel timeframeStartLabel = new JLabel("Timeframe Start:");

        // Initialize and configure the start time spinner
        SpinnerDateModel startModel = new SpinnerDateModel();
        timeframeStartSpinner = new JSpinner(startModel);
        JSpinner.DateEditor startEditor = new JSpinner.DateEditor(timeframeStartSpinner, "hh:mm a");
        timeframeStartSpinner.setEditor(startEditor);

        JLabel timeframeEndLabel = new JLabel("Timeframe End:");

        // Initialize and configure the end time spinner
        SpinnerDateModel endModel = new SpinnerDateModel();
        timeframeEndSpinner = new JSpinner(endModel);
        JSpinner.DateEditor endEditor = new JSpinner.DateEditor(timeframeEndSpinner, "hh:mm a");
        timeframeEndSpinner.setEditor(endEditor);

        // Search field and button
        JLabel searchLabel = new JLabel("Search:");
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchPrescription();
            }
        });

        // Refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePrescriptionTable();
            }
        });

        // Add, Update, Delete, Wipe, Export buttons
        JButton addButton = new JButton("Add Prescription");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPrescription();
            }
        });

        JButton updateButton = new JButton("Update Prescription");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePrescription();
            }
        });

        JButton deleteButton = new JButton("Delete Prescription");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletePrescription();
            }
        });

        JButton wipeButton = new JButton("Wipe Table");
        wipeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wipePrescriptionTable();
            }
        });

        JButton exportButton = new JButton("Export to CSV");
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportToCSV();
            }
        });

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    new LoginGUI().setVisible(true);
                    dispose();
                }
            }
        });

        // Layout code for formPanel
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(idLabel)
                        .addComponent(patientNameLabel)
                        .addComponent(medicationLabel)
                        .addComponent(dosageLabel)
                        .addComponent(issueDateLabel)
                        .addComponent(administeredByLabel)
                        .addComponent(timeframeStartLabel)
                        .addComponent(timeframeEndLabel)
                        .addComponent(searchLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(idField)
                        .addComponent(patientNameField)
                        .addComponent(medicationField)
                        .addComponent(dosageField)
                        .addComponent(issueDatePicker)
                        .addComponent(administeredByField)
                        .addComponent(timeframeStartSpinner)
                        .addComponent(timeframeEndSpinner)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(searchField)
                                .addComponent(searchButton))
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(addButton)
                                .addComponent(updateButton)
                                .addComponent(deleteButton)
                                .addComponent(refreshButton)
                                .addComponent(wipeButton)
                                .addComponent(exportButton)
                                .addComponent(logoutButton)))
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(idLabel)
                        .addComponent(idField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(patientNameLabel)
                        .addComponent(patientNameField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(medicationLabel)
                        .addComponent(medicationField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(dosageLabel)
                        .addComponent(dosageField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(issueDateLabel)
                        .addComponent(issueDatePicker))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(administeredByLabel)
                        .addComponent(administeredByField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(timeframeStartLabel)
                        .addComponent(timeframeStartSpinner))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(timeframeEndLabel)
                        .addComponent(timeframeEndSpinner))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(searchLabel)
                        .addComponent(searchField)
                        .addComponent(searchButton))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(addButton)
                        .addComponent(updateButton)
                        .addComponent(deleteButton)
                        .addComponent(refreshButton)
                        .addComponent(wipeButton)
                        .addComponent(exportButton)
                        .addComponent(logoutButton))
        );

        add(formPanel, BorderLayout.SOUTH);

        // Initial data load
        updatePrescriptionTable();

        // Add mouse listener to the table to reflect selected prescription's information in the text fields
        prescriptionTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = prescriptionTable.getSelectedRow();
                if (selectedRow >= 0) {
                    Prescription selectedPrescription = tableModel.getPrescriptionAt(selectedRow);
                    idField.setText(String.valueOf(selectedPrescription.getPrescriptionId()));
                    patientNameField.setText(selectedPrescription.getPatientName());
                    medicationField.setText(selectedPrescription.getMedication());
                    dosageField.setText(selectedPrescription.getDosage());
                    try {
                        // Parse the issue date string to a Calendar object
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        java.util.Date utilDate = sdf.parse(selectedPrescription.getIssueDate());
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(utilDate); // Set the Calendar time to the parsed Date

                        // Now set the Calendar object to the date picker
                        timeframeStartSpinner.setValue(java.sql.Time.valueOf(selectedPrescription.getTimeframeStart()));
                        timeframeEndSpinner.setValue(java.sql.Time.valueOf(selectedPrescription.getTimeframeEnd()));
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                    administeredByField.setText(selectedPrescription.getAdministeredBy());
                    timeframeStartSpinner.setValue(java.sql.Time.valueOf(selectedPrescription.getTimeframeStart()));
                    timeframeEndSpinner.setValue(java.sql.Time.valueOf(selectedPrescription.getTimeframeEnd()));
                }
            }
        });
    }

    // Method to search prescription by various fields
    private void searchPrescription() {
        String searchText = searchField.getText().toLowerCase();
        List<Prescription> prescriptions = DatabaseUtil.getAllPrescriptions().stream()
                .filter(p -> String.valueOf(p.getPrescriptionId()).contains(searchText) ||
                        p.getPatientName().toLowerCase().contains(searchText) ||
                        p.getMedication().toLowerCase().contains(searchText) ||
                        p.getDosage().toLowerCase().contains(searchText) ||
                        p.getIssueDate().toLowerCase().contains(searchText) ||
                        p.getAdministeredBy().toLowerCase().contains(searchText) ||
                        p.getTimeframeStart().toLowerCase().contains(searchText) ||
                        p.getTimeframeEnd().toLowerCase().contains(searchText))
                .collect(Collectors.toList());
        tableModel.setPrescriptions(prescriptions);
    }

    private void addPrescription() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String patientName = patientNameField.getText().trim();
            String medication = medicationField.getText().trim();
            String dosage = dosageField.getText().trim();
            String issueDate = issueDatePicker.getJFormattedTextField().getText().trim();
            String administeredBy = administeredByField.getText().trim();
            String timeframeStart = ((JSpinner.DateEditor) timeframeStartSpinner.getEditor()).getFormat().format(timeframeStartSpinner.getValue());
            String timeframeEnd = ((JSpinner.DateEditor) timeframeEndSpinner.getEditor()).getFormat().format(timeframeEndSpinner.getValue());

            validateInputs(patientName, medication, dosage, issueDate, administeredBy, timeframeStart, timeframeEnd);

            // Check for duplicate ID
            List<Prescription> prescriptions = DatabaseUtil.getAllPrescriptions();
            if (prescriptions.stream().anyMatch(p -> p.getPrescriptionId() == id)) {
                throw new IllegalArgumentException("ID already exists. Please enter a unique ID.");
            }

            Prescription newPrescription = new Prescription(id, patientName, medication, dosage, issueDate, administeredBy, timeframeStart, timeframeEnd);
            DatabaseUtil.addPrescription(newPrescription);
            updatePrescriptionTable();
            JOptionPane.showMessageDialog(this, "Prescription added successfully.");
            clearFields();
        } catch (NumberFormatException ex) {
            handleException(ex, "Invalid input. Please enter valid numeric data where required.");
        } catch (IllegalArgumentException ex) {
            handleException(ex, ex.getMessage());
        } catch (Exception ex) {
            handleException(ex, "An error occurred while adding the prescription.");
        }
    }

    private void updatePrescription() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String patientName = patientNameField.getText().trim();
            String medication = medicationField.getText().trim();
            String dosage = dosageField.getText().trim();
            String issueDate = issueDatePicker.getJFormattedTextField().getText().trim();
            String administeredBy = administeredByField.getText().trim();
            String timeframeStart = ((JSpinner.DateEditor) timeframeStartSpinner.getEditor()).getFormat().format(timeframeStartSpinner.getValue());
            String timeframeEnd = ((JSpinner.DateEditor) timeframeEndSpinner.getEditor()).getFormat().format(timeframeEndSpinner.getValue());

            validateInputs(patientName, medication, dosage, issueDate, administeredBy, timeframeStart, timeframeEnd);

            Prescription updatedPrescription = new Prescription(id, patientName, medication, dosage, issueDate, administeredBy, timeframeStart, timeframeEnd);
            DatabaseUtil.updatePrescription(updatedPrescription);
            updatePrescriptionTable();
            JOptionPane.showMessageDialog(this, "Prescription updated successfully.");
            clearFields();
        } catch (NumberFormatException ex) {
            handleException(ex, "Invalid input. Please enter valid numeric data where required.");
        } catch (IllegalArgumentException ex) {
            handleException(ex, ex.getMessage());
        } catch (Exception ex) {
            handleException(ex, "An error occurred while updating the prescription.");
        }
    }

    private void deletePrescription() {
        try {
            int id = Integer.parseInt(idField.getText());
            DatabaseUtil.deletePrescription(id);
            updatePrescriptionTable();
            JOptionPane.showMessageDialog(this, "Prescription deleted successfully.");
            clearFields();
        } catch (NumberFormatException ex) {
            handleException(ex, "Invalid ID. Please enter a valid ID.");
        } catch (Exception ex) {
            handleException(ex, "An error occurred while deleting the prescription.");
        }
    }

    private void updatePrescriptionTable() {
        List<Prescription> prescriptions = DatabaseUtil.getAllPrescriptions();
        tableModel.setPrescriptions(prescriptions);
    }

    private void clearFields() {
        idField.setText("");
        patientNameField.setText("");
        medicationField.setText("");
        dosageField.setText("");
        issueDatePicker.getJFormattedTextField().setText("");
        administeredByField.setText("");
        timeframeStartSpinner.setValue(new java.util.Date());
        timeframeEndSpinner.setValue(new java.util.Date());
    }

    private void wipePrescriptionTable() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to wipe all prescription data?", "Confirm Wipe", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            DatabaseUtil.wipeAllPrescriptions();
            updatePrescriptionTable();
            JOptionPane.showMessageDialog(this, "All prescription data wiped successfully.");
        }
    }

    private void exportToCSV() {
        String fileName = "prescriptions.csv";
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            // Write header
            fileWriter.append("ID,Patient Name,Medication,Dosage,Issue Date,Administered By,Timeframe Start,Timeframe End\n");

            // Write prescription data
            List<Prescription> prescriptions = DatabaseUtil.getAllPrescriptions();
            for (Prescription prescription : prescriptions) {
                fileWriter.append(prescription.getPrescriptionId() + ",");
                fileWriter.append(prescription.getPatientName() + ",");
                fileWriter.append(prescription.getMedication() + ",");
                fileWriter.append(prescription.getDosage() + ",");
                fileWriter.append(prescription.getIssueDate() + ",");
                fileWriter.append(prescription.getAdministeredBy() + ",");
                fileWriter.append(prescription.getTimeframeStart() + ",");
                fileWriter.append(prescription.getTimeframeEnd() + "\n");
            }
            JOptionPane.showMessageDialog(this, "Exported to CSV successfully.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error writing to CSV file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void validateInputs(String patientName, String medication, String dosage, String issueDate, String administeredBy, String timeframeStart, String timeframeEnd) {
        if (!patientName.matches("[a-zA-Z ]+")) {
            throw new IllegalArgumentException("Patient name must contain only alphabetical characters and spaces.");
        }

        if (patientName.isEmpty() || medication.isEmpty() || dosage.isEmpty() || issueDate.isEmpty() || administeredBy.isEmpty() || timeframeStart.isEmpty() || timeframeEnd.isEmpty()) {
            throw new IllegalArgumentException("All fields must be filled.");
        }

        // Validate issue date format (YYYY-MM-DD)
        if (!issueDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new IllegalArgumentException("Issue date must be in the format YYYY-MM-DD.");
        }

        // Validate dosage as a number
        try {
            Double.parseDouble(dosage);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Dosage must be a number.");
        }
    }

    private void handleException(Exception ex, String userMessage) {
        if (ex instanceof NumberFormatException) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid numeric data where required.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        } else if (ex instanceof IllegalArgumentException) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid Input", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, userMessage, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginGUI().setVisible(true);
        });
    }
}