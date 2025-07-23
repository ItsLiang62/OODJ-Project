package gui.manager;

import gui.helper.PageDesigner;
import user.Manager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManagerMainPage extends JFrame {
    private Manager managerUser;

    private JLabel titleLabel;
    private final JButton manageEmployeesButton = new JButton("Manage Employees");
    private final JButton viewAppointmentsButton = new JButton("View All Appointments");
    private final JButton viewCustomerFeedbacksButton = new JButton("View All Customer Feedbacks");
    private final JButton manageReportsButton = new JButton("Manage Reports");
    private final JButton manageMedicinesButton = new JButton("Manage Medicines");
    private final JButton backButton = new JButton("Back");;


    public ManagerMainPage(Manager managerUser) {
        this.managerUser = managerUser;
        this.titleLabel = new JLabel(String.format("Welcome to Manager Main Page, Mr. %s", managerUser.getName()));

        this.manageEmployeesButton.addActionListener(this.new ManageEmployeesButtonListener());
        this.manageMedicinesButton.addActionListener(this.new ManageMedicinesButtonListener());
        this.backButton.addActionListener(this.new BackButtonListener());

        JButton[] functionalityButtons = {manageEmployeesButton, viewAppointmentsButton, viewCustomerFeedbacksButton, manageMedicinesButton, manageReportsButton};

        this.setTitle("Manager Main Page");
        PageDesigner.displayBorderLayoutMainPage(this, titleLabel, functionalityButtons, backButton);
    }


    private class ManageEmployeesButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new EmployeeListPage(ManagerMainPage.this.managerUser));
            ManagerMainPage.this.dispose();
        }
    }

    private class ManageMedicinesButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new MedicineListPage(ManagerMainPage.this.managerUser));
            ManagerMainPage.this.dispose();
        }
    }

    private class BackButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(LoginPage::new);
            ManagerMainPage.this.dispose();
        }
    }
}