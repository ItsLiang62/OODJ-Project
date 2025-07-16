package user;

import database.*;
import operation.Appointment;
import operation.CustomerFeedback;
import operation.Medicine;

import java.util.*;

public class Manager extends User {

    public Manager(String id, String name, String email, String password) {
        super(id, name, email, password);
        Database.addManager(this);
    }

    public Manager(String name, String email, String password) {
        this(Identifiable.createId('M'), name, email, password);
    }

    public void createManager(String name, String email, String password) {
        new Manager(name, email, password);
    }

    public void createStaff(String name, String email, String password) {
        new Staff(name, email, password);
    }

    public void createDoctor(String name, String email, String password) {
        new Doctor(name, email, password);
    }

    public Manager getManagerWithId(String managerId) {
        return Database.getManager(managerId);
    }

    public Manager getManagerWithEmail(String managerEmail) {
        String managerId = Database.getUserIdWithEmail(managerEmail);
        return Database.getManager(managerId);
    }

    public Staff getStaffWithId(String staffId) {
        return Database.getStaff(staffId);
    }

    public Staff getStaffWithEmail(String staffEmail) {
        String staffId = Database.getUserIdWithEmail(staffEmail);
        return Database.getStaff(staffId);
    }

    public Doctor getDoctorWithId(String doctorId) {
        return Database.getDoctor(doctorId);
    }

    public Doctor getDoctorWithEmail(String doctorEmail) {
        String doctorId = Database.getUserIdWithEmail(doctorEmail);
        return Database.getDoctor(doctorId);
    }

    public void removeManagerWithId(String managerId) {
        if (!managerId.equals(this.id)) {
            Database.removeManager(managerId);
        }
    }

    public void removeManagerWithEmail(String managerEmail) {
        if (!managerEmail.equals(this.email)) {
            String managerId = Database.getUserIdWithEmail(managerEmail);
            Database.removeManager(managerId);
        }
    }

    public void removeStaffWithId(String staffId) {
        Database.removeStaff(staffId);
    }

    public void removeStaffWithEmail(String staffEmail) {
        String staffId = Database.getUserIdWithEmail(staffEmail);
        Database.removeStaff(staffId);
    }

    public void removeDoctorWithId(String doctorId) {
        Database.removeDoctor(doctorId);
    }

    public void removeDoctorWithEmail(String doctorEmail) {
        String doctorId = Database.getUserIdWithEmail(doctorEmail);
        Database.removeDoctor(doctorId);
    }

    public Set<List<String>> getAllAppointmentRecords() {
        Set<List<String>> allAppointmentRecords = new LinkedHashSet<>();
        for (String appointmentId: Database.getAllAppointmentId()) {
            allAppointmentRecords.add(Database.getAppointment(appointmentId).createRecord());
        }
        return allAppointmentRecords;
    }

    public Set<List<String>> getAllCustomerFeedbackRecords() {
        Set<List<String>> allCustomerFeedbackRecords = new LinkedHashSet<>();
        for (String customerFeedbackId: Database.getAllCustomerFeedbackId()) {
            allCustomerFeedbackRecords.add(Database.getCustomerFeedback(customerFeedbackId).createRecord());
        }
        return allCustomerFeedbackRecords;
    }

    public void addMedicine(String medicineName, double medicineCharge) {
        new Medicine(medicineName, medicineCharge);
    }

    public void removeMedicine(String medicineId) {
        Database.removeMedicine(medicineId);
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

    public static void createManagerFromRecord(List<String> record) {
        String managerId = record.getFirst();
        String managerName = record.get(1);
        String managerEmail = record.get(2);
        String managerPassword = record.getLast();

        new Manager(managerId, managerName, managerEmail, managerPassword);
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        Database.removeManager(this.id);
        Database.addManager(this);
    }

    @Override
    public void setEmail(String email) {
        super.setEmail(email);
        Database.removeManager(this.id);
        Database.addManager(this);
    }

    @Override
    public void setPassword(String password) {
        super.setPassword(password);
        Database.removeManager(this.id);
        Database.addManager(this);
    }
}