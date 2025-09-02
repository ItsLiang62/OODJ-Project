package user;

import customExceptions.*;
import database.*;
import operation.Appointment;
import operation.AppointmentMedicine;
import operation.CustomerFeedback;
import operation.Invoice;

import java.io.File;
import java.util.*;
import java.util.function.Function;

public class Customer extends User {

    private double apWallet;

    private static final Set<Customer> customers = new TreeSet<>(Database.getOrderedById());
    private static final File customerFile = new File("data/customer.txt");

    static {
        Database.populateFromRecords(Customer::createFromDbRecord, customerFile);
    }

    public Customer(String id, String name, String email, String password, double apWallet) {
        super(id, name, email, password);
        checkApWallet(apWallet);
        this.apWallet = apWallet;
    }

    public Customer(String name, String email) {
        this(Customer.createId(), name, email, email, 0);
    }

    public double getApWallet() { return apWallet; }

    public List<List<String>> getMyAppointmentRecords() {
        return Appointment.getPublicRecords(Appointment.getFiltered(Appointment::getCustomerId, this.id));
    }

    public List<List<String>> getMyCustomerFeedbackRecords() {
        return CustomerFeedback.getPublicRecords(CustomerFeedback.getFiltered(CustomerFeedback::getCustomerId, this.id));
    }

    public List<List<String>> getMyPrescriptionRecords() {
        Set<Appointment> myAppointments = Appointment.getFiltered(Appointment::getCustomerId, this.id);
        Set<AppointmentMedicine> myPrescriptions = new LinkedHashSet<>();
        for (Appointment myAppointment: myAppointments) {
            myPrescriptions.addAll(AppointmentMedicine.getFiltered(AppointmentMedicine::getAppointmentId, myAppointment.getId()));
        }
        return AppointmentMedicine.getPublicRecords(myPrescriptions);
    }

    public List<List<String>> getMyInvoiceRecords() {
        Set<Appointment> myAppointments = Appointment.getFiltered(Appointment::getCustomerId, this.id);
        Set<Invoice> myInvoices = new LinkedHashSet<>();
        for (Appointment myAppointment: myAppointments) {
            myInvoices.addAll(Invoice.getFiltered(Invoice::getAppointmentId, myAppointment.getId()));
        }
        return Invoice.getPublicRecords(myInvoices);
    }

    public List<List<String>> getNonManagerEmployeeRecords() {
        List<List<String>> allNonManagerEmployeeRecords = new ArrayList<>();
        allNonManagerEmployeeRecords.addAll(Staff.getPublicRecords(Staff.getAll()));
        allNonManagerEmployeeRecords.addAll(Doctor.getPublicRecords(Doctor.getAll()));
        return allNonManagerEmployeeRecords;
    }

    public void changeFeedbackContent(String customerFeedbackID, String content) {
        Set<CustomerFeedback> myCustomerFeedbacks = CustomerFeedback.getFiltered(CustomerFeedback::getCustomerId, this.id);
        if (!CustomerFeedback.getFieldVals(myCustomerFeedbacks, CustomerFeedback::getId).contains(customerFeedbackID)) {
            throw new CustomerFeedbackDoesNotBelongToCustomerException("Failed to change customer feedback content. Customer feedback does not belong to customer.");
        }
        CustomerFeedback customerFeedback = CustomerFeedback.getById(customerFeedbackID);
        customerFeedback.setContent(content.trim());
    }

    public void provideFeedback(String nonManagerEmployeeId, String content) {
        CustomerFeedback newCustomerFeedback = new CustomerFeedback(id, nonManagerEmployeeId, content.trim());
        CustomerFeedback.add(newCustomerFeedback);
    }

    public void pay(Appointment appointment) {
        if (appointment.getStatus().equals("Completed")) {
            throw new AppointmentCompletedException("Appointment is already paid by customer and marked as completed.");
        }
        double amount = appointment.getTotalCharge();
        if (apWallet < amount) {
            throw new InsufficientApWalletException("Customer does not have enough in AP Wallet to pay for appointment!");
        }
        apWallet -= amount;
        Customer.removeById(id, false);
        Customer.add(this);
    }

    public void topUpApWallet(double amount) {
        if (amount < 0) {
            throw new NegativeValueRejectedException("Cannot top up ApWallet with a negative amount!");
        }
        apWallet += amount;
        Customer.removeById(this.id, false);
        Customer.add(this);
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        Customer.removeById(this.id, false);
        Customer.add(this);
    }

    @Override
    public void setEmail(String email) {
        super.setEmail(email);
        Customer.removeById(this.id, false);
        Customer.add(this);
    }

    @Override
    public void setPassword(String password) {
        super.setPassword(password);
        Customer.removeById(this.id, false);
        Customer.add(this);
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
    public List<String> getPublicRecord() {
        List<String> publicRecord = super.getPublicRecord();
        publicRecord.add(String.valueOf(this.apWallet));
        return publicRecord;
    }

    public static void checkApWallet(double apWallet) {
        if (apWallet < 0) {
            throw new NegativeValueRejectedException("ApWallet of customer must be equal or more than 0!");
        }
    }

    public static void createFromDbRecord(List<String> record) {
        String customerId = record.getFirst();
        String customerName = record.get(1);
        String customerEmail = record.get(2);
        String customerPassword = record.get(3);
        double customerApWallet = Double.parseDouble(record.getLast());

        Customer customer = new Customer(customerId, customerName, customerEmail, customerPassword, customerApWallet);
        Customer.add(customer);
    }

    public static String createId() {
        return Database.createId(customers, 'C');
    }

    public static void add(Customer newCustomer) {
        Database.add(newCustomer, customers, customerFile);
    }

    public static Set<Customer> getAll() {
        return new LinkedHashSet<>(customers);
    }

    public static Customer getById(String customerId) {
        return Database.getById(customers, customerId, "customer");
    }

    public static <R> Set<Customer> getFiltered(
            Function<Customer, R> fieldValReturner,
            R fieldVal) {
        return Database.getFiltered(customers, fieldValReturner, fieldVal);
    }

    public static <R> List<R> getFieldVals(Set<Customer> customers, Function<Customer, R> fieldValReturner) {
        return Database.getFieldVals(customers, fieldValReturner);
    }

    public static List<List<String>> getPublicRecords(Set<Customer> customers) {
        return Database.getPublicRecords(customers);
    }

    public static void removeById(String id, boolean removeDependencies) {
        Database.removeById(customers, id, customerFile);

        if (removeDependencies) {
            for (CustomerFeedback customerFeedback: CustomerFeedback.getAll()) {
                if (customerFeedback.getCustomerId().equals(id)) {
                    CustomerFeedback.removeById(customerFeedback.getId());
                }
            }
            for (Appointment appointment: Appointment.getAll()) {
                if (appointment.getCustomerId().equals(id)) {
                    CustomerFeedback.removeById(appointment.getId());
                }
            }
        }
    }
}
