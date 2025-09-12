package gui.manager;

import gui.helper.ListenerHelper;
import gui.helper.PageDesigner;
import gui.helper.TableHelper;
import user.Doctor;
import user.Manager;
import user.Staff;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.*;

public class ManageEmployeesPage extends JFrame {
    private final Manager managerUser;
    private final DefaultTableModel tableModel = new DefaultTableModel(new String[] {"Employee ID", "Name", "Email"}, 0);
    private final JTable employeeTable = new JTable(tableModel);

    public ManageEmployeesPage(Manager managerUser) {
        this.managerUser = managerUser;

        JLabel titleLabel = new JLabel("Manage Employees");
        JButton managersButton = new JButton("Managers");
        JButton staffsButton = new JButton("Staffs");
        JButton doctorsButton = new JButton("Doctors");
        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        JButton backButton = new JButton("Back");
        JScrollPane scrollPane = new JScrollPane(employeeTable);

        JButton[] loadPanelButtons = {managersButton, staffsButton, doctorsButton};
        JButton[] operatePanelButtons = {addButton, editButton, deleteButton};

        TableHelper.configureToPreferredSettings(employeeTable, 600, 200, new JButton[] {editButton, deleteButton});

        JButton[] operatePanelButtonsToDisableWhenLoad = {editButton, deleteButton};
        managersButton.addActionListener(new ListenerHelper.LoadButtonListener(tableModel, managerUser::getManagerPublicRecords, operatePanelButtonsToDisableWhenLoad));
        staffsButton.addActionListener(new ListenerHelper.LoadButtonListener(tableModel, managerUser::getStaffPublicRecords, operatePanelButtonsToDisableWhenLoad));
        doctorsButton.addActionListener(new ListenerHelper.LoadButtonListener(tableModel, managerUser::getDoctorPublicRecords, operatePanelButtonsToDisableWhenLoad));
        addButton.addActionListener(this.new AddButtonListener());
        editButton.addActionListener(this.new EditButtonListener());
        deleteButton.addActionListener(this.new DeleteButtonListener());
        backButton.addActionListener(this.new BackButtonListener());

        editButton.setEnabled(false);
        deleteButton.setEnabled(false);

        PageDesigner.displayBorderLayoutListPage(this, "Manage Employees Page", titleLabel, loadPanelButtons, operatePanelButtons, backButton, scrollPane);
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox<String> positionComboBox = new JComboBox<>(new String[] {"Manager", "Staff", "Doctor"});
            JTextField nameField = new JTextField(15);
            JTextField emailField = new JTextField(15);

            JLabel positionLabel = new JLabel("Position:");
            JLabel nameLabel = new JLabel("Name:");
            JLabel emailLabel = new JLabel("Email:");

            Component[] inputFields = {positionComboBox, nameField, emailField};
            JLabel[] labels = {positionLabel, nameLabel, emailLabel};

            JPanel panel = ListenerHelper.getCustomizedUserInputPanel(inputFields, labels);

            int result = JOptionPane.showConfirmDialog(
                    null, panel, "Add New Employee", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.OK_OPTION) {
                String role = (String) positionComboBox.getSelectedItem();
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();

                try {
                    switch (Objects.requireNonNull(role)) {
                        case "Manager":
                            // initial user password will be the email itself, user can change the password later
                            Manager newManager = new Manager(name, email);
                            managerUser.addManager(newManager);
                            break;
                        case "Staff":
                            Staff newStaff = new Staff(name, email);
                            managerUser.addStaff(newStaff);
                            break;
                        case "Doctor":
                            Doctor newDoctor = new Doctor(name, email);
                            managerUser.addDoctor(newDoctor);
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "Could not recognize role", "Role Not Found Error", JOptionPane.ERROR_MESSAGE);
                            return;
                    }
                    JOptionPane.showMessageDialog(null, String.format("Successfully added new %s account", role.toLowerCase()), "Employee Created Successfully", JOptionPane.PLAIN_MESSAGE);
                } catch (RuntimeException exception) {
                    JOptionPane.showMessageDialog(null, exception.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private class EditButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int row = employeeTable.getSelectedRow();
            if (row != -1) {
                String id = (String) tableModel.getValueAt(row, 0);
                String name = (String) tableModel.getValueAt(row, 1);
                String email = (String) tableModel.getValueAt(row, 2);
                String password;

                String newName = JOptionPane.showInputDialog("New name:", name);
                if (newName == null) {
                    return;
                }
                String newEmail = JOptionPane.showInputDialog("New email:", email);
                if (newEmail == null) {
                    return;
                }
                try {
                    switch (id.charAt(0)) {
                        case 'M':
                            password = Manager.getById(id).getPassword();
                            Manager newManager = new Manager(id, newName, newEmail, password);
                            managerUser.updateManager(newManager);
                            JOptionPane.showMessageDialog(null, "Successfully edited manager information", "Employee Updated Successfully", JOptionPane.PLAIN_MESSAGE);
                            break;
                        case 'S':
                            password = Staff.getById(id).getPassword();
                            Staff newStaff = new Staff(id, newName, newEmail, password);
                            managerUser.updateStaff(newStaff);
                            JOptionPane.showMessageDialog(null, "Successfully edited staff information", "Employee Updated Successfully", JOptionPane.PLAIN_MESSAGE);
                            break;
                        case 'D':
                            password = Doctor.getById(id).getPassword();
                            Doctor newDoctor = new Doctor(id, newName, newEmail, password);
                            managerUser.updateDoctor(newDoctor);
                            JOptionPane.showMessageDialog(null, "Successfully edited doctor information", "Employee Updated Successfully", JOptionPane.PLAIN_MESSAGE);
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "Could not recognize employee ID", "Employee ID Not Found Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (RuntimeException exception) {
                    JOptionPane.showMessageDialog(null, exception.getMessage(), "Input error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int row = employeeTable.getSelectedRow();
            if (row != -1) {
                String id = (String) tableModel.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(null, String.format("Are you sure you want to delete the account of employee %s", id), "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (confirm == JOptionPane.YES_NO_OPTION) {
                    try {
                        switch (id.charAt(0)) {
                            case 'M':
                                managerUser.removeManagerById(id);
                                break;
                            case 'S':
                                managerUser.removeStaffById(id);
                                break;
                            case 'D':
                                managerUser.removeDoctorById(id);
                                break;
                        }
                        JOptionPane.showMessageDialog(null, "Successfully deleted employee record", "Employee Deleted Successfully", JOptionPane.PLAIN_MESSAGE);
                    } catch (RuntimeException exception) {
                        JOptionPane.showMessageDialog(null, exception.getMessage(), "Deletion Failed", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    private class BackButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new ManagerMainPage(managerUser));
            dispose();
        }
    }
}
