package gui.staff;

import gui.helper.ListenerHelper;
import gui.helper.PageDesigner;
import gui.helper.TableHelper;
import operation.Appointment;
import operation.Invoice;
import user.Staff;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ManageAppointmentsPage extends JFrame {
    private final Staff staffUser;
    private final DefaultTableModel tableModel = new DefaultTableModel(new String[] {"Appointment ID", "Customer ID", "Doctor ID", "Doctor Feedback", "Consultation Fee", "Status"}, 0);
    private final JTable appointmentTable = new JTable(tableModel);

    public ManageAppointmentsPage(Staff staffUser) {
        this.staffUser = staffUser;

        JLabel titleLabel = new JLabel("Manage Appointments");
        JButton loadButton = new JButton("Load");
        JButton addButton = new JButton("Add");
        JButton backButton = new JButton("Back");
        JButton assignDoctorButton = new JButton("Assign Doctor");
        JButton collectPaymentButton = new JButton("Collect Payment");
        JButton deleteButton = new JButton("Delete");
        JScrollPane scrollPane = new JScrollPane(appointmentTable);

        JButton[] operatePanelButtons = {addButton, assignDoctorButton, collectPaymentButton, deleteButton};

        loadButton.addActionListener(new ListenerHelper.LoadButtonListener<>(tableModel, staffUser.getAllAppointmentPublicRecords(), new JButton[] {assignDoctorButton, collectPaymentButton, deleteButton}));
        addButton.addActionListener(this.new AddButtonListener());
        assignDoctorButton.addActionListener(this.new AssignDoctorButtonListener());
        collectPaymentButton.addActionListener(this.new CollectPaymentButtonListener());
        deleteButton.addActionListener(this.new DeleteButtonListener());
        backButton.addActionListener(this.new BackButtonListener());

        assignDoctorButton.setEnabled(false);
        collectPaymentButton.setEnabled(false);
        deleteButton.setEnabled(false);

        TableHelper.configureToPreferredSettings(appointmentTable, 600, 200, new JButton[] {assignDoctorButton, collectPaymentButton, deleteButton});

        PageDesigner.displayBorderLayoutListPage(this, "Manage Appointments Page", titleLabel, new JButton[] {loadButton}, operatePanelButtons, backButton, scrollPane);
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            List<String> customerIdName = new ArrayList<>();
            staffUser.getAllCustomerPublicRecords().forEach(
                    record -> customerIdName.add(record.getFirst() + " " + record.get(1))
            );
            JLabel customerLabel = new JLabel("Customer:");
            JComboBox<String> customerComboBox = new JComboBox<>(customerIdName.toArray(new String[0]));

            JPanel panel = ListenerHelper.getCustomizedUserInputPanel(new Component[] {customerComboBox}, new JLabel[] {customerLabel});

            int result = JOptionPane.showConfirmDialog(null, panel, "Create Appointment for Customer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String customerId;
                try {
                    customerId = ((String) (Objects.requireNonNull(customerComboBox.getSelectedItem()))).substring(0, 4);
                } catch (NullPointerException ex) {
                    JOptionPane.showMessageDialog(null, "No customer is selected!", "Failed to Create Appointment", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    Appointment newAppointment = new Appointment(customerId);
                    staffUser.addAppointment(newAppointment);
                    JOptionPane.showMessageDialog(null, "Successfully created appointment for customer", "Appointment Created Successfully", JOptionPane.PLAIN_MESSAGE);
                } catch (RuntimeException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Unexpected Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private class AssignDoctorButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int row = appointmentTable.getSelectedRow();

            if (row != -1) {
                String id = (String) tableModel.getValueAt(row, 0);

                List<String> doctorIdName = new ArrayList<>();
                staffUser.getAllDoctorPublicRecords().forEach(
                        record -> doctorIdName.add(record.getFirst() + " " + record.get(1))
                );
                JLabel doctorLabel = new JLabel(String.format("Doctor for %s", id));
                JComboBox<String> doctorComboBox = new JComboBox<>(doctorIdName.toArray(new String[0]));

                JPanel panel = ListenerHelper.getCustomizedUserInputPanel(new Component[] {doctorComboBox}, new JLabel[] {doctorLabel});

                int result = JOptionPane.showConfirmDialog(null, panel, "Assign Doctor to Appointment", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    String doctorId;
                    try {
                        doctorId = ((String) (Objects.requireNonNull(doctorComboBox.getSelectedItem()))).substring(0, 4);
                    } catch (NullPointerException ex) {
                        JOptionPane.showMessageDialog(null, "No doctor is selected!", "Failed to Assign Doctor", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    try {
                        staffUser.assignDoctorToAppointment(id, doctorId);
                        JOptionPane.showMessageDialog(null, "Successfully assigned doctor to appointment", "Appointment Updated Successfully", JOptionPane.PLAIN_MESSAGE);
                    } catch (RuntimeException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Unexpected Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    private class CollectPaymentButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int row = appointmentTable.getSelectedRow();

            if (row != -1) {

                String id = (String) tableModel.getValueAt(row, 0);
                Appointment appointment = staffUser.getAppointmentById(id);
                String customerId = appointment.getCustomerId();
                double consultationFee = appointment.getConsultationFee();
                double medicineCharges = appointment.getTotalMedicineCharges();

                int confirm = JOptionPane.showConfirmDialog(
                        null, String.format("Customer %s to pay for RM%.2f consultation fee and RM%.2f medicine charges. Proceed?", customerId, consultationFee, medicineCharges), "Confirm Payment Collection", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE
                );

                if (confirm == JOptionPane.OK_OPTION) {
                    try {
                        staffUser.collectPayment(id);
                        Invoice newInvoice = new Invoice(id);
                        staffUser.addInvoice(newInvoice);
                        JOptionPane.showMessageDialog(null, "Payment Successful. Invoice created for appointment.", "Payment Successful", JOptionPane.PLAIN_MESSAGE);
                    } catch (RuntimeException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Unexpected Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
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
                    staffUser.removeAppointmentById(id);
                    JOptionPane.showMessageDialog(null, "Successfully deleted appointment", "Appointment Deleted Successfully", JOptionPane.PLAIN_MESSAGE);
                }
            }
        }
    }

    private class BackButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new StaffMainPage(ManageAppointmentsPage.this.staffUser));
            ManageAppointmentsPage.this.dispose();
        }
    }
}
