package src;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateAccountGUI extends JFrame {
    private JTextField userField;
    private JPasswordField passField;
    private JPasswordField confirmPassField;

    public CreateAccountGUI() {
        setTitle("MyManager : Prescription Management System - Create Account");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        userField = new JTextField(20);
        userField.setBounds(100, 20, 165, 25);
        panel.add(userField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(10, 50, 80, 25);
        panel.add(passLabel);

        passField = new JPasswordField(20);
        passField.setBounds(100, 50, 165, 25);
        panel.add(passField);

        JLabel confirmPassLabel = new JLabel("Confirm Password:");
        confirmPassLabel.setBounds(10, 80, 150, 25);
        panel.add(confirmPassLabel);

        confirmPassField = new JPasswordField(20);
        confirmPassField.setBounds(150, 80, 165, 25);
        panel.add(confirmPassField);

        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.setBounds(10, 110, 150, 25);
        panel.add(createAccountButton);

        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = new String(passField.getPassword());
                String confirmPassword = new String(confirmPassField.getPassword());

                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(null, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (DatabaseUtil.createUser(username, password)) {
                    JOptionPane.showMessageDialog(null, "Account created successfully!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to create account.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static void main(String[] args) {
        new CreateAccountGUI();
    }
}