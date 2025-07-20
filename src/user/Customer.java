package user;

import customExceptions.AppointmentCompletedException;
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
    }

    public Customer(String name, String email) {
        this(IdCreator.createId('C'), name, email, email, 0);
    }

    public double getApWallet() { return this.apWallet; }

    public Set<List<String>> getAllMyAppointmentRecords() {
        Set<List<String>> allMyAppointmentRecords = new LinkedHashSet<>();
        for (String appointmentId: Database.getAllAppointmentIdOfCustomer(this.id)) {
            Appointment appointment = Database.getAppointment(appointmentId);
            List<String> appointmentRecord = appointment.createDbRecord();
            appointmentRecord.add(String.valueOf(appointment.getTotalCharge()));
            allMyAppointmentRecords.add(appointmentRecord);
        }
        return allMyAppointmentRecords;
    }

    public Set<List<String>> getAllMyCustomerFeedbackRecords() {
        Set<List<String>> allMyCustomerFeedbackRecords = new LinkedHashSet<>();
        for (String customerFeedbackId: Database.getAllCustomerFeedbackIdOfCustomer(this.getId())) {
            allMyCustomerFeedbackRecords.add(Database.getCustomerFeedback(customerFeedbackId).createDbRecord());
        }
        return allMyCustomerFeedbackRecords;
    }

    public Set<List<String>> getAllNonManagerEmployeeRecords() {
        Set<List<String>> allNonManagerEmployeeRecords = new LinkedHashSet<>();
        for (String staffId: Database.getAllStaffId()) {
            List<String> staffRecords = Database.getStaff(staffId).createDbRecord();
            staffRecords.removeLast();
            staffRecords.removeLast();
            staffRecords.add("Staff");
            allNonManagerEmployeeRecords.add(staffRecords);
        }
        for (String doctorId: Database.getAllDoctorId()) {
            List<String> doctorRecords = Database.getDoctor(doctorId).createDbRecord();
            doctorRecords.removeLast();
            doctorRecords.removeLast();
            doctorRecords.add("Doctor");
            allNonManagerEmployeeRecords.add(doctorRecords);
        }
        return allNonManagerEmployeeRecords;
    }

    public void changeFeedbackContent(String customerFeedbackID, String content) {
        CustomerFeedback customerFeedback = Database.getCustomerFeedback(customerFeedbackID);
        customerFeedback.setContent(content);
    }

    public void provideFeedbackToNonManagerEmployee(String nonManagerEmployeeId, String content) {
        new CustomerFeedback(this.id, nonManagerEmployeeId, content);
    }

    public void payForAppointment(Appointment appointment) {
        if (appointment.getStatus().equals("Completed")) {
            throw new AppointmentCompletedException("Appointment is already paid by customer and marked as completed.");
        }
        double amount = appointment.getConsultationFee() + Database.getTotalMedicineChargesOfAppointment(appointment.getId());
        if (this.apWallet < amount) {
            throw new InsufficientApWalletException("Customer does not have enough in AP Wallet to pay for appointment!");
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
            throw new NegativeValueRejectedException("ApWallet of customer must be equal or more than 0!");
        }
    }

    public List<String> createDbRecord() {
        String dbId = this.id;
        String dbName = this.name;
        String dbEmail = this.email;
        String dbPassword = this.password;
        String dbApWallet = String.valueOf(this.apWallet);

        return new ArrayList<>(Arrays.asList(
                dbId, dbName, dbEmail, dbPassword, dbApWallet
        ));
    }

    @Override
    public List<String> createPublicRecord() {
        List<String> publicRecord = super.createPublicRecord();
        publicRecord.add(String.valueOf(this.apWallet));
        return publicRecord;
    }

    public static void createCustomerFromRecord(List<String> record) {
        String customerId = record.getFirst();
        String customerName = record.get(1);
        String customerEmail = record.get(2);
        String customerPassword = record.get(3);
        double customerApWallet = Double.parseDouble(record.getLast());

        Customer customer = new Customer(customerId, customerName, customerEmail, customerPassword, customerApWallet);
        Database.addCustomer(customer);
    }
}
