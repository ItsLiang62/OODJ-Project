package gui.helper;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.*;
import java.util.List;

public final class TableHelper {
    public static <T extends Collection<String>> List<Object[]> asListOfObjectArray(Collection<T> records) {
        List<Object[]> listOfObjectArray = new ArrayList<Object[]>();
        for (Collection<String> record: records) {
            listOfObjectArray.add(record.toArray());
        }
        return listOfObjectArray;
    }

    public static void configureToPreferredSettings(JTable table, int width, int height, JButton[] buttonsToDisable) {
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(new TableHelper.TableRowSelectionListener(table, buttonsToDisable));
        table.setPreferredScrollableViewportSize(new Dimension(width, height));
        table.setFillsViewportHeight(true);
    }

    // Do not need a TableHelper instance to instantiate this nested class
    private static class TableRowSelectionListener implements ListSelectionListener {
        public JButton[] buttonsToDisable;
        public JTable table;

        public TableRowSelectionListener(JTable table, JButton[] buttonsToDisable) {
            this.table = table;
            this.buttonsToDisable = buttonsToDisable;
        }
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                boolean hasSelectedRow = this.table.getSelectedRow() != -1;
                if (this.buttonsToDisable != null) {
                    Arrays.stream(this.buttonsToDisable).forEach(button -> button.setEnabled(hasSelectedRow));
                }
            }
        }
    }
}
