/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui.customer;
import user.Customer;
import operation.Invoice;

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
public class CustomerInvoicesFrame extends javax.swing.JFrame {
    
    private final Customer customerUser;
    private JTable invoicesTable;
    private DefaultTableModel tableModel;
    /**
     * Creates new form CustomerInvoicesFrame
     * @param customerUser
     */
    public CustomerInvoicesFrame(Customer customerUser) {
        this.customerUser = customerUser;
        initializeUI();
        loadInvoicesData();
    }
    private void initializeUI() {
        setTitle("My Invoices - " + customerUser.getName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);

        // Create main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 248, 255));

        // Create title label
        JLabel titleLabel = new JLabel("My Invoices", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 51, 102));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Create table model and table
        String[] columnNames = Invoice.getColumnNames();
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex ==3) {
                    return Double.class; // Amount columns
                }
                return String.class; // All other columns
            }
        };

        invoicesTable = new JTable(tableModel);
        invoicesTable.setFont(new Font("Arial", Font.PLAIN, 12));
        invoicesTable.setRowHeight(25);
        invoicesTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        invoicesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(invoicesTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(240, 248, 255));

        JButton viewDetailsButton = createStyledButton("View Details");
        JButton refreshButton = createStyledButton("Refresh");
        JButton backButton = createStyledButton("Back to Main");

        viewDetailsButton.addActionListener(new ViewDetailsButtonListener());
        refreshButton.addActionListener(new RefreshButtonListener());
        backButton.addActionListener(new BackButtonListener());

        buttonPanel.add(viewDetailsButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);
    }

    private void loadInvoicesData() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Get invoice records from customer
        List<List<String>> invoiceRecords = customerUser.getMyInvoiceRecords();
        
        if (invoiceRecords.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No invoices found. Invoices will be generated after appointments.", 
                "Information", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Add data to table and calculate total amount
            for (List<String> record : invoiceRecords) {
                tableModel.addRow(record.toArray());
            }   
            // Show success message with count
            JOptionPane.showMessageDialog(this, 
                "Loaded " + invoiceRecords.size() + " invoice(s).", 
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

    private class ViewDetailsButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = invoicesTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(CustomerInvoicesFrame.this, 
                    "Please select an invoice to view details.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Get invoice details from selected row
            String invoiceId = (String) tableModel.getValueAt(selectedRow, 0);
            String appointmentId = (String) tableModel.getValueAt(selectedRow, 1);
            String creationDate = (String) tableModel.getValueAt(selectedRow, 2);
            String totalAmount = (String) tableModel.getValueAt(selectedRow, 3);

            // Create detailed message
            String message = String.format(
                "Invoice Details:\n\n" +
                "Invoice ID: %s\n" +
                "Appointment ID: %s\n" +
                "Creation Date: %s\n" +
                "Total Amount: $%s",
                invoiceId, appointmentId, creationDate, totalAmount
            );

            JOptionPane.showMessageDialog(CustomerInvoicesFrame.this, 
                message, "Invoice Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private class RefreshButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            loadInvoicesData();
            JOptionPane.showMessageDialog(CustomerInvoicesFrame.this, 
                "Invoices list refreshed.", "Information", JOptionPane.INFORMATION_MESSAGE);
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
        setName("CustomerInvoicesFrame"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 654, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 381, Short.MAX_VALUE)
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
