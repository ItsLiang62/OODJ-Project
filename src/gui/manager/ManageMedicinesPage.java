package gui.manager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import gui.helper.ListenerHelper;
import gui.helper.PageDesigner;
import gui.helper.TableHelper;
import operation.Medicine;
import user.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManageMedicinesPage extends JFrame {
    private final Manager managerUser;
    private final DefaultTableModel tableModel = new DefaultTableModel(new String[] {"Medicine ID", "Name", "Charge"}, 0);
    private final JTable medicineTable = new JTable(tableModel);

    public ManageMedicinesPage(Manager managerUser) {
        this.managerUser = managerUser;

        JLabel titleLabel = new JLabel("Manage Medicines");
        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        JButton loadButton = new JButton("Load");
        JButton backButton = new JButton("Back");
        JScrollPane scrollPane = new JScrollPane(medicineTable);

        JButton[] loadPanelButtons = {loadButton};
        JButton[] operatePanelButtons = {addButton, editButton, deleteButton};
        JButton[] buttonsToDisableWithoutTableRowSelection = {editButton, deleteButton};

        loadButton.addActionListener(this.new LoadButtonListener(new JButton[] {editButton, deleteButton}));
        addButton.addActionListener(this.new AddButtonListener());
        editButton.addActionListener(this.new EditButtonListener());
        deleteButton.addActionListener(this.new DeleteButtonListener());
        backButton.addActionListener(this.new BackButtonListener());

        editButton.setEnabled(false);
        deleteButton.setEnabled(false);

        TableHelper.configureToPreferredSettings(medicineTable, 600, 200, buttonsToDisableWithoutTableRowSelection);

        PageDesigner.displayBorderLayoutListPage(this, "Manage Medicines Page", titleLabel, loadPanelButtons, operatePanelButtons, backButton, scrollPane);
    }

    private class LoadButtonListener implements ActionListener {

        JButton[] operatePanelButtonsToDisable;

        public LoadButtonListener(JButton[] operatePanelButtonsToDisable) {
            this.operatePanelButtonsToDisable = operatePanelButtonsToDisable;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ListenerHelper.loadButtonClicked(tableModel, managerUser.getAllMedicinePublicRecords(), operatePanelButtonsToDisable);
        }
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JTextField nameField = new JTextField(15);
            JTextField chargeField = new JTextField(15);

            JLabel nameLabel = new JLabel("Name:");
            JLabel chargeLabel = new JLabel("Charge:");

            JTextField[] textFields = {nameField, chargeField};
            JLabel[] labels = {nameLabel, chargeLabel};

            JPanel panel = ListenerHelper.getCustomizedUserInputPanel(textFields, labels);

            int result = JOptionPane.showConfirmDialog(null, panel, "Add New Medicine", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String name = nameField.getText();
                double charge;
                try {
                    charge = Double.parseDouble(chargeField.getText());
                } catch (RuntimeException exception) {
                    JOptionPane.showMessageDialog(null, "Invalid medicine charge!", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    Medicine newMedicine = new Medicine(name, charge);
                    ManageMedicinesPage.this.managerUser.addMedicine(newMedicine);
                    JOptionPane.showMessageDialog(null, "Successfully added medicine to inventory", "Medicine Created Successfully", JOptionPane.PLAIN_MESSAGE);
                } catch (RuntimeException exception) {
                    JOptionPane.showMessageDialog(null, exception.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private class EditButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Manager managerUser = ManageMedicinesPage.this.managerUser;
            int row = medicineTable.getSelectedRow();
            if (row != -1) {
                String id = (String) tableModel.getValueAt(row, 0);
                String name = (String) tableModel.getValueAt(row, 1);
                double charge = Double.parseDouble((String) tableModel.getValueAt(row, 2));

                String newName = JOptionPane.showInputDialog("New name:", name);
                if (newName == null) {
                    return;
                }
                String newChargeStr = JOptionPane.showInputDialog("New charge:", charge);
                if (newChargeStr == null) {
                    return;
                }
                double newCharge;
                try {
                    newCharge = Double.parseDouble(newChargeStr);
                } catch (RuntimeException exception) {
                    JOptionPane.showMessageDialog(null, "Invalid medicine charge!", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    Medicine newMedicine = new Medicine(id, newName, newCharge);
                    managerUser.updateMedicine(newMedicine);
                    JOptionPane.showMessageDialog(null, "Successfully edited medicine information", "Medicine Updated Successfully", JOptionPane.PLAIN_MESSAGE);
                } catch (RuntimeException exception) {
                    JOptionPane.showMessageDialog(null, exception.getMessage(), "Input error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int row = medicineTable.getSelectedRow();
            if (row != -1) {
                String id = (String) tableModel.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(null, String.format("Are you sure you want to delete the medicine %s from inventory?", id), "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (confirm == JOptionPane.YES_NO_OPTION) {
                    managerUser.removeMedicineById(id);
                    JOptionPane.showMessageDialog(null, "Successfully deleted medicine", "Medicine Deleted Successfully", JOptionPane.PLAIN_MESSAGE);
                }
            }
        }
    }

    private class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new ManagerMainPage(ManageMedicinesPage.this.managerUser));
            ManageMedicinesPage.this.dispose();
        }
    }
}
