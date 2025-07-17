package main;

import customExceptions.RecordAlreadyInDatabaseException;
import database.*;
import gui.LoginPage;
import operation.*;
import user.*;

import javax.swing.*;
import java.io.*;

public class MedicalCentre {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginPage::new);
    }
}
