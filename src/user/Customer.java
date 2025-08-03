package user;

import customExceptions.*;
import database.*;
import operation.Appointment;
import operation.CustomerFeedback;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public double getApWallet() { return apWallet; }

    public List<List<String>> getAllMyAppointmentRecords() { return Database.getAllAppointmentPublicRecordsOfCustomer(id); }
    public List<List<String>> getAllMyCustomerFeedbackRecords() { return Database.getAllCustomerFeedbackPublicRecordsOfCustomer(id); }
    public List<List<String>> getAllNonManagerEmployeeRecords() { return Stream.of(Database.getAllStaffPublicRecords(), Database.getAllDoctorPublicRecords()).flatMap(List::stream).collect(Collectors.toCollection(ArrayList::new)); }
    public List<List<String>> getAllMyPrescriptionRecords() { return Database.getAllAppointmentMedicinePublicRecordsOfCustomer(id); }
    public List<List<String>> getAllMyInvoiceRecords() { return Database.getAllInvoicePublicRecordsOfCustomer(id); }

    public void changeFeedbackContent(String customerFeedbackID, String content) {
        if (!Database.getAllCustomerFeedbackIdOfCustomer(id).contains(customerFeedbackID)) {
            throw new CustomerFeedbackDoesNotBelongToCustomerException("Failed to change customer feedback content. Customer feedback does not belong to customer.");
        }
        CustomerFeedback customerFeedback = Database.getCustomerFeedback(customerFeedbackID);
        customerFeedback.setContent(content.trim());
    }

    public void provideFeedbackToNonManagerEmployee(String nonManagerEmployeeId, String content) {
        CustomerFeedback newCustomerFeedback = new CustomerFeedback(id, nonManagerEmployeeId, content.trim());
        Database.addCustomerFeedback(newCustomerFeedback);
    }

    public void payForAppointment(Appointment appointment) {
        if (appointment.getStatus().equals("Completed")) {
            throw new AppointmentCompletedException("Appointment is already paid by customer and marked as completed.");
        }
        double amount = appointment.getConsultationFee() + Database.getTotalMedicineChargesOfAppointment(appointment.getId());
        if (apWallet < amount) {
            throw new InsufficientApWalletException("Customer does not have enough in AP Wallet to pay for appointment!");
        }
        apWallet -= amount;
        Database.removeCustomer(id, false);
        Database.addCustomer(this);
    }

    public void topUpApWallet(double amount) {
        if (amount < 0) {
            throw new NegativeValueRejectedException("Cannot top up ApWallet with a negative amount!");
        }
        apWallet += amount;
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
