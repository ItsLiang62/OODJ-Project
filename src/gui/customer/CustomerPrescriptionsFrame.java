package gui.customer;

import user.Customer;
import operation.AppointmentMedicine;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CustomerPrescriptionsFrame extends javax.swing.JFrame {
    
    private final Customer customerUser;
    private JTable prescriptionsTable;
    private DefaultTableModel tableModel;

    public CustomerPrescriptionsFrame(Customer customerUser) {
        this.customerUser = customerUser;
        initializeUI();
        loadPrescriptionsData();
    }
        private void initializeUI() {
        setTitle("My Prescriptions - " + customerUser.getName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 600);
        setLocationRelativeTo(null);

        // Create main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 248, 255));

        // Create title label
        JLabel titleLabel = new JLabel("My Prescriptions", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 51, 102));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Create info panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        infoPanel.setBackground(new Color(240, 248, 255));
        
        JLabel infoLabel = new JLabel("View all your prescription medications prescribed by doctors");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        infoLabel.setForeground(new Color(85, 107, 47)); // Dark olive green
        infoPanel.add(infoLabel);
        
        mainPanel.add(infoPanel, BorderLayout.CENTER);

        // Create table model and table
        String[] columnNames = AppointmentMedicine.getColumnNames();
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        prescriptionsTable = new JTable(tableModel);
        prescriptionsTable.setFont(new Font("Arial", Font.PLAIN, 12));
        prescriptionsTable.setRowHeight(25);
        prescriptionsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        prescriptionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Set column widths
        prescriptionsTable.getColumnModel().getColumn(0).setPreferredWidth(100); // Appointment ID
        prescriptionsTable.getColumnModel().getColumn(1).setPreferredWidth(100); // Medicine ID
        prescriptionsTable.getColumnModel().getColumn(2).setPreferredWidth(200); // Target Symptom

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(prescriptionsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

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

    private void loadPrescriptionsData() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Get prescription records from customer
        List<List<String>> prescriptionRecords = customerUser.getMyPrescriptionRecords();
        
        if (prescriptionRecords.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No prescribed medications found.", 
                "Information", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Add data to table
            for (List<String> record : prescriptionRecords) {
                tableModel.addRow(record.toArray());
            }
            
            // Show success message
            JOptionPane.showMessageDialog(this, 
                "Loaded " + prescriptionRecords.size() + " prescription(s).", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
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

    private class RefreshButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            loadPrescriptionsData();
            JOptionPane.showMessageDialog(CustomerPrescriptionsFrame.this, 
                "Prescriptions list refreshed.", "Information", JOptionPane.INFORMATION_MESSAGE);
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
        setName("CustomerPrescriptionsFrame"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 762, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
