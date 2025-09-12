/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui.customer;
import user.Customer;
import database.Database;
import javax.swing.*;
import java.util.Set;
import java.util.List;
/**
 *
 * @author Adrian Liew Ren Qian
 */
public class CustomerFeedbackFrame extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(CustomerFeedbackFrame.class.getName());
    private Customer currentCustomer;

    /**
     * Creates new form CustomerFeedbackFrame
     */
    public CustomerFeedbackFrame(Customer customer) {
        this.currentCustomer = customer;
        initComponents();
        setupFrame();
        loadEmployeesDropdown();
        setupActionListeners();
    }
    private void setupFrame() {
        setTitle("Provide Feedback");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    private void setupActionListeners() {
        jButton1.addActionListener(e -> submitFeedback());
        jButton2.addActionListener(e -> viewPreviousFeedbacks());
    }    
    private void loadEmployeesDropdown() {
        try {
            Set<List<String>> employeeRecords = currentCustomer.getAllNonManagerEmployeeRecords();
            jComboBox1.removeAllItems();
            jComboBox1.addItem("-- Select Staff/Doctor --");
            
            for (List<String> record : employeeRecords) {
                // Assuming record structure: [id, name, email, role, ...]
                String employeeId = record.get(0);
                String employeeName = record.get(1);
                String employeeRole = record.size() > 3 ? record.get(3) : "Employee";
                
                String displayText = employeeName + " (" + employeeRole + ")";
                jComboBox1.addItem(displayText);
            }
        } catch (Exception ex) {
            showError("Error loading employees: " + ex.getMessage());
        }
    }

    private void submitFeedback() {
        String selectedItem = (String) jComboBox1.getSelectedItem();
        String feedbackText = jTextArea1.getText().trim();
        
        // Validation
        if (selectedItem == null || selectedItem.equals("-- Select Staff/Doctor --")) {
            JOptionPane.showMessageDialog(this,
                "Please select a staff or doctor to provide feedback to.",
                "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (feedbackText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter your feedback message.",
                "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (feedbackText.length() < 10) {
            JOptionPane.showMessageDialog(this,
                "Feedback should be at least 10 characters long.",
                "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Extract employee ID from the selected item
            // Format: "Name (Role)" - we need to get the actual ID from database
            String selectedName = selectedItem.split(" \\(")[0];
            String employeeId = findEmployeeIdByName(selectedName);
            
            if (employeeId == null) {
                showError("Could not find employee ID for selected staff/doctor.");
                return;
            }
            
            // Provide feedback
            currentCustomer.provideFeedbackToNonManagerEmployee(employeeId, feedbackText);
            
            JOptionPane.showMessageDialog(this,
                "Thank you! Your feedback has been submitted successfully.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            
            // Clear form
            jTextArea1.setText("");
            jComboBox1.setSelectedIndex(0);
            
        } catch (Exception ex) {
            showError("Error submitting feedback: " + ex.getMessage());
        }
    }

    private String findEmployeeIdByName(String name) {
        try {
            Set<List<String>> employeeRecords = currentCustomer.getAllNonManagerEmployeeRecords();
            for (List<String> record : employeeRecords) {
                String employeeName = record.get(1);
                if (employeeName.equals(name)) {
                    return record.get(0); // Return employee ID
                }
            }
        } catch (Exception ex) {
            showError("Error finding employee: " + ex.getMessage());
        }
        return null;
    }

    private void viewPreviousFeedbacks() {
        try {
            Set<List<String>> feedbackRecords = currentCustomer.getAllMyCustomerFeedbackRecords();
            
            if (feedbackRecords.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "You haven't submitted any feedback yet.",
                    "My Feedbacks", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Create a dialog to display feedbacks
            JDialog feedbackDialog = new JDialog(this, "My Previous Feedbacks", true);
            feedbackDialog.setSize(600, 400);
            feedbackDialog.setLocationRelativeTo(this);
            feedbackDialog.setLayout(new BorderLayout());
            
            // Create text area to display feedbacks
            JTextArea feedbackTextArea = new JTextArea();
            feedbackTextArea.setEditable(false);
            feedbackTextArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
            
            StringBuilder sb = new StringBuilder();
            sb.append("YOUR FEEDBACK HISTORY\n");
            sb.append("=====================\n\n");
            
            for (List<String> record : feedbackRecords) {
                // Assuming record structure: [feedbackId, customerId, employeeId, content, date]
                if (record.size() >= 5) {
                    String date = record.get(4);
                    String employeeId = record.get(2);
                    String content = record.get(3);
                    
                    // Get employee name
                    String employeeName = getEmployeeNameById(employeeId);
                    
                    sb.append("Date: ").append(date).append("\n");
                    sb.append("To: ").append(employeeName != null ? employeeName : employeeId).append("\n");
                    sb.append("Feedback: ").append(content).append("\n");
                    sb.append("----------------------------------------\n\n");
                }
            }
            
            feedbackTextArea.setText(sb.toString());
            
            JScrollPane scrollPane = new JScrollPane(feedbackTextArea);
            feedbackDialog.add(scrollPane, BorderLayout.CENTER);
            
            // Close button
            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(e -> feedbackDialog.dispose());
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(closeButton);
            feedbackDialog.add(buttonPanel, BorderLayout.SOUTH);
            
            feedbackDialog.setVisible(true);
            
        } catch (Exception ex) {
            showError("Error loading feedbacks: " + ex.getMessage());
        }
    }

    private String getEmployeeNameById(String employeeId) {
        try {
            Set<List<String>> employeeRecords = currentCustomer.getAllNonManagerEmployeeRecords();
            for (List<String> record : employeeRecords) {
                if (record.get(0).equals(employeeId)) {
                    return record.get(1); // Return employee name
                }
            }
        } catch (Exception ex) {
            // Silently fail - we'll just show the ID instead
        }
        return null;
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
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setName("selectionPanel"); // NOI18N

        jLabel1.setText("Select Staff/Doctor:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.setName("employeeComboBox"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setName("inputPanel"); // NOI18N

        jLabel2.setText("Your Feedback:");

        jScrollPane1.setName("feedbackTextArea"); // NOI18N

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setName("feedbackTextArea"); // NOI18N
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jLabel2))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jPanel3.setName("buttonPanel"); // NOI18N

        jButton1.setText("Submit Feedback");
        jButton1.setName("submitBtn"); // NOI18N

        jButton2.setText("View My Feedbacks");
        jButton2.setName("viewBtn"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(57, 57, 57))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(45, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(179, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        java.awt.EventQueue.invokeLater(() -> new CustomerFeedbackFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
