package main;

import customExceptions.RecordAlreadyInDatabaseException;
import database.*;
import operation.*;
import user.*;

import java.io.*;

public class MedicalCentre {
    public static void main(String[] args) throws IOException {
        // Manager
        Manager manager1 = new Manager("Owen", "owen@gmail.com", "8899");
        Manager manager2 = new Manager("Felicia", "felicia@gmail.com", "7788");

        // Staff
        Staff staff1 = new Staff("Iry", "iry@gmail.com", "5566");

        // Doctor

        // Customer
        Customer customer1 = new Customer("Liam", "liam@gmail.com", "5566", 100);

        // Appointment
        Appointment appointment1 = new Appointment("C001");
        appointment1.setStatus("Completed");

        // Medicine
        Medicine medicine1 = new Medicine("Paracetamol", 7);

        // AppointmentMedicine

        // CustomerFeedback
        CustomerFeedback customerFeedback1 = new CustomerFeedback("C001", "S001", "Too bad service");

        // Invoice
        Invoice invoice1 = new Invoice("A001", "Cash", "17/6/2025");
    }
}
