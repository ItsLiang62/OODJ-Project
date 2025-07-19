package main;

import database.Database;
import gui.manager.MedicineListPage;
import user.Manager;

import javax.swing.*;

public class MedicalCentre {
    public static void main(String[] args) {
        Manager m = Database.getManager("M002");
        SwingUtilities.invokeLater(() -> new MedicineListPage(m));
    }
}