package gui.manager;

import gui.helper.ListenerHelper;
import gui.helper.PageDesigner;
import gui.helper.TableHelper;
import operation.CustomerFeedback;
import user.Manager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewCustomerFeedbacksPage extends JFrame {
    private final Manager managerUser;
    private final DefaultTableModel tableModel = new DefaultTableModel(CustomerFeedback.getColumnNames(), 0);

    public ViewCustomerFeedbacksPage(Manager managerUser) {
        this.managerUser = managerUser;

        JLabel titleLabel = new JLabel("View Customer Feedbacks");
        JButton loadButton = new JButton("Load");
        JButton backButton = new JButton("Back");
        JTable customerFeedbackTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(customerFeedbackTable);

        loadButton.addActionListener(this.new LoadButtonListener());
        backButton.addActionListener(this.new BackButtonListener());

        TableHelper.configureToPreferredSettings(customerFeedbackTable, 600, 200, null);

        PageDesigner.displayBorderLayoutListPage(this, "View Customer Feedbacks Page", titleLabel, new JButton[] {loadButton}, null, backButton, scrollPane);
    }

    private class LoadButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ListenerHelper.loadButtonClicked(tableModel, managerUser.getAllCustomerFeedbackPublicRecords(), null);
        }
    }

    private class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new ManagerMainPage(managerUser));
            dispose();
        }
    }
}
