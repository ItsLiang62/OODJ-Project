package gui.staff;

import gui.helper.PageDesigner;
import gui.manager.LoginPage;
import user.Staff;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StaffMainPage extends JFrame {
    private final Staff staffUser;

    public StaffMainPage(Staff staffUser) {
        this.staffUser = staffUser;

        JLabel titleLabel = new JLabel(String.format("Welcome to Staff Main Page, Mr. %s", staffUser.getName()));
        JButton manageCustomersButton = new JButton("Manage Customers");
        JButton manageAppointmentsButton = new JButton("Manage Appointments");
        JButton viewCustomerFeedbacksButton = new JButton("View Customer Feedbacks");
        JButton backButton = new JButton("Back");

        manageCustomersButton.addActionListener(this.new ManageCustomersButtonListener());
        manageAppointmentsButton.addActionListener(this.new ManageAppointmentsButtonListener());
        backButton.addActionListener(this.new BackButtonListener());

        JButton[] functionalityButtons = {manageCustomersButton, manageAppointmentsButton, viewCustomerFeedbacksButton};

        PageDesigner.displayBorderLayoutMainPage(this, "Staff Main Page", titleLabel, functionalityButtons, backButton);
    }

    private class ManageCustomersButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new ManageCustomersPage(StaffMainPage.this.staffUser));
            StaffMainPage.this.dispose();
        }
    }

    private class ManageAppointmentsButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new ManageAppointmentsPage(StaffMainPage.this.staffUser));
            StaffMainPage.this.dispose();
        }
    }

    private class BackButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(LoginPage::new);
            StaffMainPage.this.dispose();
        }
    }
}
