package gui.doctor;

import gui.helper.ListenerHelper;
import gui.helper.PageDesigner;
import user.Doctor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProfilePage extends JFrame {
    private final Doctor doctorUser;

    public ProfilePage(Doctor doctorUser) {
        this.doctorUser = doctorUser;
        
        JLabel titleLabel = new JLabel("My Profile");
        JLabel idLabelShow = new JLabel(doctorUser.getId());
        JTextField nameField = new JTextField(doctorUser.getName(), 15);
        JTextField emailField = new JTextField(doctorUser.getEmail(), 15);
        JTextField passwordField = new JTextField(doctorUser.getPassword(), 15);
        JLabel idLabel = new JLabel("Doctor ID:");
        JLabel nameLabel = new JLabel("Name:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel passwordLabel = new JLabel("Password:");
        JButton saveButton = new JButton("Save");
        JButton backButton = new JButton("Back");
        
        // Reuse your ListenerHelper class for saving edits
        saveButton.addActionListener(
            new ListenerHelper.SaveButtonListener(doctorUser, nameField, emailField, passwordField));

        // Back button returns to Doctor main page
        backButton.addActionListener(this.new BackButtonListener());

        // Layout design (reusing PageDesigner for consistency)
        PageDesigner.displayBorderLayoutProfilePage(
            this,"Doctor Profile Page",titleLabel,
            new JComponent[]{idLabelShow, nameField, emailField, passwordField},
            new JLabel[]{idLabel, nameLabel, emailLabel, passwordLabel},saveButton,backButton);        
        setVisible(true);
        
    }
        private class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new DoctorMainPage(doctorUser).setVisible(true));
            dispose();
        }
    }
}





//package gui.doctor;
//
//import gui.helper.ListenerHelper;
//import gui.helper.PageDesigner;
//import user.Doctor;
//
//import javax.swing.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//public class ProfilePage extends JFrame {
//    private final Doctor doctorUser;
//
//    public ProfilePage(Doctor doctorUser) {
//        this.doctorUser = doctorUser;
//
//        JLabel titleLabel = new JLabel("My Profile");
//        JLabel idLabelShow = new JLabel(doctorUser.getId());
//        JTextField nameField = new JTextField(doctorUser.getName(), 15);
//        JTextField emailField = new JTextField(doctorUser.getEmail(), 15);
//        JTextField passwordField = new JTextField(doctorUser.getPassword(), 15);
//        JLabel idLabel = new JLabel("Doctor ID:");
//        JLabel nameLabel = new JLabel("Name:");
//        JLabel emailLabel = new JLabel("Email:");
//        JLabel passwordLabel = new JLabel("Password:");
//        JButton saveButton = new JButton("Save");
//        JButton backButton = new JButton("Back");
//
//        // Reuse your ListenerHelper class for saving edits
//        saveButton.addActionListener(
//            new ListenerHelper.SaveButtonListener(doctorUser, nameField, emailField, passwordField)
//        );
//
//        // Back button returns to Doctor main page
//        backButton.addActionListener(this.new BackButtonListener());
//
//        // Layout design (reusing PageDesigner for consistency)
//        PageDesigner.displayBorderLayoutProfilePage(
//            this,
//            "Doctor Profile Page",
//            titleLabel,
//            new JComponent[]{idLabelShow, nameField, emailField, passwordField},
//            new JLabel[]{idLabel, nameLabel, emailLabel, passwordLabel},
//            saveButton,
//            backButton
//        );
//    }
//
//    private class BackButtonListener implements ActionListener {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            SwingUtilities.invokeLater(() -> new DoctorMainPage(doctorUser).setVisible(true));
//            dispose();
//        }
//    }
//}






//        setTitle("Edit Profile");
//        setSize(600, 400);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLayout(new BorderLayout());

//        JLabel header = new JLabel("Edit Profile - Dr. " + doctorUser.getName(), SwingConstants.CENTER);
//        add(header, BorderLayout.NORTH);

//        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
//
//        JTextField nameField = new JTextField(doctorUser.getName());
//        JTextField emailField = new JTextField(doctorUser.getEmail());

//        formPanel.add(new JLabel("Name:"));
//        formPanel.add(nameField);
//        formPanel.add(new JLabel("Email:"));
//        formPanel.add(emailField);
//        
//        add(formPanel, BorderLayout.CENTER);
//
//        JButton saveButton = new JButton("Save Profile");
//        add(saveButton, BorderLayout.SOUTH);
