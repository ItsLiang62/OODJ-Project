package main;

import database.*;
import operation.*;
import user.*;

public class MedicalCentre {
    public static void main(String[] args) {
        Manager manager1 = Database.getManager("M002");
        System.out.println(manager1.getAllAppointmentRecords());
    }
}
