package gui.staff;

import gui.helper.ListenerHelper;
import gui.helper.PageDesigner;
import gui.helper.TableHelper;
import operation.CustomerFeedback;
import user.Staff;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewMyCustomerFeedbacksPage extends JFrame {

    private final Staff staffUser;

    public ViewMyCustomerFeedbacksPage(Staff staffUser) {

        this.staffUser = staffUser;
        JLabel titleLabel = new JLabel("View My Customer Feedbacks");
        JButton loadButton = new JButton("Load");
        JButton backButton = new JButton("Back");
        DefaultTableModel tableModel = new DefaultTableModel(CustomerFeedback.getColumnNames(), 0);
        JTable customerFeedbackTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(customerFeedbackTable);

        loadButton.addActionListener(new ListenerHelper.LoadButtonListener(tableModel, staffUser::getAllMyCustomerFeedbackRecords, null));
        backButton.addActionListener(this.new BackButtonListener());

        TableHelper.configureToPreferredSettings(customerFeedbackTable, 600, 200, null);

        PageDesigner.displayBorderLayoutListPage(this, "View My Customer Feedbacks Page", titleLabel, new JButton[] {loadButton}, null, backButton, scrollPane);
    }

    private class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new StaffMainPage(staffUser));
            dispose();
        }
    }
}