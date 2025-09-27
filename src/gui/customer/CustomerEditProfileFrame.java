package gui.customer;

import user.Customer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerEditProfileFrame extends javax.swing.JFrame {
    
    private final Customer customerUser;
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JLabel walletLabel;

    public CustomerEditProfileFrame(Customer customerUser) {
        this.customerUser = customerUser;
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Edit Profile - " + customerUser.getName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);

        // Create main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 248, 255));

        // Create title label
        JLabel titleLabel = new JLabel("Edit Your Profile", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 51, 102));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Create form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(240, 248, 255));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Wallet info
        walletLabel = new JLabel(String.format("Current Wallet Balance: $%.2f", customerUser.getApWallet()));
        walletLabel.setFont(new Font("Arial", Font.BOLD, 14));
        walletLabel.setForeground(new Color(0, 100, 0));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(walletLabel, gbc);

        // Name field
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Full Name:"), gbc);
        
        nameField = new JTextField(customerUser.getName(), 20);
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1; gbc.gridy = 1;
        formPanel.add(nameField, gbc);

        // Email field
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Email Address:"), gbc);
        
        emailField = new JTextField(customerUser.getEmail(), 20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1; gbc.gridy = 2;
        formPanel.add(emailField, gbc);

        // Password field
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("New Password:"), gbc);
        
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setToolTipText("Leave blank to keep current password");
        gbc.gridx = 1; gbc.gridy = 3;
        formPanel.add(passwordField, gbc);

        // Confirm Password field
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Confirm Password:"), gbc);
        
        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 14));
        confirmPasswordField.setToolTipText("Leave blank to keep current password");
        gbc.gridx = 1; gbc.gridy = 4;
        formPanel.add(confirmPasswordField, gbc);

        // Info label
        JLabel infoLabel = new JLabel("Note: Leave password fields blank to keep current password");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        infoLabel.setForeground(Color.GRAY);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        formPanel.add(infoLabel, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(240, 248, 255));

        JButton saveButton = createStyledButton("Save Changes");
        JButton cancelButton = createStyledButton("Cancel");
        JButton backButton = createStyledButton("Back to Main");

        saveButton.addActionListener(new SaveButtonListener());
        cancelButton.addActionListener(new CancelButtonListener());
        backButton.addActionListener(new BackButtonListener());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private boolean validateForm() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        // Validate name
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your name.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            nameField.requestFocus();
            return false;
        }

        // Validate email
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your email address.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            emailField.requestFocus();
            return false;
        }

        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            emailField.requestFocus();
            return false;
        }

        // Validate password if provided
        if (!password.isEmpty()) {
            if (password.length() < 6) {
                JOptionPane.showMessageDialog(this, "Password must be at least 6 characters long.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                passwordField.requestFocus();
                return false;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                confirmPasswordField.requestFocus();
                return false;
            }
        }

        return true;
    }

    private class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!validateForm()) {
                return;
            }

            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            int confirm = JOptionPane.showConfirmDialog(CustomerEditProfileFrame.this, 
                "Are you sure you want to save these changes?", "Confirm Save", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    // Update name if changed
                    if (!name.equals(customerUser.getName())) {
                        customerUser.setName(name);
                    }

                    // Update email if changed
                    if (!email.equals(customerUser.getEmail())) {
                        customerUser.setEmail(email);
                    }

                    // Update password if provided
                    if (!password.isEmpty()) {
                        customerUser.setPassword(password);
                    }

                    JOptionPane.showMessageDialog(CustomerEditProfileFrame.this, 
                        "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Clear password fields
                    passwordField.setText("");
                    confirmPasswordField.setText("");
                    
                } catch (HeadlessException ex) {
                    JOptionPane.showMessageDialog(CustomerEditProfileFrame.this, 
                        "Error updating profile: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private class CancelButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Reset form to original values
            nameField.setText(customerUser.getName());
            emailField.setText(customerUser.getEmail());
            passwordField.setText("");
            confirmPasswordField.setText("");
            
            JOptionPane.showMessageDialog(CustomerEditProfileFrame.this, 
                "Changes discarded.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> {
                CustomerMainPage mainPage = new CustomerMainPage(customerUser);
                mainPage.setVisible(true);
            });
            dispose();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 627, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 435, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
