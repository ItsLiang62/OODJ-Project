/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui.customer;

import gui.manager.LoginPage;
import user.Customer;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 *
 * @author Adrian Liew Ren Qian
 */
public class CustomerMainPage extends javax.swing.JFrame {
    private final Customer customerUser;

    /**
     * Creates new form CustomerMainPage
     * @param customerUser
     */
    public CustomerMainPage(Customer customerUser) {
        this.customerUser = customerUser;
        initComponents();
        customizeUI();
        setVisible(true);
        setSize(800, 600);
        setLocationRelativeTo(null); 
        setVisible(true);
    }
    
    private void customizeUI() {
        jLabel1.setText(String.format("Welcome to Customer Main Page, %s!", customerUser.getName()));
        jLabel2.setText(String.format("Wallet Balance:$%.2f", customerUser.getApWallet()));
        jButton1.addActionListener(new ViewAppointmentsButtonListener());
        jButton2.addActionListener(new ViewPrescriptionsButtonListener());
        jButton3.addActionListener(new ViewInvoicesButtonListener());
        jButton4.addActionListener(new GiveFeedbackButtonListener());
        jButton6.addActionListener(new EditProfileButtonListener());
        jButton5.addActionListener(new TopUpWalletButtonListener());
        jButton8.addActionListener(new BackButtonListener());
        

    }

    
    private class ViewAppointmentsButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> {
                CustomerAppointmentsFrame appointmentsFrame = new CustomerAppointmentsFrame(customerUser);
                appointmentsFrame.setVisible(true);
            });
            dispose();
        }
    }

    private class ViewPrescriptionsButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> {
                CustomerPrescriptionsFrame prescriptionsFrame = new CustomerPrescriptionsFrame(customerUser);
                prescriptionsFrame.setVisible(true);
            });
            dispose();
        }
    }

    private class ViewInvoicesButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> {
                CustomerInvoicesFrame invoicesFrame = new CustomerInvoicesFrame(customerUser);
                invoicesFrame.setVisible(true);
            });
            dispose();
        }
    }

    private class GiveFeedbackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> {
                CustomerFeedbackFrame feedbackFrame = new CustomerFeedbackFrame(customerUser);
                feedbackFrame.setVisible(true);
            });
            dispose();
        }
    }

    private class EditProfileButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> {
                CustomerEditProfileFrame editProfileFrame = new CustomerEditProfileFrame(customerUser);
                editProfileFrame.setVisible(true);
            });
            dispose();
        }
    }

    private class TopUpWalletButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> {
                TopUpWalletFrame topUpFrame = new TopUpWalletFrame(customerUser, CustomerMainPage.this);
            
                // Add window listener to refresh when the top-up frame closes
                topUpFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                        refreshWalletBalance();
                    }
                });
            
                topUpFrame.setVisible(true);
            });
        }
    }

    public void refreshWalletBalance() {
        jLabel2.setText(String.format("Wallet Balance: $%.2f", customerUser.getApWallet()));
    }

    private class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int result = JOptionPane.showConfirmDialog(
                CustomerMainPage.this, 
                "Are you sure you want to logout?", 
                "Confirm Logout", 
                JOptionPane.YES_NO_OPTION
            );
            
            if (result == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(CustomerMainPage.this, "Logged out successfully!");
                SwingUtilities.invokeLater(() -> {
                    new LoginPage().setVisible(true);
                });
                dispose();
            }
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

        jPanel7 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("APU Medical Centre - Customer Dashboard");
        setBackground(new java.awt.Color(240, 248, 255));
        setName("headerPanel"); // NOI18N
        setPreferredSize(new java.awt.Dimension(800, 600));
        setSize(new java.awt.Dimension(800, 600));

        jPanel7.setBackground(new java.awt.Color(240, 248, 255));
        jPanel7.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel7.setName("buttonPanel"); // NOI18N

        jButton1.setBackground(new java.awt.Color(70, 130, 180));
        jButton1.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("My Appointments");
        jButton1.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 15, 12, 15));
        jButton1.setFocusPainted(false);
        jButton1.setName("appointmentsBtn"); // NOI18N

        jButton2.setBackground(new java.awt.Color(70, 130, 180));
        jButton2.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("My Prescriptions");
        jButton2.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 15, 12, 15));
        jButton2.setFocusPainted(false);
        jButton2.setName("prescriptionsBtn"); // NOI18N

        jButton3.setBackground(new java.awt.Color(70, 130, 180));
        jButton3.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("My Invoices");
        jButton3.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 15, 12, 15));
        jButton3.setFocusPainted(false);
        jButton3.setName("invoicesBtn"); // NOI18N

        jButton4.setBackground(new java.awt.Color(70, 130, 180));
        jButton4.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Provide Feedback");
        jButton4.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 15, 12, 15));
        jButton4.setFocusPainted(false);
        jButton4.setName("feedbackBtn"); // NOI18N

        jButton5.setBackground(new java.awt.Color(70, 130, 180));
        jButton5.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Top Up Wallet");
        jButton5.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 15, 12, 15));
        jButton5.setFocusPainted(false);
        jButton5.setName("topUpBtn"); // NOI18N

        jButton6.setBackground(new java.awt.Color(70, 130, 180));
        jButton6.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Edit Profile");
        jButton6.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 15, 12, 15));
        jButton6.setFocusPainted(false);
        jButton6.setName("editProfileBtn"); // NOI18N

        jButton8.setBackground(new java.awt.Color(70, 130, 180));
        jButton8.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setText("Logout");
        jButton8.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 15, 12, 15));
        jButton8.setFocusPainted(false);
        jButton8.setName("logoutBtn"); // NOI18N
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jLabel1.setBackground(new java.awt.Color(0, 51, 102));
        jLabel1.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("\"Welcome, \" + customer.getName() + \"!\"");
        jLabel1.setName("welcomeLabel"); // NOI18N
        jLabel1.setVerifyInputWhenFocusTarget(false);

        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 100, 0));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Wallet Balance: $150.75");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(139, 139, 139)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jButton4)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton3)
                            .addComponent(jButton2)
                            .addComponent(jButton1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 94, Short.MAX_VALUE)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton6)
                            .addComponent(jButton5)
                            .addComponent(jButton8))
                        .addGap(46, 46, 46))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton5))
                .addGap(53, 53, 53)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton6))
                .addGap(53, 53, 53)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton8))
                .addGap(55, 55, 55)
                .addComponent(jButton4)
                .addGap(51, 51, 51))
        );

        jButton1.getAccessibleContext().setAccessibleName("appointmentsBtn");
        jLabel2.getAccessibleContext().setAccessibleParent(jLabel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel7.getAccessibleContext().setAccessibleParent(jPanel7);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed

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
            
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel7;
    // End of variables declaration//GEN-END:variables
}
