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
import java.util.*;
import java.util.List;
import java.util.Queue;
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
    private JSpinner prescribedTimeSpinner, timeAdministeredSpinner;
    private JTextField searchField;

    public PrescriptionGUI() {
        setTitle("MyManager : Prescription Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
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
        formPanel.setBackground(new Color(230, 240, 255)); // Light blue background for hospital setting
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
        JLabel prescribedTimeLabel = new JLabel("Prescribed Time:");

        // Initialize and configure the prescribed time spinner
        SpinnerDateModel startModel = new SpinnerDateModel();
        prescribedTimeSpinner = new JSpinner(startModel);
        JSpinner.DateEditor startEditor = new JSpinner.DateEditor(prescribedTimeSpinner, "hh:mm a");
        prescribedTimeSpinner.setEditor(startEditor);

        JLabel timeAdministeredLabel = new JLabel("Time Administered:");

        // Initialize and configure the time administered spinner
        SpinnerDateModel endModel = new SpinnerDateModel();
        timeAdministeredSpinner = new JSpinner(endModel);
        JSpinner.DateEditor endEditor = new JSpinner.DateEditor(timeAdministeredSpinner, "hh:mm a");
        timeAdministeredSpinner.setEditor(endEditor);

        // Search field and button
        JLabel searchLabel = new JLabel("Search:");
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        searchButton.setBackground(new Color(144, 238, 144)); // Light green background for search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchPrescription();
            }
        });

        // Refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setBackground(new Color(144, 238, 144)); // Light green background for refresh button
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePrescriptionTable();
            }
        });

        // Add, Update, Delete, Wipe, Export buttons
        JButton addButton = new JButton("Add Prescription");
        addButton.setBackground(new Color(144, 238, 144)); // Light green background for add button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPrescription();
            }
        });

        JButton updateButton = new JButton("Update Prescription");
        updateButton.setBackground(new Color(144, 238, 144)); // Light green background for update button
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePrescription();
            }
        });

        JButton deleteButton = new JButton("Delete Prescription");
        deleteButton.setBackground(new Color(255, 99, 71)); // Light red background for delete button
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletePrescription();
            }
        });

        JButton wipeButton = new JButton("Wipe Table");
        wipeButton.setBackground(new Color(255, 99, 71)); // Light red background for wipe button
        wipeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wipePrescriptionTable();
            }
        });

        JButton exportButton = new JButton("Export to CSV");
        exportButton.setBackground(new Color(144, 238, 144)); // Light green background for export button
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Prescription.removeElementsBasedOnTimeframe();
                exportToCSV();
            }
        });

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(255, 99, 71)); // Light red background for logout button
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

        // Add Sort and Queue buttons
        JButton sortButton = new JButton("Sort Prescriptions");
        sortButton.setBackground(new Color(144, 238, 144)); // Light green background for sort button
        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortPrescriptions();
            }
        });

        JButton queueButton = new JButton("Queue Prescription");
        queueButton.setBackground(new Color(144, 238, 144)); // Light green background for queue button
        queueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                queuePrescription();
            }
        });

        JButton saveQueueToDatabaseButton = new JButton("Save Queue to Database");
        saveQueueToDatabaseButton.setBackground(new Color(144, 238, 144));
        saveQueueToDatabaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Prescription.removeElementsBasedOnTimeframe();
                DatabaseUtil.saveQueueToDatabase();
                JOptionPane.showMessageDialog(null, "Queue saved to database successfully.");
            }
        });

        JButton wipeQueueButton = new JButton("Wipe Queue");
        wipeQueueButton.setBackground(new Color(255, 99, 71));
        wipeQueueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Prescription.prescriptionQueue.clear();
                JOptionPane.showMessageDialog(null, "Queue wiped successfully.");
            }
        });

        // Add Sorting Criteria Dropdown
        JLabel sortCriteriaLabel = new JLabel("Sort By:");
        String[] sortOptions = {"Dosage", "Patient Name", "Medication", "Date", "Time"};
        JComboBox<String> sortCriteriaComboBox = new JComboBox<>(sortOptions);

        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCriteria = (String) sortCriteriaComboBox.getSelectedItem();
                List<Prescription> prescriptions = DatabaseUtil.getAllPrescriptions();
                DatabaseUtil.sortPrescriptions(prescriptions, Collections.singletonList(selectedCriteria.toLowerCase().replace(" ", "")));
                tableModel.setPrescriptions(prescriptions);
                JOptionPane.showMessageDialog(null, "Prescriptions sorted by " + selectedCriteria, "Sort Completed", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Step 2: Add Queue Operations Buttons
        JButton viewQueueButton = new JButton("View Queue");
        viewQueueButton.setBackground(new Color(144, 238, 144));
        viewQueueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Prescription.prescriptionQueue == null || Prescription.prescriptionQueue.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "The queue is not initialized or is empty.", "Queue Status", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    StringBuilder queueContent = new StringBuilder();
                    Queue<String[][]> queue = Prescription.prescriptionQueue; // Directly access the queue
                    for (String[][] data : queue) {
                        queueContent.append(Arrays.deepToString(data)).append("\n");
                    }
                    JOptionPane.showMessageDialog(null, queueContent.toString(), "Queue Content", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        JButton checkQueueButton = new JButton("Check Queue Empty");
        checkQueueButton.setBackground(new Color(144, 238, 144));
        checkQueueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isEmpty = Prescription.isQueueEmpty();
                JOptionPane.showMessageDialog(null, "Queue is " + (isEmpty ? "empty." : "not empty."), "Queue Status", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JButton removeFromQueueButton = new JButton("Remove from Queue");
        removeFromQueueButton.setBackground(new Color(255, 99, 71)); // Light red background for remove button
        removeFromQueueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeFromQueue();
            }
        });

        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(idLabel)
                .addComponent(patientNameLabel)
                .addComponent(medicationLabel)
                .addComponent(dosageLabel)
                .addComponent(issueDateLabel)
                .addComponent(administeredByLabel)
                .addComponent(prescribedTimeLabel)
                .addComponent(timeAdministeredLabel)
                .addComponent(searchLabel)
                .addComponent(sortCriteriaLabel))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(idField)
                .addComponent(patientNameField)
                .addComponent(medicationField)
                .addComponent(dosageField)
                .addComponent(issueDatePicker)
                .addComponent(administeredByField)
                .addComponent(prescribedTimeSpinner)
                .addComponent(timeAdministeredSpinner)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(searchField)
                    .addComponent(searchButton))
                .addComponent(sortCriteriaComboBox)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(addButton)
                    .addComponent(updateButton)
                    .addComponent(deleteButton)
                    .addComponent(refreshButton)
                    .addComponent(wipeButton)
                    .addComponent(exportButton)
                    .addComponent(logoutButton))
                .addGroup(layout.createSequentialGroup()
                    .addComponent(sortButton)
                    .addComponent(queueButton)
                    .addComponent(viewQueueButton)
                    .addComponent(checkQueueButton)
                    .addComponent(wipeQueueButton)
                    .addComponent(saveQueueToDatabaseButton)))
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
                .addComponent(prescribedTimeLabel)
                .addComponent(prescribedTimeSpinner))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(timeAdministeredLabel)
                .addComponent(timeAdministeredSpinner))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(searchLabel)
                .addComponent(searchField)
                .addComponent(searchButton))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(sortCriteriaLabel)
                .addComponent(sortCriteriaComboBox))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(addButton)
                .addComponent(updateButton)
                .addComponent(deleteButton)
                .addComponent(refreshButton)
                .addComponent(wipeButton)
                .addComponent(exportButton)
                .addComponent(logoutButton))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(sortButton)
                .addComponent(queueButton)
                .addComponent(viewQueueButton)
                .addComponent(checkQueueButton)
                .addComponent(wipeQueueButton)
                .addComponent(saveQueueToDatabaseButton))
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
                    administeredByField.setText(selectedPrescription.getAdministeredBy());
                    try {
                        // Parse the issue date string to a Calendar object
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        java.util.Date utilDate = sdf.parse(selectedPrescription.getIssueDate());
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(utilDate); // Set the Calendar time to the parsed Date
        
                        // Now set the Calendar object to the date picker
                        issueDatePicker.getModel().setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                        issueDatePicker.getModel().setSelected(true);
        
                        // Ensure time values are in the correct format
                        String prescribedTime = selectedPrescription.getPrescribedTime();
                        String timeAdministered = selectedPrescription.getTimeAdministered();
        
                        // Validate and set time values
                        if (prescribedTime != null && !prescribedTime.matches("\\d{2}:\\d{2} [AP]M")) {
                            throw new IllegalArgumentException("Prescribed time must be in the format hh:mm AM/PM");
                        }
                        if (timeAdministered != null && !timeAdministered.matches("\\d{2}:\\d{2} [AP]M")) {
                            throw new IllegalArgumentException("Time administered must be in the format hh:mm AM/PM");
                        }
        
                        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
                        prescribedTimeSpinner.setValue(timeFormat.parse(prescribedTime));
                        timeAdministeredSpinner.setValue(timeFormat.parse(timeAdministered));
                    } catch (ParseException | IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(PrescriptionGUI.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }
    private void searchPrescription() {
        String searchText = searchField.getText().toLowerCase();
        List<Prescription> prescriptions = DatabaseUtil.getAllPrescriptions().stream()
                .filter(p -> String.valueOf(p.getPrescriptionId()).contains(searchText) ||
                        p.getPatientName().toLowerCase().contains(searchText) ||
                        p.getMedication().toLowerCase().contains(searchText) ||
                        p.getDosage().toLowerCase().contains(searchText) ||
                        p.getIssueDate().toLowerCase().contains(searchText) ||
                        p.getAdministeredBy().toLowerCase().contains(searchText) ||
                        p.getPrescribedTime().toLowerCase().contains(searchText) ||
                        p.getTimeAdministered().toLowerCase().contains(searchText))
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
            String prescribedTime = ((JSpinner.DateEditor) prescribedTimeSpinner.getEditor()).getFormat().format(prescribedTimeSpinner.getValue());
            String timeAdministered = ((JSpinner.DateEditor) timeAdministeredSpinner.getEditor()).getFormat().format(timeAdministeredSpinner.getValue());
    
            validateInputs(patientName, medication, dosage, issueDate, administeredBy, prescribedTime, timeAdministered);
    
            // Check for duplicate ID
            List<Prescription> prescriptions = DatabaseUtil.getAllPrescriptions();
            if (prescriptions.stream().anyMatch(p -> p.getPrescriptionId() == id)) {
                throw new IllegalArgumentException("ID already exists. Please enter a unique ID.");
            }
    
            Prescription newPrescription = new Prescription(id, patientName, medication, dosage, issueDate, administeredBy, prescribedTime, timeAdministered);
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
            String timeframeStart = ((JSpinner.DateEditor) prescribedTimeSpinner.getEditor()).getFormat().format(prescribedTimeSpinner.getValue());
            String timeframeEnd = ((JSpinner.DateEditor) timeAdministeredSpinner.getEditor()).getFormat().format(timeAdministeredSpinner.getValue());

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
        prescribedTimeSpinner.setValue(new java.util.Date());
        timeAdministeredSpinner.setValue(new java.util.Date());
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
            fileWriter.append("ID,Patient Name,Medication,Dosage,Issue Date,Administered By,Prescribed Time,Time Administered\n");
    
            // Write prescription data
            List<Prescription> prescriptions = DatabaseUtil.getAllPrescriptions();
            for (Prescription prescription : prescriptions) {
                fileWriter.append(prescription.getPrescriptionId() + ",");
                fileWriter.append(prescription.getPatientName() + ",");
                fileWriter.append(prescription.getMedication() + ",");
                fileWriter.append(prescription.getDosage() + ",");
                fileWriter.append(prescription.getIssueDate() + ",");
                fileWriter.append(prescription.getAdministeredBy() + ",");
                fileWriter.append(prescription.getPrescribedTime() + ",");
                fileWriter.append(prescription.getTimeAdministered() + "\n");
            }
    
            // Write queue data
            for (String[][] prescriptionData : Prescription.prescriptionQueue) {
                for (String[] row : prescriptionData) {
                    fileWriter.append(String.join(",", row)).append("\n");
                }
            }
            JOptionPane.showMessageDialog(this, "Exported to CSV successfully.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error writing to CSV file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }

    private void validateInputs(String patientName, String medication, String dosage, String issueDate, String administeredBy, String prescribedTime, String timeAdministered) {
        if (!patientName.matches("[a-zA-Z ]+")) {
            throw new IllegalArgumentException("Patient name must contain only alphabetical characters and spaces.");
        }
    
        if (patientName.isEmpty() || medication.isEmpty() || dosage.isEmpty() || issueDate.isEmpty() || administeredBy.isEmpty() || prescribedTime.isEmpty() || timeAdministered.isEmpty()) {
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
    
        // Validate timeframe formats (hh:mm AM/PM)
        if (!timeframeStart.matches("\\d{2}:\\d{2} [AP]M")) {
            throw new IllegalArgumentException("Prescribed Time must be in the format hh:mm AM/PM.");
        }
        if (!timeframeEnd.matches("\\d{2}:\\d{2} [AP]M")) {
            throw new IllegalArgumentException("Time Administered must be in the format hh:mm AM/PM.");
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

    private void sortPrescriptions() {
    String[] criteria = {"dosage", "patientname", "medication", "date", "time"}; // Example criteria
    List<String> sortBy = Arrays.asList(criteria);
    List<Prescription> prescriptions = DatabaseUtil.getAllPrescriptions();
    DatabaseUtil.sortPrescriptions(prescriptions, sortBy);
    tableModel.setPrescriptions(prescriptions);
}
private void queuePrescription() {
    String[][] prescriptionData = new String[][] {
        { idField.getText(), patientNameField.getText(), medicationField.getText(), dosageField.getText(), issueDatePicker.getModel().getValue().toString(), administeredByField.getText(), prescribedTimeSpinner.getValue().toString(), timeAdministeredSpinner.getValue().toString() }
    };
    Prescription.addToQueue(prescriptionData);
    JOptionPane.showMessageDialog(this, "Prescription queued successfully!");
}

private void removeFromQueue() {
    String idToRemove = JOptionPane.showInputDialog(this, "Enter the ID of the prescription to remove from the queue:");

    if (idToRemove != null && !idToRemove.trim().isEmpty()) {
        boolean removed = false;
        Queue<String[][]> updatedQueue = new LinkedList<>();
        
        for (String[][] data : Prescription.prescriptionQueue) {
            if (data[0][0].equals(idToRemove)) {
                removed = true;
            } else {
                updatedQueue.add(data);
            }
        }
        
        if (removed) {
            Prescription.prescriptionQueue = updatedQueue;
            JOptionPane.showMessageDialog(this, "Prescription with ID " + idToRemove + " removed from the queue.");
        } else {
            JOptionPane.showMessageDialog(this, "No prescription with ID " + idToRemove + " found in the queue.");
        }
    }
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginGUI().setVisible(true);
        });
    }
}