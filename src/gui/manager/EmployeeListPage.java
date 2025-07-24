package gui.manager;

import database.Database;
import gui.helper.ListenerHelper;
import gui.helper.PageDesigner;
import gui.helper.TableHelper;
import user.Doctor;
import user.Manager;
import user.Staff;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.*;
import java.util.List;

public class EmployeeListPage extends JFrame {
    private Manager managerUser;

    private final JLabel titleLabel = new JLabel("Manage Employees");
    private final JButton managersButton = new JButton("Managers");
    private final JButton staffsButton = new JButton("Staffs");
    private final JButton doctorsButton = new JButton("Doctors");
    private final JButton addButton = new JButton("Add");
    private final JButton editButton = new JButton("Edit");
    private final JButton deleteButton = new JButton("Delete");
    private final JButton backButton = new JButton("Back");
    private final DefaultTableModel tableModel = new DefaultTableModel(new String[] {"Employee ID", "Name", "Email"}, 0);;
    private final JTable employeeTable = new JTable(tableModel);;
    private final JScrollPane scrollPane = new JScrollPane(employeeTable);;

    public EmployeeListPage(Manager managerUser) {
        this.managerUser = managerUser;

        JButton[] loadPanelButtons = {managersButton, staffsButton, doctorsButton};
        JButton[] operatePanelButtons = {addButton, editButton, deleteButton};

        TableHelper.configureToPreferredSettings(this.employeeTable, 600, 200, operatePanelButtons);

        EmployeeButtonListener ebl = this.new EmployeeButtonListener();
        this.managersButton.addActionListener(ebl);
        this.staffsButton.addActionListener(ebl);
        this.doctorsButton.addActionListener(ebl);
        this.addButton.addActionListener(this.new AddButtonListener());
        this.editButton.addActionListener(this.new EditButtonListener());
        this.deleteButton.addActionListener(this.new DeleteButtonListener());
        this.backButton.addActionListener(this.new BackButtonListener());

        this.editButton.setEnabled(false);
        this.deleteButton.setEnabled(false);

        this.setTitle("Employee List Page");
        PageDesigner.displayBorderLayoutListPage(this, titleLabel, loadPanelButtons, operatePanelButtons, backButton, scrollPane);
    }

    private List<Object[]> getEmployeeRecordsByType(String type) {
        return switch (type) {
            case "Managers" -> TableHelper.asListOfObjectArray(managerUser.getAllManagerPublicRecords());
            case "Staffs" -> TableHelper.asListOfObjectArray(managerUser.getAllStaffPublicRecords());
            case "Doctors" -> TableHelper.asListOfObjectArray(managerUser.getAllDoctorPublicRecords());
            default -> new ArrayList<>();
        };
    }

    private class EmployeeButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String employeeType = ((JButton) e.getSource()).getText();
            List<Object[]> employeeRecords = getEmployeeRecordsByType(employeeType);

            JButton[] operatePanelButtonsToDisable = {editButton, deleteButton};
            ListenerHelper.loadButtonClicked(tableModel, employeeRecords, operatePanelButtonsToDisable);
        }
    }

    private class TableRowSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                boolean hasSelectedRow = employeeTable.getSelectedRow() != -1;
                editButton.setEnabled(hasSelectedRow);
                deleteButton.setEnabled(hasSelectedRow);
            }
        }
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
                            Manager newManager = new Manager(name, email); // initial user password will be the email itself, user can change the password later
                            EmployeeListPage.this.managerUser.addManager(newManager);
                            JOptionPane.showMessageDialog(null, "Successfully added new manager account", "Employee Created Successfully", JOptionPane.PLAIN_MESSAGE);
                            break;
                        case "Staff":
                            Staff newStaff = new Staff(name, email);
                            EmployeeListPage.this.managerUser.addStaff(newStaff);
                            JOptionPane.showMessageDialog(null, "Successfully created new staff account", "Employee Added Successfully", JOptionPane.PLAIN_MESSAGE);
                            break;
                        case "Doctor":
                            Doctor newDoctor = new Doctor(name, email);
                            EmployeeListPage.this.managerUser.addDoctor(newDoctor);
                            JOptionPane.showMessageDialog(null, "Successfully created new doctor account", "Employee Added Successfully", JOptionPane.PLAIN_MESSAGE);
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "Could not recognize role", "Role Not Found Error", JOptionPane.ERROR_MESSAGE);
                    }
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
                            password = Database.getManager(id).getPassword();
                            Manager newManager = new Manager(id, newName, newEmail, password);
                            managerUser.updateManager(newManager);
                            JOptionPane.showMessageDialog(null, "Successfully edited manager information", "Employee Updated Successfully", JOptionPane.PLAIN_MESSAGE);
                            break;
                        case 'S':
                            password = Database.getStaff(id).getPassword();
                            Staff newStaff = new Staff(id, newName, newEmail, password);
                            managerUser.updateStaff(newStaff);
                            JOptionPane.showMessageDialog(null, "Successfully edited staff information", "Employee Updated Successfully", JOptionPane.PLAIN_MESSAGE);
                            break;
                        case 'D':
                            password = Database.getDoctor(id).getPassword();
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
                }
            }
        }
    }

    private class BackButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new ManagerMainPage(EmployeeListPage.this.managerUser));
            EmployeeListPage.this.dispose();
        }
    }
}
