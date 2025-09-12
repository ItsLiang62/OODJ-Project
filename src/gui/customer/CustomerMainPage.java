/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui.customer;

import gui.manager.LoginPage;
import user.Customer;
import javax.swing.*;
import java.util.Set;
import java.util.List;
/**
 *
 * @author Adrian Liew Ren Qian
 */
public class CustomerMainPage extends javax.swing.JFrame {
    private Customer currentCustomer;

    /**
     * Creates new form CustomerMainPage
     * @param customer
     */
    public CustomerMainPage(Customer customer) {
        this.currentCustomer = customer;
        initComponents();
        setupActionListeners();
        updateWelcomeDisplay();
    }
    
    public CustomerMainPage() {
        initComponents();
        
    }
    
    private void updateWelcomeDisplay() {
        jLabel1.setText("Welcome, " + currentCustomer.getName() + "!");
        jLabel2.setText("AP Wallet: RM " + String.format("%.2f", currentCustomer.getApWallet()));
    }
    
    private void setupActionListeners() {
        // Appointments button
        jButton1.addActionListener(e -> openAppointmentsFrame());
        
        // Prescriptions button
        jButton2.addActionListener(e -> openPrescriptionsFrame());
        
        // Invoices button
        jButton3.addActionListener(e -> openInvoicesFrame());
        
        // Feedback button
        jButton4.addActionListener(e -> openFeedbackFrame());
        
        // Top Up button
        jButton5.addActionListener(e -> openTopUpFrame());
        
        // Edit Profile button
        jButton6.addActionListener(e -> openEditProfileFrame());
        
        // View History button
        jButton7.addActionListener(e -> openHistoryFrame());
        
        // Logout button
        jButton8.addActionListener(e -> logout());
    }
    
    private void openAppointmentsFrame() {
        try {
            Set<List<String>> appointments = currentCustomer.getAllMyAppointmentRecords();
            CustomerAppointmentsFrame appointmentsFrame = 
                new CustomerAppointmentsFrame(currentCustomer, appointments);
            appointmentsFrame.setVisible(true);
        } catch (Exception ex) {
            showError("Error loading appointments: " + ex.getMessage());
        }
    }
    
    private void openPrescriptionsFrame() {
        try {
            Set<List<String>> prescriptions = currentCustomer.getAllMyPrescriptionRecords();
            CustomerPrescriptionsFrame prescriptionsFrame = 
                new CustomerPrescriptionsFrame(prescriptions);
            prescriptionsFrame.setVisible(true);
        } catch (Exception ex) {
            showError("Error loading prescriptions: " + ex.getMessage());
        }
    }

    private void openInvoicesFrame() {
        try {
            Set<List<String>> invoices = currentCustomer.getAllMyInvoiceRecords();
            CustomerInvoicesFrame invoicesFrame = new CustomerInvoicesFrame(invoices);
            invoicesFrame.setVisible(true);
        } catch (Exception ex) {
            showError("Error loading invoices: " + ex.getMessage());
        }
    }

    private void openFeedbackFrame() {
        try {
            CustomerFeedbackFrame feedbackFrame = new CustomerFeedbackFrame(currentCustomer);
            feedbackFrame.setVisible(true);
        } catch (Exception ex) {
            showError("Error opening feedback form: " + ex.getMessage());
        }
    }

    private void openTopUpFrame() {
        try {
            TopUpWalletFrame topUpFrame = new TopUpWalletFrame(currentCustomer, this);
            topUpFrame.setVisible(true);
        } catch (Exception ex) {
            showError("Error opening top up form: " + ex.getMessage());
        }
    }

    private void openEditProfileFrame() {
        try {
            CustomerEditProfileFrame editProfileFrame = 
                new CustomerEditProfileFrame(currentCustomer, this);
            editProfileFrame.setVisible(true);
        } catch (Exception ex) {
            showError("Error opening edit profile form: " + ex.getMessage());
        }
    }

    private void openHistoryFrame() {
        try {
            CustomerHistoryFrame historyFrame = new CustomerHistoryFrame(currentCustomer);
            historyFrame.setVisible(true);
        } catch (Exception ex) {
            showError("Error loading medical history: " + ex.getMessage());
        }
    }
    
    public void updateWalletDisplay() {
        jLabel2.setText("AP Wallet: RM " + 
            String.format("%.2f", currentCustomer.getApWallet()));
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            SwingUtilities.invokeLater(LoginPage::new);
            this.dispose();
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("APU Medical Centre - Customer Dashboard");
        setName("headerPanel"); // NOI18N

        jPanel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("\"Welcome, \" + customer.getName() + \"!\"");
        jLabel1.setName("welcomeLabel"); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("\"AP Wallet: RM \" + String.format(\"%.2f\", customer.getApWallet())");
        jLabel2.setName("walletLabel"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(66, 66, 66)
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setName("buttonPanel"); // NOI18N

        jButton1.setText("My Appointments");
        jButton1.setName("appointmentsBtn"); // NOI18N

        jButton2.setText("My Prescriptions");
        jButton2.setName("prescriptionsBtn"); // NOI18N

        jButton3.setText("My Invoices");
        jButton3.setName("invoicesBtn"); // NOI18N

        jButton4.setText("Provide Feedback");
        jButton4.setName("feedbackBtn"); // NOI18N

        jButton5.setText("Top Up Wallet");
        jButton5.setName("topUpBtn"); // NOI18N

        jButton6.setText("Edit Profile");
        jButton6.setName("editProfileBtn"); // NOI18N

        jButton7.setText("View History");
        jButton7.setName("historyBtn"); // NOI18N

        jButton8.setText("Logout");
        jButton8.setName("logoutBtn"); // NOI18N
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addGap(205, 205, 205)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton8)
                    .addComponent(jButton7)
                    .addComponent(jButton6)
                    .addComponent(jButton5))
                .addContainerGap(236, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton5))
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton6)
                        .addGap(38, 38, 38)))
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton7))
                .addGap(42, 42, 42)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jButton8))
                .addGap(39, 39, 39))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(242, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        logout();
    }//GEN-LAST:event_jButton8ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            // For testing only - remove this in production
            // Customer testCustomer = new Customer("test", "Test User", "test@email.com", "password", 100.0);
            // new CustomerMainPage(testCustomer).setVisible(true);
            
            new CustomerMainPage().setVisible(true); // For NetBeans preview
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel7;
    // End of variables declaration//GEN-END:variables
}
