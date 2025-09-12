/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui.customer;
import user.Customer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Set;
import java.util.List;
/**
 *
 * @author Adrian Liew Ren Qian
 */
public class CustomerHistoryFrame extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(CustomerHistoryFrame.class.getName());
    private Customer currentCustomer;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTextField jTextField1;
    /**
     * Creates new form CustomerHistoryFrame
     */
    public CustomerHistoryFrame(Customer customer) {
        this.currentCustomer = customer;
        initComponents();
        setupFrame();
        loadAllHistoryData();
        setupActionListeners();
    }
    private void setupFrame() {
        setTitle("My Medical History");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Search:");

        jLabel2.setText("in:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {
            "All History", "Appointments", "Prescriptions", "Invoices", "Feedback"
        }));

        // Appointments Table
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "Appointment ID", "Date", "Doctor", "Service", "Status", "Charges (RM)"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, 
                java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        // Prescriptions Table
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "Prescription ID", "Date", "Medicine", "Dosage", "Frequency", "Duration", "Doctor"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, 
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable2);

        // Invoices Table
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "Invoice ID", "Date", "Amount (RM)", "Status", "Payment Method", "Appointment ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Double.class, 
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTable3);

        // Feedback Table
        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "Date", "Staff/Doctor", "Role", "Rating", "Comments"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, 
                java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(jTable4);

        jTabbedPane1.addTab("Appointments", jScrollPane1);
        jTabbedPane1.addTab("Prescriptions", jScrollPane2);
        jTabbedPane1.addTab("Invoices", jScrollPane3);
        jTabbedPane1.addTab("Feedback", jScrollPane4);

        jButton1.setText("Export History");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }

    private void setupActionListeners() {
        jButton1.addActionListener(e -> exportHistory());
        jTextField1.addActionListener(e -> performSearch());
        jComboBox1.addActionListener(e -> performSearch());
    }

    private void loadAllHistoryData() {
        try {
            // Load appointments
            Set<List<String>> appointments = currentCustomer.getAllMyAppointmentRecords();
            loadTableData(jTable1, appointments);
            
            // Load prescriptions
            Set<List<String>> prescriptions = currentCustomer.getAllMyPrescriptionRecords();
            loadTableData(jTable2, prescriptions);
            
            // Load invoices
            Set<List<String>> invoices = currentCustomer.getAllMyInvoiceRecords();
            loadTableData(jTable3, invoices);
            
            // Load feedback
            Set<List<String>> feedbacks = currentCustomer.getAllMyCustomerFeedbackRecords();
            loadTableData(jTable4, feedbacks);
            
            // Style all tables
            styleAllTables();
            
        } catch (Exception ex) {
            showError("Error loading history data: " + ex.getMessage());
        }
    }

    private void loadTableData(JTable table, Set<List<String>> records) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        
        for (List<String> record : records) {
            model.addRow(record.toArray());
        }
    }

    private void styleAllTables() {
        styleTable(jTable1, new int[]{100, 100, 150, 150, 100, 120});
        styleTable(jTable2, new int[]{120, 100, 150, 80, 100, 80, 150});
        styleTable(jTable3, new int[]{100, 100, 120, 100, 120, 120});
        styleTable(jTable4, new int[]{100, 150, 100, 80, 200});
        
        // Add color coding for invoice status
        jTable3.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // Color code by status (column 3)
                if (column == 3) {
                    String status = (String) table.getValueAt(row, 3);
                    if (status != null) {
                        switch (status.toLowerCase()) {
                            case "paid":
                                c.setForeground(new java.awt.Color(0, 128, 0));
                                break;
                            case "pending":
                                c.setForeground(new java.awt.Color(255, 165, 0));
                                break;
                            case "overdue":
                                c.setForeground(new java.awt.Color(255, 0, 0));
                                break;
                        }
                    }
                }
                
                // Format currency columns
                if (value instanceof Double && column == 2) {
                    ((javax.swing.JLabel) c).setText(String.format("RM %.2f", (Double) value));
                }
                
                return c;
            }
        });
    }

    private void styleTable(JTable table, int[] columnWidths) {
        table.setRowHeight(25);
        table.getTableHeader().setReorderingAllowed(false);
        
        for (int i = 0; i < columnWidths.length && i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }
    }

    private void performSearch() {
        String searchText = jTextField1.getText().toLowerCase().trim();
        String searchType = (String) jComboBox1.getSelectedItem();
        
        if (searchText.isEmpty()) {
            // Reset all tables
            loadAllHistoryData();
            return;
        }
        
        // Simple search implementation - would need proper RowFilter in production
        JTable currentTable = getCurrentTable();
        if (currentTable != null) {
            DefaultTableModel model = (DefaultTableModel) currentTable.getModel();
            for (int i = 0; i < model.getRowCount(); i++) {
                boolean match = false;
                for (int j = 0; j < model.getColumnCount(); j++) {
                    Object value = model.getValueAt(i, j);
                    if (value != null && value.toString().toLowerCase().contains(searchText)) {
                        match = true;
                        break;
                    }
                }
                // Simple search highlight - in production, use RowFilter
            }
        }
    }

    private JTable getCurrentTable() {
        int selectedIndex = jTabbedPane1.getSelectedIndex();
        switch (selectedIndex) {
            case 0: return jTable1;
            case 1: return jTable2;
            case 2: return jTable3;
            case 3: return jTable4;
            default: return null;
        }
    }

    private void exportHistory() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Medical History");
        fileChooser.setSelectedFile(new java.io.File("apu_medical_history.pdf"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File fileToSave = fileChooser.getSelectedFile();
                String fileName = fileToSave.getName().toLowerCase();
                
                if (fileName.endsWith(".csv")) {
                    exportToCSV(fileToSave);
                } else if (fileName.endsWith(".txt")) {
                    exportToTXT(fileToSave);
                } else {
                    // Default to PDF
                    exportToPDF(fileToSave);
                }
                
                JOptionPane.showMessageDialog(this,
                    "Medical history exported successfully!",
                    "Export Successful", JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (Exception ex) {
                showError("Error exporting history: " + ex.getMessage());
            }
        }
    }

    private void exportToCSV(java.io.File file) {
        try (java.io.PrintWriter writer = new java.io.PrintWriter(file)) {
            JTable currentTable = getCurrentTable();
            DefaultTableModel model = (DefaultTableModel) currentTable.getModel();
            
            // Write header
            for (int i = 0; i < model.getColumnCount(); i++) {
                writer.print(model.getColumnName(i));
                if (i < model.getColumnCount() - 1) {
                    writer.print(",");
                }
            }
            writer.println();
            
            // Write data
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    Object value = model.getValueAt(i, j);
                    writer.print(value != null ? value.toString() : "");
                    if (j < model.getColumnCount() - 1) {
                        writer.print(",");
                    }
                }
                writer.println();
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error exporting to CSV", ex);
        }
    }

    private void exportToTXT(java.io.File file) {
        try (java.io.PrintWriter writer = new java.io.PrintWriter(file)) {
            JTable currentTable = getCurrentTable();
            DefaultTableModel model = (DefaultTableModel) currentTable.getModel();
            
            writer.println("APU MEDICAL CENTRE - MEDICAL HISTORY");
            writer.println("====================================");
            writer.println("Export Date: " + new java.util.Date());
            writer.println("Tab: " + jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()));
            writer.println("====================================\n");
            
            // Write header
            for (int i = 0; i < model.getColumnCount(); i++) {
                writer.printf("%-20s", model.getColumnName(i));
            }
            writer.println();
            
            // Write separator
            for (int i = 0; i < model.getColumnCount(); i++) {
                writer.printf("%-20s", "--------------------");
            }
            writer.println();
            
            // Write data
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    Object value = model.getValueAt(i, j);
                    writer.printf("%-20s", value != null ? value.toString() : "");
                }
                writer.println();
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error exporting to TXT", ex);
        }
    }

    private void exportToPDF(java.io.File file) {
        // Placeholder for PDF export - would require external libraries like iText
        JOptionPane.showMessageDialog(this,
            "PDF export would require additional libraries.\n" +
            "Exported as CSV instead.",
            "Export Notice", JOptionPane.INFORMATION_MESSAGE);
        exportToCSV(file);
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

        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        jScrollPane4 = new javax.swing.JScrollPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setName("historyTabbedPane"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 287, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 534, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(66, 66, 66)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(16, 16, 16)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 540, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 525, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(75, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        java.awt.EventQueue.invokeLater(() -> new CustomerHistoryFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    // End of variables declaration//GEN-END:variables
}
