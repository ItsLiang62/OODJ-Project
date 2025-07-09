package main;

import customExceptions.RecordAlreadyInDatabaseException;
import database.*;
import operation.*;
import user.*;

import java.io.*;

public class MedicalCentre {
    public static void main(String[] args) throws IOException {
        /*
        Customer myCustomer1 = new Customer("C001", "Owen", "owen@email.com", "123");
        Customer myCustomer2 = new Customer("C002", "Owen", "owen@email.com", "123");
        try {
            Database.addUser(myCustomer2);
            Database.addUser(myCustomer1);
        } catch (RecordAlreadyInDatabaseException e) {
            System.out.println("user exist");
        }
        Database.save();
        Account.save();
        */

        Customer myCustomer3 = (Customer) Database.getUser("C002");
        System.out.println(myCustomer3.getPassword());
    }
}
