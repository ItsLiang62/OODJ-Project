/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui.customer;
import user.Customer;
import operation.Appointment;
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
public class CustomerAppointmentsFrame extends javax.swing.JFrame {
    
    private final Customer customerUser;
    private JTable appointmentsTable;
    private DefaultTableModel tableModel;
    /**
     * Creates new form CustomerAppointmentsFrame
     * @param customerUser
     */
    public CustomerAppointmentsFrame(Customer customerUser) {
        this.customerUser = customerUser;
        initializeUI();
        loadAppointmentsData();
    }
    private void initializeUI() {
        setTitle("My Appointments - " + customerUser.getName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        // Create main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 248, 255));

        // Create title label
        JLabel titleLabel = new JLabel("My Appointments", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 51, 102));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Create table model and table
        String[] columnNames = Appointment.getColumnNames();
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        appointmentsTable = new JTable(tableModel);
        appointmentsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        appointmentsTable.setRowHeight(25);
        appointmentsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        appointmentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(appointmentsTable);
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

    private void loadAppointmentsData() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        List<List<String>> appointmentRecords = customerUser.getMyAppointmentRecords();
        
        if (appointmentRecords.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No appointments found.", "Information", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (List<String> record : appointmentRecords) {
                tableModel.addRow(record.toArray());
            }
        }
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }



    private class RefreshButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            loadAppointmentsData();
            JOptionPane.showMessageDialog(CustomerAppointmentsFrame.this, 
                "Appointments list refreshed.", "Information", JOptionPane.INFORMATION_MESSAGE);
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
            .addGap(0, 640, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
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
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

