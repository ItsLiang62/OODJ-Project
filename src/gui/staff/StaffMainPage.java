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
        JButton viewMyCustomerFeedbacksButton = new JButton("View My Customer Feedbacks");
        JButton viewInvoicesButton = new JButton("View Invoices");
        JButton myProfileButton = new JButton("My Profile");
        JButton backButton = new JButton("Back");

        manageCustomersButton.addActionListener(this.new ManageCustomersButtonListener());
        manageAppointmentsButton.addActionListener(this.new ManageAppointmentsButtonListener());
        viewMyCustomerFeedbacksButton.addActionListener(this.new ViewMyCustomerFeedbacksButtonListener());
        viewInvoicesButton.addActionListener(this.new ViewInvoicesButtonListener());
        backButton.addActionListener(this.new BackButtonListener());
        myProfileButton.addActionListener(this.new MyProfileButtonListener());

        JButton[] functionalityButtons = {manageCustomersButton, manageAppointmentsButton, viewMyCustomerFeedbacksButton, viewInvoicesButton, myProfileButton};

        PageDesigner.displayBorderLayoutMainPage(this, "Staff Main Page", titleLabel, functionalityButtons, backButton);
    }

    private class ManageCustomersButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new ManageCustomersPage(staffUser));
            dispose();
        }
    }

    private class ManageAppointmentsButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new ManageAppointmentsPage(staffUser));
            dispose();
        }
    }

    private class ViewMyCustomerFeedbacksButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new ViewMyCustomerFeedbacksPage(staffUser));
            dispose();
        }
    }

    private class ViewInvoicesButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new ViewInvoicesPage(staffUser));
            dispose();
        }
    }

    private class MyProfileButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new ProfilePage(staffUser));
            dispose();
        }
    }

    private class BackButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(LoginPage::new);
            dispose();
        }
    }
}
