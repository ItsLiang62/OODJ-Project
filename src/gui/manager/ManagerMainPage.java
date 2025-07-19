package gui.manager;

import user.Manager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManagerMainPage extends JFrame {
    private Manager managerUser;

    private JLabel title;
    private JButton manageEmployeesButton, viewAppointmentsButton, viewCustomerFeedbacksButton, manageReportsButton, manageMedicinesButton, backButton;

    public ManagerMainPage(Manager managerUser) {
        this.managerUser = managerUser;

        this.title = new JLabel(String.format("Welcome to Manager Main Page, Mr. %s", managerUser.getName()));
        this.manageEmployeesButton = new JButton("Manager Employees");
        this.viewAppointmentsButton = new JButton("View All Appointments");
        this.viewCustomerFeedbacksButton = new JButton("View All Customer Feedbacks");
        this.manageReportsButton = new JButton("Manage Reports");
        this.manageMedicinesButton = new JButton("Manage Medicines");
        this.backButton = new JButton("Back");

        this.setTitle("Manager Main Page");
        this.setSize(700, 500);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GridBagConstraints gbc = new GridBagConstraints();

        // Adding panels to ManagerMainPage (BorderLayout)
        JPanel northPanel = new JPanel(new GridBagLayout());
        JPanel centerPanel = new JPanel(new GridBagLayout());

        this.add(northPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);

        // Adding panels to northPanel (GridBagLayout)
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;

        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        gbc.gridy = 0;
        northPanel.add(backPanel, gbc);
        gbc.gridy = 1;
        northPanel.add(titlePanel, gbc);

        // Adding components to centerPanel (GridBagLayout)
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;

        gbc.gridy = 0;
        this.manageEmployeesButton.addActionListener(this.new ManageEmployeesButtonListener());
        centerPanel.add(this.manageEmployeesButton, gbc);
        gbc.gridy = 1;
        centerPanel.add(this.viewAppointmentsButton, gbc);
        gbc.gridy = 2;
        centerPanel.add(this.viewCustomerFeedbacksButton, gbc);
        gbc.gridy = 3;
        centerPanel.add(this.manageReportsButton, gbc);
        gbc.gridy = 4;
        centerPanel.add(this.manageMedicinesButton, gbc);

        // Add components to backPanel
        backButton.addActionListener(this.new BackButtonListener());
        backPanel.add(this.backButton);

        // Add components to titlePanel
        titlePanel.add(this.title);

        this.setVisible(true);
    }


    private class ManageEmployeesButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new EmployeeListPage(ManagerMainPage.this.managerUser));
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
