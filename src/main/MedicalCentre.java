package main;

import gui.helper.ListenerHelper;
import gui.manager.LoginPage;
import operation.Invoice;
import user.Manager;

import javax.swing.*;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MedicalCentre {
    // For the very first system run, please log in as root manager using root@email.com and 123 as password
    // If you have an account registered by a manager, your password is exactly as your email if you have not changed your password
    public static void main(String[] args) {
        //List<Invoice> allInvoicesOfMonth = new Manager("Me", "me@email.com").getAllInvoicesOfMonth(Month.AUGUST);
        //List<Double> allAppointmentRevenueOfMonth = allInvoicesOfMonth.stream().map(Invoice::getTotalAmount).toList();
        SwingUtilities.invokeLater(LoginPage::new);
    }
}