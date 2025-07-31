package gui.staff;

import gui.helper.ListenerHelper;
import gui.helper.PageDesigner;
import user.Staff;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProfilePage extends JFrame {
    private final Staff staffUser;

    public ProfilePage(Staff staffUser) {
        this.staffUser = staffUser;

        JLabel titleLabel = new JLabel("My Profile");
        JLabel idLabelShow = new JLabel(staffUser.getId());
        JTextField nameField = new JTextField(staffUser.getName(), 15);
        JTextField emailField = new JTextField(staffUser.getEmail(), 15);
        JTextField passwordField = new JTextField(staffUser.getPassword(), 15);
        JLabel idLabel = new JLabel("Staff ID:");
        JLabel nameLabel = new JLabel("Name:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel passwordLabel = new JLabel("Password:");
        JButton saveButton = new JButton("Save");
        JButton backButton = new JButton("Back");

        saveButton.addActionListener(new ListenerHelper.SaveButtonListener(staffUser, nameField, emailField, passwordField));
        backButton.addActionListener(this.new BackButtonListener());

        PageDesigner.displayBorderLayoutProfilePage(this, "Profile Page", titleLabel, new JComponent[] {idLabelShow, nameField, emailField, passwordField}, new JLabel[] {idLabel, nameLabel, emailLabel, passwordLabel}, saveButton, backButton);
    }

    private class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new StaffMainPage(staffUser));
            dispose();
        }
    }
}
