package gui.manager;

import gui.helper.PageDesigner;
import user.Manager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManagerMainPage extends JFrame {
    private final Manager managerUser;

    public ManagerMainPage(Manager managerUser) {
        this.managerUser = managerUser;

        JLabel titleLabel = new JLabel(String.format("Welcome to Manager Main Page, Mr. %s", managerUser.getName()));
        JButton manageEmployeesButton = new JButton("Manage Employees");
        JButton viewAppointmentsButton = new JButton("View All Appointments");
        JButton viewCustomerFeedbacksButton = new JButton("View All Customer Feedbacks");
        JButton manageMedicinesButton = new JButton("Manage Medicines");
        JButton manageReportsButton = new JButton("Manage Reports");
        JButton myProfileButton = new JButton("My Profile");
        JButton backButton = new JButton("Back");

        manageEmployeesButton.addActionListener(this.new ManageEmployeesButtonListener());
        viewAppointmentsButton.addActionListener(this.new ViewAppointmentsButtonListener());
        viewCustomerFeedbacksButton.addActionListener(this.new ViewCustomerFeedbacksButtonListener());
        manageMedicinesButton.addActionListener(this.new ManageMedicinesButtonListener());
        manageReportsButton.addActionListener(this.new ManageReportsButtonListener());
        myProfileButton.addActionListener(this.new MyProfileButtonListener());
        backButton.addActionListener(this.new BackButtonListener());

        JButton[] functionalityButtons = {manageEmployeesButton, viewAppointmentsButton, viewCustomerFeedbacksButton, manageMedicinesButton, manageReportsButton, myProfileButton};

        PageDesigner.displayBorderLayoutMainPage(this, "Manager Main Page", titleLabel, functionalityButtons, backButton);
    }


    private class ManageEmployeesButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new ManageEmployeesPage(managerUser));
            dispose();
        }
    }

    private class ViewAppointmentsButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new ViewAppointmentsPage(managerUser));
            dispose();
        }
    }

    private class ViewCustomerFeedbacksButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new ViewCustomerFeedbacksPage(managerUser));
            dispose();
        }
    }

    private class ManageMedicinesButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new ManageMedicinesPage(managerUser));
            dispose();
        }
    }

    private class ManageReportsButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new ManageReportsPage(managerUser));
            dispose();
        }
    }

    private class MyProfileButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new ProfilePage(managerUser));
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