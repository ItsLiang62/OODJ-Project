package gui.staff;

import gui.helper.ListenerHelper;
import gui.helper.PageDesigner;
import gui.helper.TableHelper;
import operation.Invoice;
import user.Staff;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewInvoicesPage extends JFrame {

    private final Staff staffUser;

    public ViewInvoicesPage(Staff staffUser) {

        this.staffUser = staffUser;
        JLabel titleLabel = new JLabel("View Invoices");
        JButton loadButton = new JButton("Load");
        JButton backButton = new JButton("Back");
        DefaultTableModel tableModel = new DefaultTableModel(Invoice.getColumnNames(), 0);
        JTable invoiceTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(invoiceTable);

        loadButton.addActionListener(new ListenerHelper.LoadButtonListener(tableModel, staffUser::getInvoicePublicRecords, null));
        backButton.addActionListener(this.new BackButtonListener());

        TableHelper.configureToPreferredSettings(invoiceTable, 600, 200, null);

        PageDesigner.displayBorderLayoutListPage(this, "View Invoices Page", titleLabel, new JButton[] {loadButton}, null, backButton, scrollPane);
    }

    private class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new StaffMainPage(staffUser));
            dispose();
        }
    }
}