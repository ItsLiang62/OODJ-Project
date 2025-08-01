package gui.helper;

import operation.Invoice;
import user.Manager;
import user.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public final class ListenerHelper {
    public static <T extends Collection<String>> void loadButtonClicked(DefaultTableModel tableModel, Collection<T> records, JButton[] buttonsToDisable) {
        tableModel.setRowCount(0);
        for (Object[] record: TableHelper.asListOfObjectArray(records)) {
            tableModel.addRow(record);
        }
        if (buttonsToDisable != null) {
            Arrays.stream(buttonsToDisable).forEach(button -> button.setEnabled(false));
        }
    }

    public static JPanel getCustomizedUserInputPanel(Component[] inputFields, JLabel[] labels) {

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;

        gbc.gridy = 0;
        for (JLabel label : labels) {
            panel.add(label, gbc);
            gbc.gridy ++;
        }

        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 1;

        gbc.gridy = 0;
        for (Component inputField: inputFields) {
            panel.add(inputField, gbc);
            gbc.gridy ++;
        }

        return panel;
    }

    public static class SaveButtonListener implements ActionListener {
        User user;
        JTextField nameField;
        JTextField emailField;
        JTextField passwordField;

        public SaveButtonListener(User user, JTextField nameField, JTextField emailField, JTextField passwordField) {
            this.user = user;
            this.nameField = nameField;
            this.emailField = emailField;
            this.passwordField = passwordField;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            try {
                user.setName(name);
                user.setEmail(email);
                user.setPassword(password);
            } catch (RuntimeException exception) {
                JOptionPane.showMessageDialog(null, exception.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(null, "Successfully updated profile!", "Profile Updated Successfully", JOptionPane.PLAIN_MESSAGE);
        }
    }

    public class GenerateReportButtonListener implements ActionListener {
        Manager managerUser;
        Month month;

        public GenerateReportButtonListener(Manager managerUser, Month month) {
            this.managerUser = managerUser;
            this.month = month;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            // Filter invoice records to this month only
            List<Invoice> allInvoicesOfMonth = managerUser.getAllInvoicesOfMonth(month);

            // Calculate statistics
            double monthlyRevenue = 0;
            double numAppointments = allInvoicesOfMonth.size();
            double avgAppointmentRevenue;

            for (Invoice invoice: allInvoicesOfMonth) {
                monthlyRevenue += invoice.getTotalAmount();
            }
            avgAppointmentRevenue = monthlyRevenue / numAppointments;
        }
    }
}
