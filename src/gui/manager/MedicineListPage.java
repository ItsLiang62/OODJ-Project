package gui.manager;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import gui.helper.ListenerHelper;
import gui.helper.PageDesigner;
import gui.helper.TableHelper;
import operation.Medicine;
import user.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MedicineListPage extends JFrame {
    private Manager managerUser;

    private final JLabel titleLabel = new JLabel("Manage Medicines");
    private final JButton loadButton = new JButton("Load");
    private final JButton addButton = new JButton("Add");
    private final JButton editButton = new JButton("Edit");
    private final JButton deleteButton = new JButton("Delete");
    private final JButton backButton = new JButton("Back");
    private final DefaultTableModel tableModel = new DefaultTableModel(new String[] {"Medicine ID", "Name", "Charge"}, 0);
    private final JTable medicineTable = new JTable(tableModel);
    private final JScrollPane scrollPane = new JScrollPane(medicineTable);

    public MedicineListPage(Manager managerUser) {
        JButton[] loadPanelButtons = {loadButton};
        JButton[] operatePanelButtons = {addButton, editButton, deleteButton};

        this.managerUser = managerUser;

        TableHelper.configureToPreferredSettings(medicineTable, 600, 200, operatePanelButtons);

        this.loadButton.addActionListener(this.new LoadButtonListener());
        this.addButton.addActionListener(this.new AddButtonListener());
        this.editButton.addActionListener(this.new EditButtonListener());
        this.deleteButton.addActionListener(this.new DeleteButtonListener());
        this.backButton.addActionListener(this.new BackButtonListener());

        this.editButton.setEnabled(false);
        this.deleteButton.setEnabled(false);

        PageDesigner.displayBorderLayoutListPage(this, "Manage Medicines", titleLabel, loadPanelButtons, operatePanelButtons, backButton, scrollPane);
    }

    private List<Object[]> getMedicineRecords() { return TableHelper.asListOfObjectArray(managerUser.getAllMedicinePublicRecords()); }

    private class LoadButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton[] operatePanelButtonsToDisable = {editButton, deleteButton};
            ListenerHelper.loadButtonClicked(tableModel, getMedicineRecords(), operatePanelButtonsToDisable);
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
                    MedicineListPage.this.managerUser.addMedicine(newMedicine);
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
            Manager managerUser = MedicineListPage.this.managerUser;
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
                    managerUser.removeMedicine(id);
                    JOptionPane.showMessageDialog(null, "Successfully deleted medicine", "Medicine Deleted Successfully", JOptionPane.PLAIN_MESSAGE);
                }
            }
        }
    }

    private class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new ManagerMainPage(MedicineListPage.this.managerUser));
            MedicineListPage.this.dispose();
        }
    }
}
