package gui.doctor;

import gui.helper.PageDesigner;
import gui.manager.LoginPage;
import user.Doctor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;

public class DoctorMainPage extends JFrame {
    private final Doctor doctorUser;

    public DoctorMainPage(Doctor doctorUser) {
        this.doctorUser = doctorUser;

        JLabel titleLabel = new JLabel(String.format("Welcome to Doctor Main Page, Dr. %s", doctorUser.getName()));
        titleLabel.setFont(new Font("Serif", Font.BOLD, 26));

        JButton editProfileButton = new JButton("Edit Profile");
        JButton manageAppointmentsButton = new JButton("Manage Appointments");
        JButton viewMedicineButton = new JButton("View Medicine");
        JButton prescriptionButton = new JButton("Manage Prescription");
        JButton viewCustomerFeedbackButton = new JButton("View Customer Feedback");
        JButton backButton = new JButton("Back");

        editProfileButton.addActionListener(new EditProfileButtonListener());
        manageAppointmentsButton.addActionListener(new ManageAppointmentsButtonListener());
        viewMedicineButton.addActionListener(new ViewMedicineButtonListener());
        prescriptionButton.addActionListener(new PrescriptionButtonListener());
        viewCustomerFeedbackButton.addActionListener(new ViewCustomerFeedbackButtonListener());
        backButton.addActionListener(new BackButtonListener());

        JButton[] functionalityButtons = {editProfileButton,manageAppointmentsButton,viewMedicineButton,prescriptionButton,viewCustomerFeedbackButton};

        PageDesigner.displayBorderLayoutMainPage(this,"Doctor Main Page",titleLabel,functionalityButtons,backButton);
        setVisible(true);
    }

    private class EditProfileButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new ProfilePage(doctorUser));
            dispose();
        }
    }

    private class ManageAppointmentsButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new DoctorAppointmentsPage(doctorUser));
            dispose();
        }
    }
    
    private class ViewMedicineButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new DoctorMedicine(doctorUser));
            dispose();
        }
    }
    
    private class PrescriptionButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new DoctorPrescription(doctorUser));
            dispose();
        }
    }

    private class ViewCustomerFeedbackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new DoctorCustomerFeedback(doctorUser));
            dispose();
        }
    }

    private class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(LoginPage::new);
            dispose();
        }
    }
}

