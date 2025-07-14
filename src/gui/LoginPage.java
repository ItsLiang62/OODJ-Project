package gui;

import customExceptions.EmailNotFoundException;
import database.Database;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage extends JFrame implements ActionListener {
    private JLabel title;
    private JLabel emailLabel;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JButton loginButton;
    private JLabel statusLabel;

    public LoginPage() {

        this.title = new JLabel("Login to APU Medical Center");
        this.emailLabel = new JLabel("Email:");
        this.emailField = new JTextField(15);
        this.passwordLabel = new JLabel("Password:");
        this.passwordField = new JPasswordField(15);
        this.loginButton = new JButton("Login");
        this.statusLabel = new JLabel("");

        this.setTitle("Login Page");
        this.setSize(700, 500);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.add(this.title);

        JPanel credentialPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        credentialPanel.add(this.emailLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        credentialPanel.add(this.passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        credentialPanel.add(this.emailField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        credentialPanel.add(this.passwordField, gbc);

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));

        this.loginButton.addActionListener(this);
        this.loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginPanel.add(this.loginButton);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        this.statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginPanel.add(this.statusLabel);

        this.add(titlePanel, BorderLayout.NORTH);
        this.add(credentialPanel, BorderLayout.CENTER);
        this.add(loginPanel, BorderLayout.SOUTH);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        statusLabel.setText("");
        String email = emailField.getText();
        String password = String.valueOf(passwordField.getPassword());
        String userId;
        try {
            userId = Database.getUserIdWithEmail(email);
        } catch (EmailNotFoundException exception) {
            statusLabel.setForeground(Color.RED);
            statusLabel.setText("Email is not registered. Please contact the manager if you think this is a mistake.");
            return;
        }
        switch (userId.charAt(0)) {
            case 'M':
                if (!password.equals(Database.getManager(userId).getPassword())) {
                    statusLabel.setText("Password incorrect. Login failed.");
                    return;
                } else {
                    statusLabel.setText("Login successful.");
                    return;
                }
            case 'S':
                if (!password.equals(Database.getStaff(userId).getPassword())) {
                    statusLabel.setText("Password incorrect. Login failed.");
                    return;
                } else {
                    statusLabel.setText("Login successful.");
                    return;
                }
            case 'D':
                if (!password.equals(Database.getDoctor(userId).getPassword())) {
                    statusLabel.setText("Password incorrect. Login failed.");
                    return;
                } else {
                    statusLabel.setText("Login successful.");
                    return;
                }
            case 'C':
                if (!password.equals(Database.getCustomer(userId).getPassword())) {
                    statusLabel.setText("Password incorrect. Login failed.");
                    return;
                } else {
                    statusLabel.setText("Login successful.");
                    return;
                }
            default:
                statusLabel.setText("Email points to an invalid User ID.");
        }
    }
}
