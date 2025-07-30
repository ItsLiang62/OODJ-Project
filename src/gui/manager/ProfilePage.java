package gui.manager;

import gui.helper.PageDesigner;
import user.Manager;

import javax.swing.*;

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

        PageDesigner.displayBorderLayoutProfilePage(this, "Profile Page", titleLabel, new JComponent[] {idLabelShow, nameField, emailField, passwordField}, new JLabel[] {idLabel, nameLabel, emailLabel, passwordLabel}, saveButton, backButton);
    }
}
