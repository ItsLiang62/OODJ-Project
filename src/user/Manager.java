package user;

import database.*;
import operation.Medicine;

import java.util.*;

public class Manager extends User {

    public Manager(String id, String name, String email, String password) {
        super(id, name, email, password);
        Database.addManager(this);
    }

    public Manager(String name, String email, String password) {
        this(IdCreator.createId('M'), name, email, password);
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

    public Manager getManagerById(String managerId) {
        return Database.getManager(managerId);
    }

    public Manager getManagerByEmail(String managerEmail) {
        String managerId = Database.getUserIdByEmail(managerEmail);
        return Database.getManager(managerId);
    }

    public Staff getStaffById(String staffId) {
        return Database.getStaff(staffId);
    }

    public Staff getStaffByEmail(String staffEmail) {
        String staffId = Database.getUserIdByEmail(staffEmail);
        return Database.getStaff(staffId);
    }

    public Doctor getDoctorById(String doctorId) {
        return Database.getDoctor(doctorId);
    }

    public Doctor getDoctorByEmail(String doctorEmail) {
        String doctorId = Database.getUserIdByEmail(doctorEmail);
        return Database.getDoctor(doctorId);
    }

    public void updateManager(String managerId, String newName, String newEmail, String newPassword) {
        Manager manager = Database.getManager(managerId);
        Database.removeManager(managerId);
        try {
            new Manager(managerId, newName, newEmail, newPassword);
        } catch (RuntimeException e) {
            Database.removeManager(managerId);
            new Manager(managerId, manager.getName(), manager.getEmail(), manager.getPassword());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void updateStaff(String staffId, String newName, String newEmail, String newPassword) {
        Staff staff = Database.getStaff(staffId);
        Database.removeStaff(staffId, false);
        try {
            new Staff(staffId, newName, newEmail, newPassword);
        } catch (RuntimeException e) {
            Database.removeStaff(staffId, false);
            new Staff(staffId, staff.getName(), staff.getEmail(), staff.getPassword());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void updateDoctor(String doctorId, String newName, String newEmail, String newPassword) {
        Doctor doctor = Database.getDoctor(doctorId);
        Database.removeDoctor(doctorId, false);
        try {
            new Doctor(doctorId, newName, newEmail, newPassword);
        } catch (RuntimeException e) {
            Database.removeDoctor(doctorId, false);
            new Doctor(doctorId, doctor.getName(), doctor.getEmail(), doctor.getPassword());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void removeManagerById(String managerId) {
        if (!managerId.equals(this.id)) {
            Database.removeManager(managerId);
        }
    }

    public void removeStaffById(String staffId) { Database.removeStaff(staffId, true); }

    public void removeDoctorById(String doctorId) { Database.removeDoctor(doctorId, true); }

    public Set<List<String>> getAllAppointmentPublicRecords() { return Database.getAllPublicRecordsOf(Database.getAllAppointmentId(), Database::getAppointment); }

    public Set<List<String>> getAllCustomerFeedbackPublicRecords() { return Database.getAllPublicRecordsOf(Database.getAllCustomerFeedbackId(), Database::getCustomerFeedback); }

    public Set<List<String>> getAllManagerPublicRecords() { return Database.getAllPublicRecordsOf(Database.getAllManagerId(), Database::getManager); }

    public Set<List<String>> getAllStaffPublicRecords() { return Database.getAllPublicRecordsOf(Database.getAllStaffId(), Database::getStaff); }

    public Set<List<String>> getAllDoctorPublicRecords() { return Database.getAllPublicRecordsOf(Database.getAllDoctorId(), Database::getDoctor); }

    public Set<List<String>> getAllMedicinePublicRecords() { return Database.getAllPublicRecordsOf(Database.getAllMedicineId(), Database::getMedicine); }


    public void addMedicine(String medicineName, double medicineCharge) {
        new Medicine(medicineName, medicineCharge);
    }

    public void removeMedicine(String medicineId) {
        Database.removeMedicine(medicineId, true);
    }

    public void updateMedicine(String medicineId, String newName, double newCharge) {
        Medicine medicine = Database.getMedicine(medicineId);
        Database.removeMedicine(medicineId, false); // remove medicine record without removing dependencies
        try {
            new Medicine(medicineId, newName, newCharge);
        } catch (RuntimeException e) {
            Database.removeDoctor(medicineId, false); // remove newly added invalid medicine record
            new Medicine(medicineId, medicine.getName(), medicine.getCharge()); // add back original medicine record
            throw new RuntimeException(e.getMessage()); // throw error due to invalid new medicine record
        }
    }

    public static void createManagerFromDbRecord(List<String> record) {
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