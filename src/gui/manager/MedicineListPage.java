package gui.manager;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import user.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MedicineListPage extends JFrame {
    private Manager managerUser;

    private final JLabel title = new JLabel("Manage Medicine");
    private final JButton loadButton = new JButton("Load");
    private final JButton addButton = new JButton("Add");
    private final JButton editButton = new JButton("Edit");
    private final JButton deleteButton = new JButton("Delete");
    private final JButton backButton = new JButton("Back");
    private final DefaultTableModel tableModel = new DefaultTableModel(new String[] {"Medicine ID", "Name", "Charge"}, 0);
    private final JTable medicineTable = new JTable(tableModel);
    private final JScrollPane scrollPane = new JScrollPane(medicineTable);

    public MedicineListPage(Manager managerUser) {
        this.managerUser = managerUser;

        this.medicineTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.medicineTable.getSelectionModel().addListSelectionListener(this.new TableRowSelectionListener());
        this.medicineTable.setPreferredScrollableViewportSize(new Dimension(600, 200));
        this.medicineTable.setFillsViewportHeight(true);

        setTitle("Medicines List Page");
        setSize(700, 600);
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
        JPanel loadPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        gbc.gridy = 0;
        northPanel.add(backPanel, gbc);
        gbc.gridy = 1;
        northPanel.add(titlePanel, gbc);
        gbc.gridy = 2;
        northPanel.add(loadPanel, gbc);

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

        // Add components to loadPanel (FlowLayout)
        this.loadButton.addActionListener(this.new LoadButtonListener());
        loadPanel.add(this.loadButton);

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

    private Object[][] getMedicineRecords() {
        List<Object[]> allObjects = new ArrayList<>();
        for (List<String> objectRecord: managerUser.getAllMedicinePublicRecords()) {
            allObjects.add(objectRecord.toArray()); // String array is Object array
        }
        return allObjects.toArray(new Object[0][]);
    }

    private class TableRowSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                boolean hasSelectedRow = MedicineListPage.this.medicineTable.getSelectedRow() != -1;
                MedicineListPage.this.editButton.setEnabled(hasSelectedRow);
                MedicineListPage.this.deleteButton.setEnabled(hasSelectedRow);
            }
        }
    }

    private class LoadButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            MedicineListPage.this.tableModel.setRowCount(0);
            for (Object[] medicineRecord: MedicineListPage.this.getMedicineRecords()) {
                MedicineListPage.this.tableModel.addRow(medicineRecord);
            }
            MedicineListPage.this.editButton.setEnabled(false);
            MedicineListPage.this.deleteButton.setEnabled(false);
        }
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JTextField nameField = new JTextField(15);
            JTextField chargeField = new JTextField(15);

            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);

            gbc.anchor = GridBagConstraints.EAST;
            gbc.gridx = 0;

            gbc.gridy = 0;
            panel.add(new JLabel("Name:"), gbc);
            gbc.gridy = 1;
            panel.add(new JLabel("Charge:"), gbc);

            gbc.anchor = GridBagConstraints.CENTER;
            gbc.gridx = 1;

            gbc.gridy = 0;
            panel.add(nameField, gbc);
            gbc.gridy = 1;
            panel.add(chargeField, gbc);

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
                    MedicineListPage.this.managerUser.addMedicine(name, charge);
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
                    managerUser.updateMedicine(id, newName, newCharge);
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
