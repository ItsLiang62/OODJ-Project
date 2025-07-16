package user;

import customExceptions.InsufficientApWalletException;
import customExceptions.NegativeValueRejectedException;
import database.Database;
import database.Identifiable;
import operation.Appointment;
import operation.Invoice;

import java.util.*;

public class Staff extends User {

    public Staff(String id, String name, String email, String password) {
        super(id, name, email, password);
        Database.addStaff(this);
    }

    public Staff(String name, String email, String password) {
        this(Identifiable.createId('S'), name, email, password);
    }

    public void createCustomer(String name, String email, String password, double apWallet) {
        new Customer(name, email, password, apWallet);
    }

    public Customer getCustomerWithId(String customerId) {
        return Database.getCustomer(customerId);
    }

    public Customer getCustomerWithEmail(String customerEmail) {
        String customerId = Database.getUserIdWithEmail(customerEmail);
        return Database.getCustomer(customerId);
    }

    public void removeCustomerWithId(String customerId) {
        Database.removeCustomer(customerId);
    }

    public void removeCustomerWithEmail(String customerEmail) {
        String customerId = Database.getUserIdWithEmail(customerEmail);
        Database.removeCustomer(customerId);
    }

    public void createAppointmentForCustomer(String customerId) {
        new Appointment(customerId);
    }

    public void assignDoctorToAppointment(String appointmentId, String doctorId) {
        Appointment appointment = Database.getAppointment(appointmentId);
        appointment.setDoctorId(doctorId);
    }

    public void markAppointmentAsCompleted(String appointmentId) {
        Appointment appointment = Database.getAppointment(appointmentId);
        appointment.setStatusToCompleted();
    }

    public void collectPaymentAndGenerateInvoice(String appointmentId, String paymentMethod, String paymentDate) {
        Appointment appointment = Database.getAppointment(appointmentId);
        double appointmentConsultationFee = appointment.getConsultationFee();
        double appointmentMedicineCharges = appointment.getTotalMedicineCharges();
        Customer customerOfAppointment = Database.getCustomer(appointment.getCustomerId());
        customerOfAppointment.payWithApWallet(appointmentConsultationFee + appointmentMedicineCharges);

        new Invoice(appointmentId, paymentMethod, paymentDate);
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

    public List<String> createRecord() {
        String dbId = this.id;
        String dbName = this.name;
        String dbEmail = this.email;
        String dbPassword = this.password;

        return new ArrayList<>(Arrays.asList(
                dbId, dbName, dbEmail, dbPassword
        ));
    }

    public static void createStaffFromRecord(List<String> record) {
        String staffId = record.getFirst();
        String staffName = record.get(1);
        String staffEmail = record.get(2);
        String staffPassword = record.getLast();

        new Staff(staffId, staffName, staffEmail, staffPassword);
    }
}
