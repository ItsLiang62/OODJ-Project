/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui.customer;
import user.Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 *
 * @author Adrian Liew Ren Qian
 */
public class TopUpWalletFrame extends javax.swing.JFrame {
    
    private final Customer customerUser;
    private JLabel currentBalanceLabel;
    private JTextField amountField;
    private JComboBox<String> presetAmountCombo;
    /**
     * Creates new form TopUpWalletFrame
     * @param customerUser
     * @param mainPage
     */
    public TopUpWalletFrame(Customer customerUser, CustomerMainPage mainPage) {
        this.customerUser = customerUser;
        initializeUI();
    }
    private void initializeUI() {
        setTitle("Top Up Wallet - " + customerUser.getName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        // Create main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 248, 255));

        // Create title label
        JLabel titleLabel = new JLabel("Top Up Your Wallet", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 51, 102));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Create center panel with form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(240, 248, 255));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Current balance
        currentBalanceLabel = new JLabel(String.format("Current Balance: $%.2f", customerUser.getApWallet()));
        currentBalanceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        currentBalanceLabel.setForeground(new Color(0, 100, 0));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(currentBalanceLabel, gbc);

        // Preset amounts
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Quick Select:"), gbc);
        
        String[] presetAmounts = {"$10", "$20", "$50", "$100", "$200", "$500"};
        presetAmountCombo = new JComboBox<>(presetAmounts);
        presetAmountCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        presetAmountCombo.addActionListener(new PresetAmountListener());
        gbc.gridx = 1; gbc.gridy = 1;
        formPanel.add(presetAmountCombo, gbc);

        // Custom amount
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Custom Amount:"), gbc);
        
        amountField = new JTextField(15);
        amountField.setFont(new Font("Arial", Font.PLAIN, 14));
        amountField.setToolTipText("Enter amount to top up (e.g., 25.50)");
        gbc.gridx = 1; gbc.gridy = 2;
        formPanel.add(amountField, gbc);

        // Currency symbol label
        JLabel currencyLabel = new JLabel("$");
        currencyLabel.setFont(new Font("Arial", Font.BOLD, 14));
        currencyLabel.setForeground(Color.GRAY);
        gbc.gridx = 2; gbc.gridy = 2; gbc.gridwidth = 1;
        formPanel.add(currencyLabel, gbc);

        // Payment method
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Payment Method:"), gbc);
        
        String[] paymentMethods = {"Credit/Debit Card", "PayPal", "Bank Transfer", "Google Pay", "Apple Pay"};
        JComboBox<String> paymentMethodCombo = new JComboBox<>(paymentMethods);
        paymentMethodCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1; gbc.gridy = 3;
        formPanel.add(paymentMethodCombo, gbc);

        // Info label
        JLabel infoLabel = new JLabel("Minimum top-up amount: $5.00");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        infoLabel.setForeground(Color.GRAY);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        formPanel.add(infoLabel, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(240, 248, 255));

        JButton topUpButton = createStyledButton("Top Up Now", new Color(50, 205, 50)); // Green color
        JButton clearButton = createStyledButton("Clear");
        JButton backButton = createStyledButton("Back to Main");

        topUpButton.addActionListener(new TopUpButtonListener());
        clearButton.addActionListener(new ClearButtonListener());
        backButton.addActionListener(new BackButtonListener());

        buttonPanel.add(topUpButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);
    }

    private JButton createStyledButton(String text) {
        return createStyledButton(text, new Color(70, 130, 180));
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private boolean validateAmount() {
        String amountText = amountField.getText().trim();
        
        if (amountText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an amount to top up.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            amountField.requestFocus();
            return false;
        }

        try {
            double amount = Double.parseDouble(amountText);
            
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be greater than zero.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                amountField.requestFocus();
                return false;
            }
            
            if (amount < 5.0) {
                JOptionPane.showMessageDialog(this, "Minimum top-up amount is $5.00.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                amountField.requestFocus();
                return false;
            }
            
            if (amount > 10000.0) {
                JOptionPane.showMessageDialog(this, "Maximum top-up amount is $10,000.00.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                amountField.requestFocus();
                return false;
            }
            
            return true;
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount (e.g., 25.50).", "Validation Error", JOptionPane.ERROR_MESSAGE);
            amountField.requestFocus();
            return false;
        }
    }

    private class PresetAmountListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedAmount = (String) presetAmountCombo.getSelectedItem();
            if (selectedAmount != null) {
                // Remove the dollar sign and set to amount field
                String amount = selectedAmount.substring(1);
                amountField.setText(amount);
            }
        }
    }

    private class TopUpButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!validateAmount()) {
                return;
            }

            double amount = Double.parseDouble(amountField.getText().trim());
            double newBalance = customerUser.getApWallet() + amount;

            int confirm = JOptionPane.showConfirmDialog(TopUpWalletFrame.this, 
                String.format("Confirm top-up of $%.2f?\nNew balance will be: $%.2f", amount, newBalance),
                "Confirm Top-Up", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    // Process the top-up
                    customerUser.topUpApWallet(amount);
                    
                    JOptionPane.showMessageDialog(TopUpWalletFrame.this, 
                        String.format("Top-up successful!\n$%.2f has been added to your wallet.\nNew balance: $%.2f", 
                        amount, customerUser.getApWallet()), 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Update current balance display
                    currentBalanceLabel.setText(String.format("Current Balance: $%.2f", customerUser.getApWallet()));
                    
                    
                    // Clear amount field
                    amountField.setText("");
                    
                } catch (HeadlessException ex) {
                    JOptionPane.showMessageDialog(TopUpWalletFrame.this, 
                        "Error processing top-up: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private class ClearButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            amountField.setText("");
            presetAmountCombo.setSelectedIndex(0);
        }
    }

    private class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    }

    // Method to update balance display (can be called from outside if needed)
    public void updateBalanceDisplay() {
        currentBalanceLabel.setText(String.format("Current Balance: $%.2f", customerUser.getApWallet()));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 690, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 389, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
    /* Set the Nimbus look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CustomerAppointmentsFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            // Customer testCustomer = new Customer("test", "Test User", "test@email.com", "password", 100.0);
            // new CustomerAppointmentsFrame(testCustomer).setVisible(true);
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
