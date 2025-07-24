package gui.staff;

import gui.helper.ListenerHelper;
import gui.helper.PageDesigner;
import gui.helper.TableHelper;
import operation.Appointment;
import operation.Invoice;
import user.Staff;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AppointmentListPage extends JFrame {
    private Staff staffUser;
    private final JLabel titleLabel = new JLabel("Manage Appointments");
    private final JButton loadButton = new JButton("Load");
    private final JButton addButton = new JButton("Add");
    private final JButton assignDoctorButton = new JButton("Assign Doctor");
    private final JButton collectPaymentButton = new JButton("Collect Payment");
    private final JButton deleteButton = new JButton("Delete");
    private final JButton backButton = new JButton("Back");
    private final DefaultTableModel tableModel = new DefaultTableModel(new String[] {"Appointment ID", "Customer ID", "Doctor ID", "Doctor Feedback", "Consultation Fee", "Status"}, 0);
    private final JTable appointmentTable = new JTable(tableModel);
    private final JScrollPane scrollPane = new JScrollPane(appointmentTable);

    public AppointmentListPage(Staff staffUser) {
        this.staffUser = staffUser;

        JButton[] loadPanelButtons = {loadButton};
        JButton[] operatePanelButtons = {addButton, assignDoctorButton, collectPaymentButton, deleteButton};

        TableHelper.configureToPreferredSettings(appointmentTable, 600, 200, operatePanelButtons);

        loadButton.addActionListener(this.new LoadButtonListener());
        addButton.addActionListener(this.new AddButtonListener());
        assignDoctorButton.addActionListener(this.new AssignDoctorButtonListener());
        collectPaymentButton.addActionListener(this.new CollectPaymentButtonListener());
        deleteButton.addActionListener(this.new DeleteButtonListener());
        backButton.addActionListener(this.new BackButtonListener());

        assignDoctorButton.setEnabled(false);
        collectPaymentButton.setEnabled(false);
        deleteButton.setEnabled(false);

        this.setTitle("Appointment List Page");

        PageDesigner.displayBorderLayoutListPage(this, titleLabel, loadPanelButtons, operatePanelButtons, backButton, scrollPane);
    }

    private class LoadButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            List<Object[]> appointmentRecords = TableHelper.asListOfObjectArray(staffUser.getAllAppointmentPublicRecords());
            JButton[] operatePanelButtonsToDisable = {assignDoctorButton, collectPaymentButton, deleteButton};
            ListenerHelper.loadButtonClicked(tableModel, appointmentRecords, operatePanelButtonsToDisable);
        }
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
                String customerId = ((String) (Objects.requireNonNull(customerComboBox.getSelectedItem()))).substring(0, 4);
                System.out.println(customerId);
                try {
                    Appointment newAppointment = new Appointment(customerId);
                    staffUser.addAppointment(newAppointment);
                    JOptionPane.showMessageDialog(null, "Successfully created appointment for customer", "Appointment Created Successfully", JOptionPane.PLAIN_MESSAGE);
                } catch (RuntimeException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Unexpected error", JOptionPane.ERROR_MESSAGE);
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
                    String newDoctorId = ((String) Objects.requireNonNull(doctorComboBox.getSelectedItem())).substring(0, 4);

                    try {
                        staffUser.assignDoctorToAppointment(id, newDoctorId);
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
            SwingUtilities.invokeLater(() -> new StaffMainPage(AppointmentListPage.this.staffUser));
            AppointmentListPage.this.dispose();
        }
    }
}
