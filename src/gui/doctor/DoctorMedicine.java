package gui.doctor;

import gui.helper.ListenerHelper;
import gui.helper.PageDesigner;
import gui.helper.TableHelper;
import operation.Medicine;
import user.Doctor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DoctorMedicine extends JFrame {
    private final Doctor doctorUser;

    public DoctorMedicine(Doctor doctorUser) {
        this.doctorUser = doctorUser;

        JLabel titleLabel = new JLabel("View Medicine");
        JButton loadButton = new JButton("Load");
        JButton backButton = new JButton("Back");
        DefaultTableModel tableModel = new DefaultTableModel(Medicine.getColumnNames(), 0);
        JTable medicineTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(medicineTable);

        loadButton.addActionListener(new ListenerHelper.LoadButtonListener(tableModel, doctorUser::getMedicineRecords, null));
        backButton.addActionListener(this.new BackButtonListener());

        TableHelper.configureToPreferredSettings(medicineTable, 600, 200, null);

        PageDesigner.displayBorderLayoutListPage(this, "View Medicine Page", titleLabel, new JButton[] {loadButton}, null, backButton, scrollPane);
    }

    private class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new DoctorMainPage(DoctorMedicine.this.doctorUser));
            DoctorMedicine.this.dispose();
        }
    }
}

