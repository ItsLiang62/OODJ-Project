/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui.customer;
import user.Customer;
import operation.CustomerFeedback;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 *
 * @author Adrian Liew Ren Qian
 */
public class CustomerFeedbackFrame extends javax.swing.JFrame {
    
    private final Customer customerUser;
    private JTable feedbackTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> employeeComboBox;
    private JTextArea feedbackTextArea;

    /**
     * Creates new form CustomerFeedbackFrame
     * @param customerUser
     */
    public CustomerFeedbackFrame(Customer customerUser) {
        this.customerUser = customerUser;
        initializeUI();
        loadFeedbackData();
        loadEmployeeComboBox();
    }
        private void initializeUI() {
        setTitle("Customer Feedback - " + customerUser.getName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        // Create main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 248, 255));

        // Create title label
        JLabel titleLabel = new JLabel("Customer Feedback", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 51, 102));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Create center panel with tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));

        // Tab 1: View My Feedback
        JPanel viewFeedbackPanel = createViewFeedbackPanel();
        tabbedPane.addTab("View My Feedback", viewFeedbackPanel);

        // Tab 2: Give New Feedback
        JPanel giveFeedbackPanel = createGiveFeedbackPanel();
        tabbedPane.addTab("Give New Feedback", giveFeedbackPanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(240, 248, 255));

        JButton refreshButton = createStyledButton("Refresh");
        JButton backButton = createStyledButton("Back to Main");

        refreshButton.addActionListener(new RefreshButtonListener());
        backButton.addActionListener(new BackButtonListener());

        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);
    }

    private JPanel createViewFeedbackPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(240, 248, 255));

        // Create table model and table
        String[] columnNames = CustomerFeedback.getColumnNames();
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        feedbackTable = new JTable(tableModel);
        feedbackTable.setFont(new Font("Arial", Font.PLAIN, 12));
        feedbackTable.setRowHeight(20);
        feedbackTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        feedbackTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(feedbackTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Create edit panel
        JPanel editPanel = new JPanel(new BorderLayout(10, 10));
        editPanel.setBorder(BorderFactory.createTitledBorder("Edit Selected Feedback"));
        editPanel.setBackground(new Color(240, 248, 255));

        JTextArea editTextArea = new JTextArea(3, 40);
        editTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
        editTextArea.setLineWrap(true);
        editTextArea.setWrapStyleWord(true);

        JButton editButton = createStyledButton("Update Feedback");
        editButton.addActionListener(e -> {
            int selectedRow = feedbackTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a feedback to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String newContent = editTextArea.getText().trim();
            if (newContent.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Feedback content cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String feedbackId = (String) tableModel.getValueAt(selectedRow, 0);
            try {
                CustomerFeedback feedback = CustomerFeedback.getById(feedbackId);
                feedback.setContent(newContent);
                JOptionPane.showMessageDialog(this, "Feedback updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadFeedbackData();
                editTextArea.setText("");
            } catch (HeadlessException ex) {
                JOptionPane.showMessageDialog(this, "Error updating feedback: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel editButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        editButtonPanel.setBackground(new Color(240, 248, 255));
        editButtonPanel.add(editButton);

        editPanel.add(new JScrollPane(editTextArea), BorderLayout.CENTER);
        editPanel.add(editButtonPanel, BorderLayout.SOUTH);

        panel.add(editPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createGiveFeedbackPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(240, 248, 255));

        // Create form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Employee selection
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Select Employee:"), gbc);
        
        employeeComboBox = new JComboBox<>();
        employeeComboBox.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1; gbc.gridy = 0;
        formPanel.add(employeeComboBox, gbc);

        // Feedback content
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Feedback Content:"), gbc);
        
        feedbackTextArea = new JTextArea(5, 40);
        feedbackTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
        feedbackTextArea.setLineWrap(true);
        feedbackTextArea.setWrapStyleWord(true);
        gbc.gridx = 1; gbc.gridy = 1;
        formPanel.add(new JScrollPane(feedbackTextArea), gbc);

        panel.add(formPanel, BorderLayout.CENTER);

        // Submit button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(240, 248, 255));
        
        JButton submitButton = createStyledButton("Submit Feedback");
        submitButton.addActionListener(new SubmitFeedbackListener());
        buttonPanel.add(submitButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void loadFeedbackData() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Get feedback records from customer
        List<List<String>> feedbackRecords = customerUser.getMyCustomerFeedbackRecords();
        
        if (feedbackRecords.isEmpty()) {
            // Show message in dialog instead of adding empty row
            JOptionPane.showMessageDialog(this, 
                "No feedback found.", "Information", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Add data to table
            for (List<String> record : feedbackRecords) {
                tableModel.addRow(record.toArray());
            }
        }
    }

    private void loadEmployeeComboBox() {
        employeeComboBox.removeAllItems();
        
        // Get non-manager employees
        List<List<String>> employeeRecords = customerUser.getNonManagerEmployeeRecords();
        
        if (employeeRecords.isEmpty()) {
            employeeComboBox.addItem("No employees available");
        } else {
            for (List<String> record : employeeRecords) {
                String employeeId = record.get(0);
                String employeeName = record.size() > 1 ? record.get(1) : "Unknown";
                employeeComboBox.addItem(employeeId + " - " + employeeName);
            }
        }
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private class SubmitFeedbackListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedItem = (String) employeeComboBox.getSelectedItem();
            if (selectedItem == null || selectedItem.equals("No employees available")) {
                JOptionPane.showMessageDialog(CustomerFeedbackFrame.this, 
                    "No employee selected or no employees available.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String content = feedbackTextArea.getText().trim();
            if (content.isEmpty()) {
                JOptionPane.showMessageDialog(CustomerFeedbackFrame.this, 
                    "Please enter feedback content.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Extract employee ID from the combo box item
            String employeeId = selectedItem.split(" - ")[0];

            try {
                customerUser.provideFeedback(employeeId, content);
                JOptionPane.showMessageDialog(CustomerFeedbackFrame.this, 
                    "Feedback submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Clear form
                feedbackTextArea.setText("");
                loadFeedbackData(); // Refresh the feedback list
            } catch (HeadlessException ex) {
                JOptionPane.showMessageDialog(CustomerFeedbackFrame.this, 
                    "Error submitting feedback: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class RefreshButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            loadFeedbackData();
            loadEmployeeComboBox();
            JOptionPane.showMessageDialog(CustomerFeedbackFrame.this, 
                "Data refreshed.", "Information", JOptionPane.INFORMATION_MESSAGE);
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
            .addGap(0, 573, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 388, Short.MAX_VALUE)
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
