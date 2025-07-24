package gui.manager;

import gui.helper.PageDesigner;
import gui.helper.TableHelper;
import operation.Appointment;
import user.Manager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AppointmentListPage extends JFrame {
    private Manager managerUser;

    private final JLabel titleLabel = new JLabel("View All Appointments");
    private final DefaultTableModel tableModel = new DefaultTableModel(Appointment.getColumnNames(), 0);
    private final JTable appointmentTable = new JTable(tableModel);
    private final JScrollPane scrollPane = new JScrollPane(appointmentTable);
    private final JButton backButton = new JButton("Back");

    public AppointmentListPage(Manager managerUser) {
        this.managerUser = managerUser;

        TableHelper.configureToPreferredSettings(appointmentTable, 600, 200, null);
        PageDesigner.displayBorderLayoutListPage(this, "View All Appointments", titleLabel, null, null, backButton, scrollPane);
    }
}
