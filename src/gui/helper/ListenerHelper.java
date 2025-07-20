package gui.helper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.Collection;

public final class ListenerHelper {
    public static void loadButtonClicked(DefaultTableModel tableModel, Collection<Object[]> records, JButton[] operatePanelButtonsToDisable) {
        tableModel.setRowCount(0);
        for (Object[] record: records) {
            tableModel.addRow(record);
        }
        Arrays.stream(operatePanelButtonsToDisable).forEach(button -> button.setEnabled(false));
    }

    public static JPanel getCustomUserInputPanel(Component[] inputFields, JLabel[] labels) {

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
}
