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
            List<String> staffRecord = Database.getStaff(staffId).createPublicRecord();
            staffRecord.removeLast();
            staffRecord.add("Staff");
            allNonManagerEmployeeRecords.add(staffRecord);
        }
        for (String doctorId: Database.getAllDoctorId()) {
            List<String> doctorRecord = Database.getDoctor(doctorId).createPublicRecord();
            doctorRecord.removeLast();
            doctorRecord.add("Doctor");
            allNonManagerEmployeeRecords.add(doctorRecord);
        }
        return allNonManagerEmployeeRecords;
    }

    public Set<List<String>> getAllMyPrescriptionRecords() {
        Set<List<String>> allMyPrescriptionRecords = new LinkedHashSet<>();
        for (List<String> prescriptionInfo: Database.getAllPrescriptionInfoOfCustomer(this.id)) {
            allMyPrescriptionRecords.add(Database.getAppointmentMedicine(prescriptionInfo.getFirst(), prescriptionInfo.getLast()).createPublicRecord());
        }
        return allMyPrescriptionRecords;
    }

    public Set<List<String>> getAllMyInvoiceRecords() {
        Set<List<String>> allMyInvoiceRecords = new LinkedHashSet<>();
        for (String invoiceId: Database.getAllInvoiceIdOfCustomer(this.id)) {
            allMyInvoiceRecords.add(Database.getInvoice(invoiceId).createPublicRecord());
        }
        return allMyInvoiceRecords;
    }

    public void changeFeedbackContent(String customerFeedbackID, String content) {
        CustomerFeedback customerFeedback = Database.getCustomerFeedback(customerFeedbackID);
        customerFeedback.setContent(content.trim());
    }

    public void provideFeedbackToNonManagerEmployee(String nonManagerEmployeeId, String content) {
        CustomerFeedback newCustomerFeedback = new CustomerFeedback(this.id, nonManagerEmployeeId, content.trim());
        Database.addCustomerFeedback(newCustomerFeedback);
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
        if (amount < 0) {
            throw new NegativeValueRejectedException("Cannot top up ApWallet with a negative amount!");
        }
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
