package user;

import customExceptions.InsufficientApWalletException;
import customExceptions.NegativeValueRejectedException;
import database.*;
import operation.Appointment;
import operation.CustomerFeedback;

import java.util.*;

public class Customer extends User {

    private double apWallet;

    public Customer(String id, String name, String email, String password, double apWallet) {
        super(id, name, email, password);
        checkApWallet(apWallet);
        this.apWallet = apWallet;
        Database.addCustomer(this);
    }

    public Customer(String name, String email, String password, double apWallet) {
        this(Identifiable.createId('C'), name, email, password, apWallet);
    }

    public Set<List<String>> getAllMyAppointmentRecords() {
        Set<List<String>> allMyAppointmentRecords = new LinkedHashSet<>();
        for (String appointmentId: Database.getAllAppointmentIdOfCustomer(this.id)) {
            Appointment appointment = Database.getAppointment(appointmentId);
            List<String> appointmentRecord = appointment.createRecord();
            appointmentRecord.add(String.valueOf(appointment.getTotalCharge()));
            allMyAppointmentRecords.add(appointmentRecord);
        }
        return allMyAppointmentRecords;
    }

    public Set<List<String>> getAllMyCustomerFeedbackRecords() {
        Set<List<String>> allMyCustomerFeedbackRecords = new LinkedHashSet<>();
        for (String customerFeedbackId: Database.getAllCustomerFeedbackIdOfCustomer(this.getId())) {
            allMyCustomerFeedbackRecords.add(Database.getCustomerFeedback(customerFeedbackId).createRecord());
        }
        return allMyCustomerFeedbackRecords;
    }

    public void provideFeedbackToNonManagerEmployee(String nonManagerEmployeeId, String content) {
        new CustomerFeedback(this.id, nonManagerEmployeeId, content);
    }

    public void payWithApWallet(double amount) {
        if (this.apWallet < amount) {
            throw new InsufficientApWalletException("--- Customer does not have enough in ApWallet to pay for appointment ---");
        }
        this.apWallet -= amount;
        Database.removeCustomer(this.id, false);
        Database.addCustomer(this);
    }

    public void topUpApWallet(double amount) {
        this.apWallet += amount;
        Database.removeCustomer(this.id, false);
        Database.addCustomer(this);
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        Database.removeCustomer(this.id, false);
        Database.addCustomer(this);
    }

    @Override
    public void setEmail(String email) {
        super.setEmail(email);
        Database.removeCustomer(this.id, false);
        Database.addCustomer(this);
    }

    @Override
    public void setPassword(String password) {
        super.setPassword(password);
        Database.removeCustomer(this.id, false);
        Database.addCustomer(this);
    }

    public static void checkApWallet(double apWallet) {
        if (apWallet < 0) {
            throw new NegativeValueRejectedException("--- apWallet field of Customer object must be equal or more than 0 ---");
        }
    }

    public List<String> createRecord() {
        String dbId = this.id;
        String dbName = this.name;
        String dbEmail = this.email;
        String dbPassword = this.password;
        String dbApWallet = String.valueOf(this.apWallet);

        return new ArrayList<>(Arrays.asList(
                dbId, dbName, dbEmail, dbPassword, dbApWallet
        ));
    }

    public static void createCustomerFromRecord(List<String> record) {
        String customerId = record.getFirst();
        String customerName = record.get(1);
        String customerEmail = record.get(2);
        String customerPassword = record.get(3);
        double customerApWallet = Double.parseDouble(record.getLast());

        new Customer(customerId, customerName, customerEmail, customerPassword, customerApWallet);
    }
}
