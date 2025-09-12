/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui.customer;
import user.Customer;
import database.Database;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Set;
import java.util.List;
/**
 *
 * @author Adrian Liew Ren Qian
 */
public class CustomerPrescriptionsFrame extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(CustomerPrescriptionsFrame.class.getName());
    private Set<List<String>> prescriptionRecords;
    /**
     * Creates new form CustomerPrescriptionsFrame
     */
    public CustomerPrescriptionsFrame(Set<List<String>> prescriptionRecords) {
        this.prescriptionRecords = prescriptionRecords;
        initComponents();
        setupFrame();
        loadPrescriptionsTable();
        setupActionListeners();
    }
    private void setupFrame() {
        setTitle("My Prescriptions");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    private void setupActionListeners() {
        jButton1.addActionListener(e -> refreshPrescriptions());
        jButton2.addActionListener(e -> viewDetails());
        jButton3.addActionListener(e -> printPrescription());
        
        // Search functionality
        jTextField1.addActionListener(e -> performSearch());
        jComboBox1.addActionListener(e -> applyFilter());
    }

    private void loadPrescriptionsTable() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Clear existing data

        for (List<String> record : prescriptionRecords) {
            // Assuming record structure matches the table columns
            model.addRow(record.toArray());
        }

        styleTable();
        
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No prescriptions found.");
        }
    }

    private void styleTable() {
        jTable1.setRowHeight(25);
        jTable1.getTableHeader().setReorderingAllowed(false);
        
        // Set column widths
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(120); // Prescription ID
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(120); // Appointment ID
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(150); // Medicine Name
        jTable1.getColumnModel().getColumn(3).setPreferredWidth(80);  // Dosage
        jTable1.getColumnModel().getColumn(4).setPreferredWidth(100); // Frequency
        jTable1.getColumnModel().getColumn(5).setPreferredWidth(80);  // Duration
        jTable1.getColumnModel().getColumn(6).setPreferredWidth(200); // Instructions
    }

    private void refreshPrescriptions() {
        try {
            // This would reload from database in real application
            // For now, we'll just reload the current data
            loadPrescriptionsTable();
            JOptionPane.showMessageDialog(this, "Prescriptions list refreshed.");
        } catch (Exception ex) {
            showError("Error refreshing prescriptions: " + ex.getMessage());
        }
    }

    private void viewDetails() {
        int selectedRow = jTable1.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a prescription to view details.",
                "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get selected prescription data
        String prescriptionId = (String) jTable1.getValueAt(selectedRow, 0);
        String medicineName = (String) jTable1.getValueAt(selectedRow, 2);
        String dosage = (String) jTable1.getValueAt(selectedRow, 3);
        String frequency = (String) jTable1.getValueAt(selectedRow, 4);
        String duration = (String) jTable1.getValueAt(selectedRow, 5);
        String instructions = (String) jTable1.getValueAt(selectedRow, 6);

        // Create detailed message
        String message = String.format(
            "PRESCRIPTION DETAILS\n\n" +
            "Prescription ID: %s\n" +
            "Medicine: %s\n" +
            "Dosage: %s\n" +
            "Frequency: %s\n" +
            "Duration: %s\n\n" +
            "Instructions:\n%s",
            prescriptionId, medicineName, dosage, frequency, duration, instructions
        );

        JOptionPane.showMessageDialog(this,
            message,
            "Prescription Details - " + medicineName,
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void printPrescription() {
        int selectedRow = jTable1.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a prescription to print.",
                "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String prescriptionId = (String) jTable1.getValueAt(selectedRow, 0);
            String medicineName = (String) jTable1.getValueAt(selectedRow, 2);
            
            JTextArea printArea = new JTextArea();
            printArea.setText(generatePrintText(selectedRow));
            printArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
            printArea.setEditable(false);
            
            JOptionPane.showMessageDialog(this,
                new JScrollPane(printArea),
                "Print Preview - " + medicineName,
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception ex) {
            showError("Error generating print preview: " + ex.getMessage());
        }
    }

    private String generatePrintText(int row) {
        return String.format(
            "APU MEDICAL CENTRE\n" +
            "==============================\n" +
            "PRESCRIPTION\n" +
            "==============================\n" +
            "ID: %s\n" +
            "Medicine: %s\n" +
            "Dosage: %s\n" +
            "Frequency: %s\n" +
            "Duration: %s\n" +
            "==============================\n" +
            "Instructions:\n%s\n" +
            "==============================\n" +
            "Please follow the instructions carefully.\n" +
            "Consult your doctor if symptoms persist.\n" +
            "==============================\n",
            jTable1.getValueAt(row, 0),
            jTable1.getValueAt(row, 2),
            jTable1.getValueAt(row, 3),
            jTable1.getValueAt(row, 4),
            jTable1.getValueAt(row, 5),
            jTable1.getValueAt(row, 6)
        );
    }

    private void performSearch() {
        String searchText = jTextField1.getText().toLowerCase().trim();
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        
        if (searchText.isEmpty()) {
            // Reset to show all records
            loadPrescriptionsTable();
            return;
        }
        
        // Simple search implementation
        for (int i = 0; i < model.getRowCount(); i++) {
            boolean match = false;
            for (int j = 0; j < model.getColumnCount(); j++) {
                Object value = model.getValueAt(i, j);
                if (value != null && value.toString().toLowerCase().contains(searchText)) {
                    match = true;
                    break;
                }
            }
            // Hide non-matching rows (simple approach)
            // In a real application, you might want to use RowFilter
        }
    }

    private void applyFilter() {
        String filter = (String) jComboBox1.getSelectedItem();
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        
        if (filter.equals("All Medicines")) {
            // Show all records
            loadPrescriptionsTable();
            return;
        }
        
        // Simple filter implementation
        for (int i = 0; i < model.getRowCount(); i++) {
            String medicineName = ((String) model.getValueAt(i, 2)).toLowerCase();
            boolean show = false;
            
            switch (filter) {
                case "Antibiotics":
                    show = medicineName.contains("antibiotic") || 
                           medicineName.contains("penicillin") ||
                           medicineName.contains("amoxicillin") ||
                           medicineName.contains("cephalexin");
                    break;
                case "Pain Relief":
                    show = medicineName.contains("pain") || 
                           medicineName.contains("analgesic") ||
                           medicineName.contains("ibuprofen") ||
                           medicineName.contains("paracetamol") ||
                           medicineName.contains("aspirin");
                    break;
                case "Chronic Medication":
                    show = medicineName.contains("insulin") || 
                           medicineName.contains("metformin") ||
                           medicineName.contains("blood pressure") ||
                           medicineName.contains("cholesterol");
                    break;
                case "Other":
                    show = !(medicineName.contains("antibiotic") || 
                            medicineName.contains("pain") ||
                            medicineName.contains("insulin"));
                    break;
            }
            
            // Simple approach - in real application use RowFilter
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
        jSeparator1 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("CustomerPrescriptionsFrame"); // NOI18N

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
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jTable1);

        jPanel1.setName("searchPanel"); // NOI18N

        jLabel1.setText("Search:");

        jTextField1.setText("jTextField1");
        jTextField1.setToolTipText("Search by medicine name or instructions");
        jTextField1.setName("searchField"); // NOI18N

        jButton1.setText("Search");
        jButton1.setName("searchBtn"); // NOI18N

        jLabel2.setText("Filter by:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.setName("filterComboBox"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 89, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(151, 151, 151)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );

        jPanel2.setName("buttonPanel"); // NOI18N

        jButton2.setText("Refresh");
        jButton2.setName("refreshBtn"); // NOI18N

        jButton3.setText("View Details");
        jButton3.setName("detailsBtn"); // NOI18N

        jButton4.setText("Print");
        jButton4.setName("printBtn"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(141, 141, 141)
                .addComponent(jButton2)
                .addGap(52, 52, 52)
                .addComponent(jButton3)
                .addGap(27, 27, 27)
                .addComponent(jButton4)
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addContainerGap(52, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 489, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(208, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(143, 143, 143)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        java.awt.EventQueue.invokeLater(() -> new CustomerPrescriptionsFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
