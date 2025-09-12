/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui.customer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Set;
import java.util.List;
/**
 *
 * @author Adrian Liew Ren Qian
 */
public class CustomerInvoicesFrame extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(CustomerInvoicesFrame.class.getName());
    private Set<List<String>> invoiceRecords;
    /**
     * Creates new form CustomerInvoicesFrame
     */
    public CustomerInvoicesFrame(Set<List<String>> invoiceRecords) {
        this.invoiceRecords = invoiceRecords;
        initComponents();
        setupFrame();
        loadInvoicesTable();
        createSummaryPanel();
        setupActionListeners();
    }
    private void setupFrame() {
        setTitle("My Invoices");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    private void setupActionListeners() {
        jButton1.addActionListener(e -> refreshInvoices());
        jButton2.addActionListener(e -> viewDetails());
        jButton3.addActionListener(e -> exportInvoices());
    }

    private void loadInvoicesTable() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Clear existing data

        for (List<String> record : invoiceRecords) {
            // Convert string values to appropriate types
            Object[] rowData = new Object[8];
            for (int i = 0; i < record.size() && i < 8; i++) {
                if (i >= 4 && i <= 6) { // Currency columns
                    try {
                        rowData[i] = Double.parseDouble(record.get(i));
                    } catch (NumberFormatException e) {
                        rowData[i] = 0.0;
                    }
                } else {
                    rowData[i] = record.get(i);
                }
            }
            model.addRow(rowData);
        }

        styleTable();
        
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No invoices found.");
        }
    }

    private void styleTable() {
        jTable1.setRowHeight(25);
        jTable1.getTableHeader().setReorderingAllowed(false);
        
        // Set column widths
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(100); // Invoice ID
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(100); // Date
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(120); // Appointment ID
        jTable1.getColumnModel().getColumn(3).setPreferredWidth(100); // Status
        jTable1.getColumnModel().getColumn(4).setPreferredWidth(120); // Consultation Fee
        jTable1.getColumnModel().getColumn(5).setPreferredWidth(120); // Medicine Charges
        jTable1.getColumnModel().getColumn(6).setPreferredWidth(120); // Total Amount
        jTable1.getColumnModel().getColumn(7).setPreferredWidth(100); // Payment Method
        
        // Add color coding for status
        jTable1.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // Color code by status (column 3)
                String status = (String) table.getValueAt(row, 3);
                if (status != null && column == 3) {
                    switch (status.toLowerCase()) {
                        case "paid":
                            c.setForeground(new java.awt.Color(0, 128, 0)); // Green
                            break;
                        case "pending":
                            c.setForeground(new java.awt.Color(255, 165, 0)); // Orange
                            break;
                        case "overdue":
                            c.setForeground(new java.awt.Color(255, 0, 0)); // Red
                            break;
                    }
                }
                
                // Format currency columns
                if (value instanceof Double && column >= 4 && column <= 6) {
                    ((javax.swing.JLabel) c).setText(String.format("RM %.2f", (Double) value));
                }
                
                return c;
            }
        });
    }

    private void createSummaryPanel() {
        double totalPaid = 0;
        double totalPending = 0;
        double totalOverdue = 0;
        int totalInvoices = invoiceRecords.size();

        for (List<String> record : invoiceRecords) {
            if (record.size() >= 7) {
                try {
                    double amount = Double.parseDouble(record.get(6)); // Total amount
                    String status = record.get(3).toLowerCase();
                    
                    switch (status) {
                        case "paid":
                            totalPaid += amount;
                            break;
                        case "pending":
                            totalPending += amount;
                            break;
                        case "overdue":
                            totalOverdue += amount;
                            break;
                    }
                } catch (NumberFormatException e) {
                    // Skip invalid amounts
                }
            }
        }

        // Update summary labels
        jLabel1.setText("<html><center><b>Total Invoices</b><br>" + totalInvoices + "</center></html>");
        jLabel2.setText("<html><center><b>Total Paid</b><br>RM " + String.format("%.2f", totalPaid) + "</center></html>");
        jLabel3.setText("<html><center><b>Pending Payment</b><br>RM " + String.format("%.2f", totalPending) + "</center></html>");
        jLabel4.setText("<html><center><b>Overdue</b><br>RM " + String.format("%.2f", totalOverdue) + "</center></html>");
    }

    private void refreshInvoices() {
        try {
            // This would reload from database in real application
            loadInvoicesTable();
            createSummaryPanel();
            JOptionPane.showMessageDialog(this, "Invoices list refreshed.");
        } catch (Exception ex) {
            showError("Error refreshing invoices: " + ex.getMessage());
        }
    }

    private void viewDetails() {
        int selectedRow = jTable1.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select an invoice to view details.",
                "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get selected invoice data
        String invoiceId = (String) jTable1.getValueAt(selectedRow, 0);
        String date = (String) jTable1.getValueAt(selectedRow, 1);
        String appointmentId = (String) jTable1.getValueAt(selectedRow, 2);
        String status = (String) jTable1.getValueAt(selectedRow, 3);
        double consultationFee = (Double) jTable1.getValueAt(selectedRow, 4);
        double medicineCharges = (Double) jTable1.getValueAt(selectedRow, 5);
        double totalAmount = (Double) jTable1.getValueAt(selectedRow, 6);
        String paymentMethod = (String) jTable1.getValueAt(selectedRow, 7);

        // Create detailed view
        String message = String.format(
            "INVOICE DETAILS\n\n" +
            "Invoice ID: %s\n" +
            "Date: %s\n" +
            "Appointment ID: %s\n" +
            "Status: %s\n\n" +
            "BREAKDOWN:\n" +
            "Consultation Fee: RM %.2f\n" +
            "Medicine Charges: RM %.2f\n" +
            "-----------------------------\n" +
            "TOTAL AMOUNT: RM %.2f\n\n" +
            "Payment Method: %s",
            invoiceId, date, appointmentId, status, 
            consultationFee, medicineCharges, totalAmount, paymentMethod
        );

        JOptionPane.showMessageDialog(this,
            message,
            "Invoice Details - " + invoiceId,
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void exportInvoices() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Invoices");
        fileChooser.setSelectedFile(new java.io.File("apu_medical_invoices.csv"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            
            try (java.io.PrintWriter writer = new java.io.PrintWriter(fileToSave)) {
                // Write CSV header
                writer.println("Invoice ID,Date,Appointment ID,Status,Consultation Fee,Medicine Charges,Total Amount,Payment Method");
                
                // Write data
                for (List<String> record : invoiceRecords) {
                    writer.println(String.join(",", record));
                }
                
                JOptionPane.showMessageDialog(this,
                    "Invoices exported successfully to: " + fileToSave.getName(),
                    "Export Successful", JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (Exception ex) {
                showError("Error exporting invoices: " + ex.getMessage());
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("CustomerInvoicesFrame"); // NOI18N

        jScrollPane1.setName("scrollPane"); // NOI18N

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.setName("invoicesTable"); // NOI18N
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jTable1);

        jPanel1.setName("summaryPanel"); // NOI18N

        jLabel1.setText("jLabel1");

        jLabel2.setText("jLabel2");

        jLabel3.setText("jLabel3");

        jLabel4.setText("jLabel4");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(139, 139, 139)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(113, 113, 113)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 113, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addContainerGap(59, Short.MAX_VALUE))
        );

        jPanel2.setName("buttonPanel"); // NOI18N

        jButton1.setText("Refresh");

        jButton2.setText("View Details");

        jButton3.setText("Export");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(99, 99, 99)
                .addComponent(jButton1)
                .addGap(79, 79, 79)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 83, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addGap(52, 52, 52))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap(41, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane1)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(69, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new CustomerInvoicesFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
