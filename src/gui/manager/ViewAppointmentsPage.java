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

public class ViewAppointmentsPage extends JFrame {
    private final Manager managerUser;
    private final DefaultTableModel tableModel = new DefaultTableModel(Appointment.getColumnNames(), 0);

    public ViewAppointmentsPage(Manager managerUser) {
        this.managerUser = managerUser;

        JLabel titleLabel = new JLabel("View Appointments");
        JButton loadButton = new JButton("Load");
        JButton backButton = new JButton("Back");
        JTable appointmentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(appointmentTable);

        loadButton.addActionListener(new ListenerHelper.LoadButtonListener(tableModel, managerUser::getAllAppointmentPublicRecords, null));
        backButton.addActionListener(this.new BackButtonListener());

        TableHelper.configureToPreferredSettings(appointmentTable, 600, 200, null);

        PageDesigner.displayBorderLayoutListPage(this, "View Appointments Page", titleLabel, new JButton[] {loadButton}, null, backButton, scrollPane);
    }

    private class LoadButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ListenerHelper.loadButtonClicked(tableModel, managerUser.getAllAppointmentPublicRecords(), null);
        }
    }

    private class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new ManagerMainPage(ViewAppointmentsPage.this.managerUser));
            ViewAppointmentsPage.this.dispose();
        }
    }
}
