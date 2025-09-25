package gui.manager;

import customExceptions.EmailNotFoundException;
import database.Database;
import gui.customer.CustomerMainPage;
import gui.staff.StaffMainPage;
import gui.doctor.DoctorMainPage;
import user.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage extends JFrame {

    public LoginPage() {
        JLabel title = new JLabel("Login to APU Medical Center");
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(15);
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(15);
        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(this.new LoginButtonListener(emailField, passwordField));

        setTitle("Login Page");
        setSize(700, 500);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Design starts here

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.add(title);

        JPanel credentialPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        credentialPanel.add(emailLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        credentialPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        credentialPanel.add(emailField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        credentialPanel.add(passwordField, gbc);

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));

        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginPanel.add(loginButton);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 50)));

        add(titlePanel, BorderLayout.NORTH);
        add(credentialPanel, BorderLayout.CENTER);
        add(loginPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private class LoginButtonListener implements ActionListener {
        JTextField emailField;
        JPasswordField passwordField;

        public LoginButtonListener(JTextField emailField, JPasswordField passwordField) {
            this.emailField = emailField;
            this.passwordField = passwordField;
        }

        public void showLoginSuccessfulMessage() {
            JOptionPane.showMessageDialog(null, "Login successful!");
        }

        public void showEmailNotRegisteredErrorMessage() {
            JOptionPane.showMessageDialog(null, "Email is not registered. Please contact the manager if you think this is a mistake.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }

        public void showIncorrectPasswordErrorMessage() {
            JOptionPane.showMessageDialog(null, "Password is incorrect!", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String email = emailField.getText();
            String password = String.valueOf(passwordField.getPassword());
            String userId;
            try {
                userId = User.getIdByEmail(email);
            } catch (EmailNotFoundException exception) {
                showEmailNotRegisteredErrorMessage();
                return;
            }
            switch (userId.charAt(0)) {
                case 'M':
                    if (!password.equals(Manager.getById(userId).getPassword())) {
                        showIncorrectPasswordErrorMessage();
                    } else {
                        showLoginSuccessfulMessage();
                        SwingUtilities.invokeLater(() -> new ManagerMainPage(Manager.getById(userId)));
                        dispose();
                    }
                    return;
                case 'S':
                    if (!password.equals(Staff.getById(userId).getPassword())) {
                       showIncorrectPasswordErrorMessage();
                    } else {
                        showLoginSuccessfulMessage();
                        SwingUtilities.invokeLater(() -> new StaffMainPage(Staff.getById(userId)));
                        dispose();
                    }
                    return;
                case 'D':
                    if (!password.equals(Doctor.getById(userId).getPassword())) {
                        showIncorrectPasswordErrorMessage();
                    } else {
                        showLoginSuccessfulMessage();
                        SwingUtilities.invokeLater(() -> new DoctorMainPage(Doctor.getById(userId)));
                        dispose();                        
                    }
                    return;
                case 'C':
                    if (!password.equals(Customer.getById(userId).getPassword())) {
                        showIncorrectPasswordErrorMessage();
                    } else {
                        showLoginSuccessfulMessage();
                        SwingUtilities.invokeLater(() -> new CustomerMainPage(Customer.getById(userId)));
                        dispose();
                    }
                    return;
                default:
                    JOptionPane.showMessageDialog(null, "Email points to an invalid User ID.", "Unexpected Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
