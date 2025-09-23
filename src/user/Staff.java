package user;

import database.Database;
import operation.Appointment;
import operation.CustomerFeedback;
import operation.Invoice;

import java.io.File;
import java.util.*;
import java.util.function.Function;

public class Staff extends User {

    private static final Set<Staff> staffs = new TreeSet<>(Database.getOrderedById());
    private static final File staffFile = new File("data/staff.txt");

    static {
        Database.populateFromRecords(Staff::createFromDbRecord, staffFile);
    }

    public Staff(String id, String name, String email, String password) {
        super(id, name, email, password);
    }

    public Staff(String name, String email) {
        this(createId(), name, email, email);
    }

    public void addCustomer(Customer newCustomer) {
        checkName(newCustomer.getName());
        checkEmail(newCustomer.getEmail());
        checkPassword(newCustomer.getPassword());
        Customer.add(newCustomer);
    }

    public void addAppointment(Appointment newAppointment) {
        Appointment.checkCustomerId(newAppointment.getCustomerId());
        Appointment.checkDoctorId(newAppointment.getDoctorId());
        Appointment.checkConsultationFee(newAppointment.getConsultationFee());
        Appointment.checkStatus(newAppointment.getStatus());
        Appointment.add(newAppointment);
    }

    public void addInvoice(Invoice newInvoice) {
        Invoice.checkAppointmentId(newInvoice.getAppointmentId());
        Invoice.add(newInvoice);
    }

    public Customer getCustomerById(String customerId) {
        return Customer.getById(customerId);
    }

    public Appointment getAppointmentById(String appointmentId) {
        return Appointment.getById(appointmentId);
    }

    public List<List<String>> getCustomerPublicRecords() {
        return Database.getPublicRecords(Customer.getAll());
    }

    public List<List<String>> getAppointmentPublicRecords() {
        return Database.getPublicRecords(Appointment.getAll());
    }

    public List<List<String>> getDoctorPublicRecords() {
        return Database.getPublicRecords(Doctor.getAll());
    }

    public List<List<String>> getInvoicePublicRecords() {
        return Database.getPublicRecords(Invoice.getAll());
    }

    public List<List<String>> getMyCustomerFeedbackRecords() {
        return Database.getPublicRecords(CustomerFeedback.getFiltered(CustomerFeedback::getNonManagerEmployeeId, this.id));
    }

    public void updateCustomer(Customer newCustomer) {
        Customer.removeById(newCustomer.getId(), false);
        Customer.add(newCustomer);
    }

    public void removeCustomerById(String customerId) {
        Customer.removeById(customerId, true);
    }

    public void removeAppointmentById(String appointmentId) {
        Appointment.removeById(appointmentId, true);
    }

    public void assignDoctorToAppointment(String appointmentId, String doctorId) {
        Appointment appointment = Appointment.getById(appointmentId);
        appointment.setDoctorId(doctorId);
    }

    public void collectPayment(String appointmentId) {
        Appointment appointment = Appointment.getById(appointmentId);
        Customer customerOfAppointment = Customer.getById(appointment.getCustomerId());
        customerOfAppointment.pay(appointment);
        appointment.setStatusToCompleted();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        removeById(this.id, false);
        add(this);
    }

    @Override
    public void setEmail(String email) {
        super.setEmail(email);
        Staff.removeById(this.id, false);
        Staff.add(this);
    }

    @Override
    public void setPassword(String password) {
        super.setPassword(password);
        Staff.removeById(this.id, false);
        Staff.add(this);
    }

    public static void createFromDbRecord(List<String> record) {
        String staffId = record.getFirst();
        String staffName = record.get(1);
        String staffEmail = record.get(2);
        String staffPassword = record.getLast();

        Staff staff = new Staff(staffId, staffName, staffEmail, staffPassword);
        Staff.add(staff);
    }

    public static String createId() {
        return Database.createId(staffs, 'S');
    }

    public static void add(Staff newStaff) {
        Database.add(newStaff, staffs, staffFile);
    }

    public static Set<Staff> getAll() {
        return new LinkedHashSet<>(staffs);
    }

    public static Staff getById(String staffId) {
        return Database.getById(staffs, staffId, "staff");
    }

    public static <R> Set<Staff> getFiltered(
            Function<Staff, R> fieldValReturner,
            R fieldVal) {
        return Database.getFiltered(staffs, fieldValReturner, fieldVal);
    }

    public static <R> List<R> getFieldVals(Set<Staff> staffs, Function<Staff, R> fieldValReturner) {
        return Database.getFieldVals(staffs, fieldValReturner);
    }

    public static List<List<String>> getPublicRecords(Set<Staff> staffs) {
        return Database.getPublicRecords(staffs);
    }

    public static void removeById(String id, boolean removeDependencies) {
        if (removeDependencies) {
            for (CustomerFeedback customerFeedback: CustomerFeedback.getAll()) {
                if (customerFeedback.getNonManagerEmployeeId().equals(id)) {
                    CustomerFeedback.removeById(customerFeedback.getId());
                }
            }
        }
        Database.removeById(staffs, id, staffFile);
    }
}