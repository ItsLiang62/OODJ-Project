package gui.staff;

import gui.helper.PageDesigner;
import gui.manager.LoginPage;
import user.Staff;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StaffMainPage extends JFrame {
    private Staff staffUser;

    private JLabel titleLabel;
    private final JButton manageCustomersButton = new JButton("Manage Customers");
    private final JButton manageAppointmentsButton = new JButton("Manage Appointments");
    private final JButton viewCustomerFeedbacksButton = new JButton("View All Customer Feedbacks");
    private final JButton backButton = new JButton("Back");;


    public StaffMainPage(Staff staffUser) {
        this.staffUser = staffUser;
        this.titleLabel = new JLabel(String.format("Welcome to Staff Main Page, Mr. %s", staffUser.getName()));

        this.manageCustomersButton.addActionListener(this.new ManageCustomersButtonListener());
        this.manageAppointmentsButton.addActionListener(this.new ManageAppointmentsButtonListener());
        this.backButton.addActionListener(this.new BackButtonListener());

        JButton[] functionalityButtons = {manageCustomersButton, manageAppointmentsButton, viewCustomerFeedbacksButton};

        this.setTitle("Staff Main Page");
        PageDesigner.displayBorderLayoutMainPage(this, titleLabel, functionalityButtons, backButton);
    }


    private class ManageCustomersButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new CustomerListPage(StaffMainPage.this.staffUser));
            StaffMainPage.this.dispose();
        }
    }

    private class ManageAppointmentsButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new AppointmentListPage(StaffMainPage.this.staffUser));
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
