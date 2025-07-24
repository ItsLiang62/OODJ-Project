package gui.manager;

import gui.helper.ListenerHelper;
import gui.helper.PageDesigner;
import gui.helper.TableHelper;
import operation.Appointment;
import user.Manager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AppointmentListPage extends JFrame {
    private Manager managerUser;

    private final JLabel titleLabel = new JLabel("View All Appointments");
    private final DefaultTableModel tableModel = new DefaultTableModel(Appointment.getColumnNames(), 0);
    private final JTable appointmentTable = new JTable(tableModel);
    private final JScrollPane scrollPane = new JScrollPane(appointmentTable);
    private final JButton loadButton = new JButton("Load");
    private final JButton backButton = new JButton("Back");

    public AppointmentListPage(Manager managerUser) {
        this.managerUser = managerUser;

        this.loadButton.addActionListener(this.new LoadButtonListener());
        this.backButton.addActionListener(this.new BackButtonListener());

        JButton[] loadPanelButtons = {loadButton};
        TableHelper.configureToPreferredSettings(appointmentTable, 600, 200, null);
        PageDesigner.displayBorderLayoutListPage(this, "View All Appointments", titleLabel, loadPanelButtons, null, backButton, scrollPane);
    }

    private class LoadButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ListenerHelper.loadButtonClicked(tableModel, TableHelper.asListOfObjectArray(managerUser.getAllAppointmentPublicRecords()), null);
        }
    }

    private class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new ManagerMainPage(AppointmentListPage.this.managerUser));
            AppointmentListPage.this.dispose();
        }
    }
}
