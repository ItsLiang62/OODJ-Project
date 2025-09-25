package gui.doctor;

import gui.helper.PageDesigner;
import gui.helper.TableHelper;
import operation.Appointment;
import operation.AppointmentMedicine;
import operation.Medicine;
import user.Doctor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.Set;

public class DoctorPrescription extends JFrame {
    private final Doctor doctorUser;
    private final DefaultTableModel tableModel;

    public DoctorPrescription(Doctor doctorUser) {
        this.doctorUser = doctorUser;

        // UI components
        JLabel titleLabel = new JLabel("Manage Prescription");
        JButton loadButton = new JButton("Load");
        JButton backButton = new JButton("Back");
        JButton addButton = new JButton("Add");

        // Table setup
        tableModel = new DefaultTableModel(AppointmentMedicine.getColumnNames(), 0);
        JTable prescriptionTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(prescriptionTable);

        // Load prescriptions
        loadButton.addActionListener(e -> {
            tableModel.setRowCount(0); // clear old rows
            for (var record : doctorUser.getMyPrescriptionRecords()) {
                tableModel.addRow(record.toArray());
            }
        });

        // Button listeners
        backButton.addActionListener(new BackButtonListener());
        addButton.addActionListener(new AddButtonListener());

        // Configure table
        TableHelper.configureToPreferredSettings(prescriptionTable, 600, 200, new JButton[]{addButton});

        // Layout
        PageDesigner.displayBorderLayoutListPage(
                this,
                "Prescription Page",
                titleLabel,
                new JButton[]{loadButton},
                new JButton[]{addButton},
                backButton,
                scrollPane
        );
    }

    // ---------------- Inner Listeners ----------------

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Appointment dropdown (from Set)
            Set<Appointment> appointments = doctorUser.getMyAppointments();
            JComboBox<String> appointmentBox = new JComboBox<>(
                    appointments.stream().map(Appointment::getId).toArray(String[]::new)
            );

            // Medicine dropdown
            JComboBox<String> medicineBox = new JComboBox<>(
                    Medicine.getFieldVals(Medicine.getAll(), Medicine::getId).toArray(new String[0])
            );

            // Symptom input
            JTextField symptomField = new JTextField();

            // Panel for dialog
            JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
            panel.add(new JLabel("Appointment:"));
            panel.add(appointmentBox);
            panel.add(new JLabel("Medicine:"));
            panel.add(medicineBox);
            panel.add(new JLabel("Target Symptom:"));
            panel.add(symptomField);

            // Show dialog
            int result = JOptionPane.showConfirmDialog(
                    null,
                    panel,
                    "Create Prescription",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            // Save if confirmed
            if (result == JOptionPane.OK_OPTION) {
                try {
                    String appointmentId = Objects.requireNonNull((String) appointmentBox.getSelectedItem());
                    String medicineId = Objects.requireNonNull((String) medicineBox.getSelectedItem());
                    String targetSymptom = symptomField.getText();

                    doctorUser.prescribeMedicine(appointmentId, medicineId, targetSymptom);
                    JOptionPane.showMessageDialog(null, "Prescription created successfully!");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new DoctorMainPage(DoctorPrescription.this.doctorUser));
            DoctorPrescription.this.dispose();
        }
    }
}


























//package gui.doctor;
//
//import gui.helper.ListenerHelper;
//import gui.helper.PageDesigner;
//import gui.helper.TableHelper;
//import java.awt.Component;
//import operation.AppointmentMedicine;
//import user.Doctor;
//
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//import operation.Appointment;
//
//public class DoctorPrescription extends JFrame {
//    private final Doctor doctorUser;
//
//    public DoctorPrescription(Doctor doctorUser) {
//        this.doctorUser = doctorUser;
//
//        JLabel titleLabel = new JLabel("Manage Prescription");
//        JButton loadButton = new JButton("Load");
//        JButton backButton = new JButton("Back");
//        JButton addButton = new JButton("Add");
//        DefaultTableModel tableModel = new DefaultTableModel(AppointmentMedicine.getColumnNames(), 0);
//        JTable prescriptionTable = new JTable(tableModel);
//        JScrollPane scrollPane = new JScrollPane(prescriptionTable);
//
//        JButton[] operatePanelButtons = {addButton}; 
//        
//        loadButton.addActionListener(new ListenerHelper.LoadButtonListener(tableModel, () -> doctorUser.getMyPrescriptionRecords(), null));
//        backButton.addActionListener(this.new BackButtonListener());
//        addButton.addActionListener(this.new AddButtonListener());
//
//        TableHelper.configureToPreferredSettings(prescriptionTable, 600, 200, new JButton[] {addButton});
//
//        PageDesigner.displayBorderLayoutListPage(this, "View Medicine Page", titleLabel, new JButton[] {loadButton},operatePanelButtons, backButton, scrollPane);
//    }
//
//    private class AddButtonListener implements ActionListener {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            List<String> appointmentId = new ArrayList<>();
//            doctorUser.getMyAppointmentRecords().forEach(
//                    record -> appointmentId.add(record.getFirst() + " " + record.get(1))
//            );
//            JLabel appointmentLabel = new JLabel("Appointment:");
//            JComboBox<String> appointmentComboBox = new JComboBox<>(appointmentId.toArray(new String[0]));
//
//            JPanel panel = ListenerHelper.getCustomizedUserInputPanel(new Component[] {appointmentComboBox}, new JLabel[] {appointmentLabel});
//
//            int result = JOptionPane.showConfirmDialog(null, panel, "Create Prescription for Appointment", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
//
//            if (result == JOptionPane.OK_OPTION) {
//                String customerId;
//                try {
//                    customerId = ((String) (Objects.requireNonNull(appointmentComboBox.getSelectedItem()))).substring(0, 4);
//                } catch (NullPointerException ex) {
//                    JOptionPane.showMessageDialog(null, "No customer is selected!", "Failed to Create Appointment", JOptionPane.ERROR_MESSAGE);
//                    return;
//                }
//                try {
//                    Appointment newAppointment = new Appointment(customerId);
//                    doctorUser.addAppointment(newAppointment);
//                    JOptionPane.showMessageDialog(null, "Successfully created appointment for customer", "Appointment Created Successfully", JOptionPane.PLAIN_MESSAGE);
//                } catch (RuntimeException ex) {
//                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Unexpected Error", JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        }
//    }
//    
//    private class BackButtonListener implements ActionListener {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            SwingUtilities.invokeLater(() -> new DoctorMainPage(DoctorPrescription.this.doctorUser));
//            DoctorPrescription.this.dispose();
//        }
//    }
//}
//
