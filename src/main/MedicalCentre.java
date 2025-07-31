package main;

import gui.manager.LoginPage;

import javax.swing.*;

public class MedicalCentre {
    // For the very first system run, please log in as root manager using root@email.com and 123 as password
    // If you have an account registered by a manager, your password is exactly as your email if you have not changed your password
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginPage::new);
    }
}