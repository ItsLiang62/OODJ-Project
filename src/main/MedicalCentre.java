package main;

import customExceptions.RecordAlreadyInDatabaseException;
import database.*;
import operation.*;
import user.*;

import java.io.*;

public class MedicalCentre {
    public static void main(String[] args) throws IOException {
        CustomerFeedback myCustomerFeedback = new CustomerFeedback("C001", "D002", "Too good bro");
        //Database.addCustomerFeedback(myCustomerFeedback);
        CustomerFeedback myCF2 = Database.getCustomerFeedback("F001");
        System.out.println(myCF2.getContent());
        Database.save();
    }
}
