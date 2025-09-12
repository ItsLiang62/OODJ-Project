/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui.customer;
import user.Customer;
import operation.Appointment;
import database.Database;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Set;
import java.util.List;
/**
 *
 * @author Adrian Liew Ren Qian
 */
public class CustomerAppointmentsFrame extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(CustomerAppointmentsFrame.class.getName());
    private Customer currentCustomer;
    private Set<List<String>> appointmentRecords;
    /**
     * Creates new form CustomerAppointmentsFrame
     */
    public CustomerAppointmentsFrame(Customer customer, Set<List<String>> appointmentRecords) {
        this.currentCustomer = customer;
        this.appointmentRecords = appointmentRecords;
        initComponents();
        setupFrame();
        loadAppointmentsTable();
        setupActionListeners();
    }
    private void setupFrame() {
        setTitle("My Appointments");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "Appointment ID", "Date", "Doctor", "Service", "Status", "Total Charge (RM)"
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

            private void setupActionListeners() {
        jButton1.addActionListener(e -> refreshAppointments());
        jButton2.addActionListener(e -> payForSelectedAppointment());
    }

    private void loadAppointmentsTable() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Clear existing data

        for (List<String> record : appointmentRecords) {
            // Assuming record structure: [appointmentId, date, doctor, service, status, charge]
            model.addRow(record.toArray());
        }

        styleTable();
        
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No appointments found.");
        }
    }

    private void styleTable() {
        jTable1.setRowHeight(25);
        jTable1.getTableHeader().setReorderingAllowed(false);
        
        // Set column widths
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(120); // Appointment ID
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(100); // Date
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(150); // Doctor
        jTable1.getColumnModel().getColumn(3).setPreferredWidth(150); // Service
        jTable1.getColumnModel().getColumn(4).setPreferredWidth(100); // Status
        jTable1.getColumnModel().getColumn(5).setPreferredWidth(120); // Charge
    }

    private void refreshAppointments() {
        try {
            Set<List<String>> updatedRecords = currentCustomer.getAllMyAppointmentRecords();
            this.appointmentRecords = updatedRecords;
            loadAppointmentsTable();
            JOptionPane.showMessageDialog(this, "Appointments list refreshed.");
        } catch (Exception ex) {
            showError("Error refreshing appointments: " + ex.getMessage());
        }
    }

    private void payForSelectedAppointment() {
        int selectedRow = jTable1.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select an appointment to pay.", 
                "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String appointmentId = (String) jTable1.getValueAt(selectedRow, 0);
        String status = (String) jTable1.getValueAt(selectedRow, 4);
        double amount = (Double) jTable1.getValueAt(selectedRow, 5);

        // Check if already paid/completed
        if ("completed".equalsIgnoreCase(status) || "paid".equalsIgnoreCase(status)) {
            JOptionPane.showMessageDialog(this, 
                "This appointment is already paid and completed.", 
                "Payment Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Confirm payment
        int confirm = JOptionPane.showConfirmDialog(this,
            "Confirm payment of RM " + String.format("%.2f", amount) + " for appointment " + appointmentId + "?",
            "Confirm Payment", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Appointment appointment = Database.getAppointment(appointmentId);
                currentCustomer.payForAppointment(appointment);
                
                JOptionPane.showMessageDialog(this,
                    "Payment successful! RM " + String.format("%.2f", amount) + " deducted from your AP Wallet.",
                    "Payment Successful", JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh the table
                refreshAppointments();
                
            } catch (customExceptions.AppointmentCompletedException ex) {
                showError("Appointment already completed: " + ex.getMessage());
            } catch (customExceptions.InsufficientApWalletException ex) {
                showError("Insufficient funds: " + ex.getMessage());
            } catch (Exception ex) {
                showError("Payment failed: " + ex.getMessage());
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
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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
        jTable1.setName("appointmentsTable"); // NOI18N
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jTable1);

        jPanel1.setName("buttonPanel"); // NOI18N

        jButton1.setText("Refresh");
        jButton1.setName("refreshBtn"); // NOI18N

        jButton2.setText("Pay Selected");
        jButton2.setName("payBtn"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 104, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(68, 68, 68))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(49, Short.MAX_VALUE))
        );

        jLabel1.setText("My Appointments");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 504, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(111, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1)
                .addGap(33, 33, 33)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
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
        java.awt.EventQueue.invokeLater(() -> new CustomerAppointmentsFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
