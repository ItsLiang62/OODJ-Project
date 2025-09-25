package gui.doctor;

import gui.helper.ListenerHelper;
import gui.helper.PageDesigner;
import gui.helper.TableHelper;
import java.awt.Component;
import user.Doctor;
import operation.Appointment;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.swing.table.DefaultTableModel;

public class DoctorAppointmentsPage extends JFrame {
    private final Doctor doctorUser;
    private final DefaultTableModel tableModel = new DefaultTableModel(new String[] {"Appointment ID", "Customer ID", "Doctor ID", "Doctor Feedback", "Consultation Fee", "Status"}, 0);
    private final JTable appointmentTable = new JTable(tableModel); 

    public DoctorAppointmentsPage(Doctor doctorUser) {
        this.doctorUser = doctorUser;

        JLabel titleLabel = new JLabel("Manage Appointments");
        JButton loadButton = new JButton("Load");
        JButton backButton = new JButton("Back");
        JButton deleteButton = new JButton("Delete");
        JButton provideFeedbackButton = new JButton("Provide Feedback");
        JButton consultationFeeButton = new JButton("Set Consultation Fee");        
        JScrollPane scrollPane = new JScrollPane(appointmentTable);


        JButton[] operatePanelButtons = {deleteButton, provideFeedbackButton, consultationFeeButton}; 
        
        loadButton.addActionListener(new ListenerHelper.LoadButtonListener(tableModel, doctorUser::getMyAppointmentRecords, new JButton[] {deleteButton}));
        provideFeedbackButton.addActionListener(this.new ProvideFeedbackButtonListener());
        consultationFeeButton.addActionListener(this.new ConsultationFeeButtonListener());  
        deleteButton.addActionListener(this.new DeleteButtonListener());
        backButton.addActionListener(this.new BackButtonListener());        

        TableHelper.configureToPreferredSettings(appointmentTable, 600, 200, new JButton[] {deleteButton,provideFeedbackButton,consultationFeeButton});

        PageDesigner.displayBorderLayoutListPage(this, "Manage Appointments Page", titleLabel, new JButton[] {loadButton}, operatePanelButtons, backButton, scrollPane);        
        
        deleteButton.setEnabled(false);
        provideFeedbackButton.setEnabled(false);
        consultationFeeButton.setEnabled(false);
//        setVisible(true);
    }
    
    private class ProvideFeedbackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int row = appointmentTable.getSelectedRow();

            if (row != -1) {
                String id = (String) tableModel.getValueAt(row, 0);

                // Input dialog for feedback
                String feedback = JOptionPane.showInputDialog(
                        null,
                        "Please enter your feedback for appointment " + id + ":",
                        "Provide Feedback",
                        JOptionPane.PLAIN_MESSAGE
                );

                if (feedback != null) { // user clicked OK
                    feedback = feedback.trim();
                    if (!feedback.isEmpty()) {
                        try {
                            // call your doctor logic
                            doctorUser.provideFeedback(id, feedback);

                            // update the table model (assume feedback column = index 5)
                            int feedbackColIndex = 5; // adjust if needed
                            tableModel.setValueAt(feedback, row, feedbackColIndex);

                            JOptionPane.showMessageDialog(
                                    null,
                                    "Successfully provided feedback",
                                    "Appointment Updated Successfully",
                                    JOptionPane.PLAIN_MESSAGE
                            );
                        } catch (RuntimeException ex) {
                            JOptionPane.showMessageDialog(
                                    null,
                                    ex.getMessage(),
                                    "Unexpected Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    } else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Feedback cannot be empty!",
                                "Validation Error",
                                JOptionPane.WARNING_MESSAGE
                        );
                    }
                }
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "Please select an appointment first.",
                        "No Selection",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        }
    }

    
    
private class ConsultationFeeButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        int row = appointmentTable.getSelectedRow();

        if (row != -1) {
            String id = (String) tableModel.getValueAt(row, 0);

            // Input dialog for consultation fee
            String input = JOptionPane.showInputDialog(
                    null,
                    "Please enter the consultation fee for appointment " + id + ":",
                    "Set Consultation Fee",
                    JOptionPane.PLAIN_MESSAGE
            );

            if (input != null) { // user clicked OK
                try {
                    double fee = Double.parseDouble(input.trim());

                    try {
                        // call your doctor logic
                        doctorUser.setConsultationFee(id, fee);

                        // update the table model (assume consultation fee column = index 6)
                        int feeColIndex = 4; // adjust if needed
                        tableModel.setValueAt(fee, row, feeColIndex);

                        JOptionPane.showMessageDialog(
                                null,
                                "Successfully set consultation fee",
                                "Appointment Updated Successfully",
                                JOptionPane.PLAIN_MESSAGE
                        );
                    } catch (RuntimeException ex) {
                        JOptionPane.showMessageDialog(
                                null,
                                ex.getMessage(),
                                "Unexpected Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Invalid input. Please enter a valid number for the consultation fee.",
                            "Input Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        } else {
            JOptionPane.showMessageDialog(
                    null,
                    "Please select an appointment first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }
}

    
    
    
    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int row = appointmentTable.getSelectedRow();
            if (row != -1) {
                String id = (String) tableModel.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(null, String.format("Are you sure you want to delete the appointment %s and all of its references?", id), "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (confirm == JOptionPane.YES_NO_OPTION) {
                    doctorUser.removeAppointmentById(id);
                    JOptionPane.showMessageDialog(null, "Successfully deleted appointment", "Appointment Deleted Successfully", JOptionPane.PLAIN_MESSAGE);
                }
            }
        }
    }

    private class BackButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new DoctorMainPage(DoctorAppointmentsPage.this.doctorUser));
            DoctorAppointmentsPage.this.dispose();
        }
    }    
}




//package gui.doctor;
//
//import gui.helper.PageDesigner;
//import user.Doctor;
//import operation.Appointment;
//
//import javax.swing.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//public class DoctorAppointmentsPage extends JFrame {
//    private final Doctor doctorUser;
//
//    public DoctorAppointmentsPage(Doctor doctorUser) {
//        this.doctorUser = doctorUser;
//
//        JLabel titleLabel = new JLabel("My Appointments");
//
//        // Show appointments in a table
//        String[] columns = {"Appointment ID", "Customer ID", "Date", "Status"};
//        java.util.List<java.util.List<String>> appointmentRecords = doctorUser.getMyAppointmentRecords();
//        String[][] data = appointmentRecords.stream().map(l -> l.toArray(new String[0])).toArray(String[][]::new);
//
//        JTable appointmentTable = new JTable(data, columns);
//        JScrollPane scrollPane = new JScrollPane(appointmentTable);
//
//        JButton backButton = new JButton("Back");
//        backButton.addActionListener(this.new BackButtonListener());
//
//        PageDesigner.displayBorderLayoutWithTable(this, "Doctor Appointments", titleLabel, scrollPane, backButton);
//    }
//
//    private class BackButtonListener implements ActionListener {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            SwingUtilities.invokeLater(() -> new DoctorMainPage(doctorUser));
//            dispose();
//        }
//    }
//}






//        setTitle("Check Appointments");
//        setSize(700, 400);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLayout(new BorderLayout());
//
//        JLabel header = new JLabel("Appointments - Dr. " + doctorUser.getName(), SwingConstants.CENTER);
//        add(header, BorderLayout.NORTH);
//
//        String[] columnNames = {"Appointment ID", "Patient Name", "Date", "Time", "Status"};
//        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
//        JTable table = new JTable(tableModel);
//
//        // Example dummy data (you can later fetch from DB)
//        tableModel.addRow(new Object[]{"APT001", "John Doe", "2025-09-25", "10:00 AM", "Pending"});
//        tableModel.addRow(new Object[]{"APT002", "Jane Smith", "2025-09-26", "2:00 PM", "Confirmed"});
//
//        JScrollPane scrollPane = new JScrollPane(table);
//        add(scrollPane, BorderLayout.CENTER);
