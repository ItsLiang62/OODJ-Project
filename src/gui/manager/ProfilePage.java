package gui.manager;

import gui.helper.ListenerHelper;
import gui.helper.PageDesigner;
import user.Manager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProfilePage extends JFrame {
    private final Manager managerUser;

    public ProfilePage(Manager managerUser) {
        this.managerUser = managerUser;

        JLabel titleLabel = new JLabel("My Profile");
        JLabel idLabelShow = new JLabel(managerUser.getId());
        JTextField nameField = new JTextField(managerUser.getName(), 15);
        JTextField emailField = new JTextField(managerUser.getEmail(), 15);
        JTextField passwordField = new JTextField(managerUser.getPassword(), 15);
        JLabel idLabel = new JLabel("Manager ID:");
        JLabel nameLabel = new JLabel("Name:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel passwordLabel = new JLabel("Password:");
        JButton saveButton = new JButton("Save");
        JButton backButton = new JButton("Back");

        saveButton.addActionListener(new ListenerHelper.SaveButtonListener(managerUser, nameField, emailField, passwordField));
        backButton.addActionListener(this.new BackButtonListener());

        PageDesigner.displayBorderLayoutProfilePage(this, "Profile Page", titleLabel, new JComponent[] {idLabelShow, nameField, emailField, passwordField}, new JLabel[] {idLabel, nameLabel, emailLabel, passwordLabel}, saveButton, backButton);
    }

    private class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> new ManagerMainPage(managerUser));
            dispose();
        }
    }
}
