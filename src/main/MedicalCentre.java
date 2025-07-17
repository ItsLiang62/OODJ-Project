package main;

import gui.LoginPage;

import javax.swing.*;

public class MedicalCentre {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginPage::new);
    }
}