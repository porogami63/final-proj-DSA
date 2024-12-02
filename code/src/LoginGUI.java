package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI extends JFrame {
    private JTextField userField;
    private JPasswordField passField;

    public LoginGUI() {
        setTitle("MyManager : Prescription Management System - Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(230, 240, 255)); // Light blue background for hospital setting
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

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(10, 80, 80, 25);
        loginButton.setBackground(new Color(144, 238, 144)); // Light green background for login button
        panel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = userField.getText();
                String password = new String(passField.getPassword());

                if (user.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter username and password.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (DatabaseUtil.authenticateUser(user, password)) {
                    JOptionPane.showMessageDialog(null, "Login successful!");
                    new PrescriptionGUI().setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.setBounds(100, 80, 150, 25);
        createAccountButton.setBackground(new Color(255, 99, 71)); // Light red background for create account button
        panel.add(createAccountButton);

        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CreateAccountGUI();
            }
        });
    }

    public static void main(String[] args) {
        new LoginGUI();
    }
}