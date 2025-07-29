package gui.staff;

import gui.helper.PageDesigner;
import gui.helper.ListenerHelper;
import gui.helper.TableHelper;
import user.Customer;
import user.Staff;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManageCustomersPage extends JFrame {
    private final Staff staffUser;
    private final DefaultTableModel tableModel = new DefaultTableModel(new String[] {"Customer ID", "Name", "Email", "ApWallet"}, 0);
    private final JTable customerTable = new JTable(tableModel);

    private class LoadButtonListener implements ActionListener {

        JButton[] operatePanelButtonsToDisable;

        public LoadButtonListener(JButton[] operatePanelButtonsToDisable) {
            this.operatePanelButtonsToDisable = operatePanelButtonsToDisable;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ListenerHelper.loadButtonClicked(tableModel, staffUser.getAllCustomerPublicRecords(), operatePanelButtonsToDisable);
        }
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JTextField nameField = new JTextField(15);
            JTextField emailField = new JTextField(15);

            JLabel nameLabel = new JLabel("Name:");
            JLabel emailLabel = new JLabel("Email:");

            JTextField[] textFields = {nameField, emailField};
            JLabel[] labels = {nameLabel, emailLabel};

            JPanel panel = ListenerHelper.getCustomizedUserInputPanel(textFields, labels);

            int result = JOptionPane.showConfirmDialog(null, panel, "Add New Customer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String name = nameField.getText();
                String email = emailField.getText();
                try {
                    Customer newCustomer = new Customer(name, email);
                    staffUser.addCustomer(newCustomer);
                    JOptionPane.showMessageDialog(null, "Successfully created new customer account", "Customer Created Successfully", JOptionPane.PLAIN_MESSAGE);
                } catch (RuntimeException exception) {
                    JOptionPane.showMessageDialog(null, exception.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private class EditButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int row = customerTable.getSelectedRow();
            if (row != -1) {
                String id = (String) tableModel.getValueAt(row, 0);
                String name = (String) tableModel.getValueAt(row, 1);
                String email = (String) tableModel.getValueAt(row, 2);
                String password = staffUser.getCustomerById(id).getPassword();
                double apWallet = staffUser.getCustomerById(id).getApWallet();

                String newName = JOptionPane.showInputDialog("New name:", name);
                if (newName == null) {
                    return;
                }
                String newEmail = JOptionPane.showInputDialog("New email:", email);
                if (newEmail == null) {
                    return;
                }

                try {
                    Customer newCustomer = new Customer(id, newName, newEmail, password, apWallet);
                    staffUser.updateCustomer(newCustomer);
                    JOptionPane.showMessageDialog(null, "Successfully edited customer information", "Customer Updated Successfully", JOptionPane.PLAIN_MESSAGE);
                } catch (RuntimeException exception) {
                    JOptionPane.showMessageDialog(null, exception.getMessage(), "Input error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int row = customerTable.getSelectedRow();
            if (row != -1) {
                String id = (String) tableModel.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(null, String.format("Are you sure you want to delete the account of customer %s and all of its references?", id), "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (confirm == JOptionPane.YES_NO_OPTION) {
                    staffUser.removeCustomerById(id);
                    JOptionPane.showMessageDialog(null, "Successfully deleted customer", "Customer Deleted Successfully", JOptionPane.PLAIN_MESSAGE);
                }
            }
        }
    }

    private class BackButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new StaffMainPage(staffUser));
            ManageCustomersPage.this.dispose();
        }
    }

    public ManageCustomersPage(Staff staffUser) {
        this.staffUser = staffUser;

        JLabel titleLabel = new JLabel("Manage Customers");
        JButton loadButton = new JButton("Load");
        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        JButton backButton = new JButton("Back");
        JScrollPane scrollPane = new JScrollPane(customerTable);
        JButton[] loadPanelButtons = {loadButton};

        JButton[] operatePanelButtons = {addButton, editButton, deleteButton};

        loadButton.addActionListener(this.new LoadButtonListener(new JButton[] {editButton, deleteButton}));
        addButton.addActionListener(this.new AddButtonListener());
        editButton.addActionListener(this.new EditButtonListener());
        deleteButton.addActionListener(this.new DeleteButtonListener());
        backButton.addActionListener(this.new BackButtonListener());

        editButton.setEnabled(false);
        deleteButton.setEnabled(false);

        TableHelper.configureToPreferredSettings(this.customerTable, 600, 200, new JButton[] {editButton, deleteButton});

        PageDesigner.displayBorderLayoutListPage(this, "Manage Customers", titleLabel, loadPanelButtons, operatePanelButtons, backButton, scrollPane);
    }
}
