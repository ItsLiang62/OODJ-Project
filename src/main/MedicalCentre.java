package main;

import database.Database;
import gui.manager.MedicineListPage;
import gui.staff.AppointmentListPage;
import gui.staff.CustomerListPage;
import operation.Invoice;
import user.*;

import javax.swing.*;

public class MedicalCentre {
    public static void main(String[] args) {
        Staff s = Database.getStaff("S002");
        SwingUtilities.invokeLater(() -> new AppointmentListPage(s));
    }
}