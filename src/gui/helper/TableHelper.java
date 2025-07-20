package gui.helper;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public final class TableHelper {
    public static <T extends Collection<String>> List<Object[]> asListOfObjectArray(Collection<T> records) {
        List<Object[]> listOfObjectArray = new ArrayList<Object[]>();
        for (Collection<String> record: records) {
            listOfObjectArray.add(record.toArray());
        }
        return listOfObjectArray;
    }

    public static void configureToRecommendedSettings(JTable table, ListSelectionListener listSelectionListener) {
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(listSelectionListener);
        table.setPreferredScrollableViewportSize(new Dimension(600, 200));
        table.setFillsViewportHeight(true);
    }
}
