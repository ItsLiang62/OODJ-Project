package user;

import database.Database;
import database.IdCreator;
import operation.Appointment;
import operation.Invoice;

import java.util.*;

public class Staff extends User {

    public Staff(String id, String name, String email, String password) {
        super(id, name, email, password);
    }

    public Staff(String name, String email) {
        this(IdCreator.createId('S'), name, email, email);
    }

    public void addCustomer(Customer newCustomer) {
        checkName(newCustomer.getName());
        checkEmail(newCustomer.getEmail());
        checkPassword(newCustomer.getPassword());
        Database.addCustomer(newCustomer);
    }

    public void addAppointment(Appointment newAppointment) {
        Appointment.checkCustomerId(newAppointment.getCustomerId());
        Appointment.checkDoctorId(newAppointment.getDoctorId());
        Appointment.checkConsultationFee(newAppointment.getConsultationFee());
        Appointment.checkStatus(newAppointment.getStatus());
        Database.addAppointment(newAppointment);
    }

    public void addInvoice(Invoice newInvoice) {
        Invoice.checkAppointmentId(newInvoice.getAppointmentId());
        Database.addInvoice(newInvoice);
    }

    public Customer getCustomerById(String customerId) {
        return Database.getCustomer(customerId);
    }
    public Appointment getAppointmentById(String appointmentId) { return Database.getAppointment(appointmentId); }

    public List<List<String>> getAllCustomerPublicRecords() { return Database.getAllCustomerPublicRecords(); }
    public List<List<String>> getAllAppointmentPublicRecords() { return Database.getAllAppointmentPublicRecords(); }
    public List<List<String>> getAllDoctorPublicRecords() { return Database.getAllDoctorPublicRecords(); }
    public List<List<String>> getAllInvoicePublicRecords() { return Database.getAllInvoicePublicRecords(); }
    public List<List<String>> getAllMyCustomerFeedbackRecords() { return Database.getAllCustomerFeedbackPublicRecordsOfNonManagerEmployee(id); }

    public void updateCustomer(Customer newCustomer) {
        Database.removeCustomer(newCustomer.getId(), false);
        Database.addCustomer(newCustomer);
    }

    public void removeCustomerById(String customerId) {
        Database.removeCustomer(customerId, true);
    }
    public void removeAppointmentById(String appointmentId) { Database.removeAppointment(appointmentId, true); }

    public void assignDoctorToAppointment(String appointmentId, String doctorId) {
        Appointment appointment = Database.getAppointment(appointmentId);
        appointment.setDoctorId(doctorId);
    }

    public void collectPayment(String appointmentId) {
        Appointment appointment = Database.getAppointment(appointmentId);
        Customer customerOfAppointment = Database.getCustomer(appointment.getCustomerId());
        customerOfAppointment.payForAppointment(appointment);
        appointment.setStatusToCompleted();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        Database.removeStaff(this.id, false);
        Database.addStaff(this);
    }

    @Override
    public void setEmail(String email) {
        super.setEmail(email);
        Database.removeStaff(this.id, false);
        Database.addStaff(this);
    }

    @Override
    public void setPassword(String password) {
        super.setPassword(password);
        Database.removeStaff(this.id, false);
        Database.addStaff(this);
    }

    public static void createStaffFromRecord(List<String> record) {
        String staffId = record.getFirst();
        String staffName = record.get(1);
        String staffEmail = record.get(2);
        String staffPassword = record.getLast();

        Staff staff = new Staff(staffId, staffName, staffEmail, staffPassword);
        Database.addStaff(staff);
    }
}