package gui.manager;

import database.Database;
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

    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    private JLabel title;
    private JButton managersButton, staffsButton, doctorsButton, addButton, editButton, deleteButton, backButton;

    public EmployeeListPage(Manager managerUser) {
        this.managerUser = managerUser;

        this.title = new JLabel("Manage Employees");
        this.managersButton = new JButton("Managers");
        this.staffsButton = new JButton("Staffs");
        this.doctorsButton = new JButton("Doctors");
        this.addButton = new JButton("Add");
        this.editButton = new JButton("Edit");
        this.deleteButton = new JButton("Delete");
        this.backButton = new JButton("Back");

        this.tableModel = new DefaultTableModel(new String[] {"Employee ID", "Name", "Email"}, 0);
        this.employeeTable = new JTable(tableModel);
        this.employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.employeeTable.getSelectionModel().addListSelectionListener(this.new TableRowSelectionListener()); // the non-static nested class will handle the list selection listening
        this.employeeTable.setPreferredScrollableViewportSize(new Dimension(600, 200));
        this.employeeTable.setFillsViewportHeight(true);
        this.scrollPane = new JScrollPane(employeeTable);

        setTitle("Employee List Page");
        setSize(700, 500);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GridBagConstraints gbc = new GridBagConstraints();

        // Adding panels to EmployeeListPage (BorderLayout)
        JPanel northPanel = new JPanel(new GridBagLayout());
        JPanel centerPanel = new JPanel(new GridBagLayout());
        JPanel southPanel = new JPanel(new GridBagLayout());

        this.add(northPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(southPanel, BorderLayout.SOUTH);

        // Adding panels to northPanel (GridBagLayout)
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;

        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel employeeButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));

        gbc.gridy = 0;
        northPanel.add(backPanel, gbc);
        gbc.gridy = 1;
        northPanel.add(titlePanel, gbc);
        gbc.gridy = 2;
        northPanel.add(employeeButtonsPanel, gbc);

        // Adding panels to centerPanel (GridBagLayout)
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;

        gbc.gridy = 0;
        centerPanel.add(this.scrollPane, gbc);

        // Adding panels to southPanel (GridBagLayout)
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;

        JPanel operatePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        JPanel fillerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));

        gbc.gridy = 0;
        southPanel.add(operatePanel, gbc);
        gbc.gridy = 1;
        southPanel.add(fillerPanel, gbc);

        // Add components to backPanel (FlowLayout)
        this.backButton.addActionListener(this.new BackButtonListener());
        backPanel.add(this.backButton);

        // Add components to titlePanel (FlowLayout)
        titlePanel.add(this.title);

        // Add components to employeeButtonsPanel (FlowLayout)
        EmployeeButtonListener ebl = this.new EmployeeButtonListener();
        this.managersButton.addActionListener(ebl);
        this.staffsButton.addActionListener(ebl);
        this.doctorsButton.addActionListener(ebl);
        employeeButtonsPanel.add(this.managersButton);
        employeeButtonsPanel.add(this.staffsButton);
        employeeButtonsPanel.add(this.doctorsButton);

        // Add components to operatePanel (FlowLayout)
        this.addButton.addActionListener(this.new AddButtonListener());
        this.editButton.addActionListener(this.new EditButtonListener());
        this.deleteButton.addActionListener(this.new DeleteButtonListener());
        this.editButton.setEnabled(false);
        this.deleteButton.setEnabled(false);
        operatePanel.add(this.addButton);
        operatePanel.add(this.editButton);
        operatePanel.add(this.deleteButton);

        this.setVisible(true);
    }

    private Object[][] getEmployeeRecordsByType(String type) {
        return switch (type) {
            case "Managers" -> to2dObjectArray(managerUser.getAllManagerPublicRecords());
            case "Staffs" -> to2dObjectArray(managerUser.getAllStaffPublicRecords());
            case "Doctors" -> to2dObjectArray(managerUser.getAllDoctorPublicRecords());
            default -> new Object[0][];
        };
    }

    private Object[][] to2dObjectArray(Set<List<String>> recordSet) {
        List<Object[]> allObjects = new ArrayList<>();
        for (List<String> objectRecord: recordSet) {
            allObjects.add(objectRecord.toArray()); // String array is Object array
        }

        return allObjects.toArray(new Object[0][]);
    }

    private class EmployeeButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String employeeType = ((JButton) e.getSource()).getText();
            Object[][] employeeRecords = getEmployeeRecordsByType(employeeType);

            EmployeeListPage.this.tableModel.setRowCount(0);
            for (Object[] record: employeeRecords) {
                tableModel.addRow(record);
            }
            editButton.setEnabled(false);
            deleteButton.setEnabled(false);
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
            JComboBox<String> roleComboBox = new JComboBox<>(new String[] {"Manager", "Staff", "Doctor"});
            JTextField nameField = new JTextField(15);
            JTextField emailField = new JTextField(15);

            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);

            gbc.anchor = GridBagConstraints.EAST;
            gbc.gridx = 0;

            gbc.gridy = 0;
            panel.add(new JLabel("Position:"), gbc);
            gbc.gridy = 1;
            panel.add(new JLabel("Name:"), gbc);
            gbc.gridy = 2;
            panel.add(new JLabel("Email:"), gbc);

            gbc.anchor = GridBagConstraints.WEST;
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            gbc.gridy = 0;
            panel.add(roleComboBox, gbc);
            gbc.gridy = 1;
            panel.add(nameField, gbc);
            gbc.gridy = 2;
            panel.add(emailField, gbc);

            int result = JOptionPane.showConfirmDialog(
                    null, panel, "Add New Employee", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.OK_OPTION) {
                String role = (String) roleComboBox.getSelectedItem();
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();

                try {
                    switch (Objects.requireNonNull(role)) {
                        case "Manager":
                            Manager newManager = new Manager(name, email, email); // initial user password will be the email itself, user can change the password later
                            EmployeeListPage.this.managerUser.addManager(newManager);
                            JOptionPane.showMessageDialog(null, "Successfully added new manager account", "Employee Created Successfully", JOptionPane.PLAIN_MESSAGE);
                            break;
                        case "Staff":
                            Staff newStaff = new Staff(name, email, email);
                            EmployeeListPage.this.managerUser.addStaff(newStaff);
                            JOptionPane.showMessageDialog(null, "Successfully created new staff account", "Employee Added Successfully", JOptionPane.PLAIN_MESSAGE);
                            break;
                        case "Doctor":
                            Doctor newDoctor = new Doctor(name, email, email);
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
            Manager managerUser = EmployeeListPage.this.managerUser;
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
