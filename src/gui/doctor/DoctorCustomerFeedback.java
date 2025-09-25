package gui.doctor;

import gui.helper.PageDesigner;
import gui.helper.TableHelper;
import user.Doctor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class DoctorCustomerFeedback extends JFrame {
    private final Doctor doctorUser;

    public DoctorCustomerFeedback(Doctor doctorUser) {
        this.doctorUser = doctorUser;

        JLabel titleLabel = new JLabel("Customer Feedback");
        JButton loadButton = new JButton("Load");
        JButton backButton = new JButton("Back");

        String[] columnNames = { "Feedback ID", "Customer ID", "Doctor ID", "Feedback" };
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable feedbackTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(feedbackTable);

        TableHelper.configureToPreferredSettings(feedbackTable, 600, 200, null);

        loadButton.addActionListener(e -> {
            tableModel.setRowCount(0);
            List<List<String>> records = doctorUser.getMyCustomerFeedbackRecords();
            for (List<String> rec : records) {tableModel.addRow(rec.toArray());}
        });

        backButton.addActionListener(new BackButtonListener());

        PageDesigner.displayBorderLayoutListPage(this,"View Customer Feedback Page",titleLabel,new JButton[] { loadButton },null,backButton,scrollPane);
    }

    private class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new DoctorMainPage(doctorUser));
            DoctorCustomerFeedback.this.dispose();
        }
    }
}
