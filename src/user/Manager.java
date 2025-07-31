package user;

import customExceptions.SelfDeletionUnsupportedException;
import database.*;
import operation.Invoice;
import operation.Medicine;

import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class Manager extends User {

    public Manager(String id, String name, String email, String password) {
        super(id, name, email, password);
    }

    public Manager(String name, String email) {
        this(IdCreator.createId('M'), name, email, email);
    }

    public void addManager(Manager newManager) {
        checkName(newManager.getName());
        checkEmail(newManager.getEmail());
        checkPassword(newManager.getPassword());
        // Verify manager validity in case user did not enter constructor instantiated Manager
        Database.addManager(newManager);
    }

    public void addStaff(Staff newStaff) {
        checkName(newStaff.getName());
        checkEmail(newStaff.getEmail());
        checkPassword(newStaff.getPassword());
        Database.addStaff(newStaff);
    }

    public void addDoctor(Doctor newDoctor) {
        checkName(newDoctor.getName());
        checkEmail(newDoctor.getEmail());
        checkPassword(newDoctor.getPassword());
        Database.addDoctor(newDoctor);
    }

    public void addMedicine(Medicine newMedicine) {
        Medicine.checkName(newMedicine.getName());
        Medicine.checkCharge(newMedicine.getCharge());
        Database.addMedicine(newMedicine);
    }

    public Manager getManagerById(String managerId) { return Database.getManager(managerId); }
    public Staff getStaffById(String staffId) { return Database.getStaff(staffId); }
    public Doctor getDoctorById(String doctorId) { return Database.getDoctor(doctorId); }

    public Manager getManagerByEmail(String managerEmail) {
        String managerId = Database.getUserIdByEmail(managerEmail);
        return Database.getManager(managerId);
    }
    public Staff getStaffByEmail(String staffEmail) {
        String staffId = Database.getUserIdByEmail(staffEmail);
        return Database.getStaff(staffId);
    }
    public Doctor getDoctorByEmail(String doctorEmail) {
        String doctorId = Database.getUserIdByEmail(doctorEmail);
        return Database.getDoctor(doctorId);
    }

    public void updateManager(Manager newManager) { // expects a valid manager
        Database.removeManager(newManager.getId());
        Database.addManager(newManager);
    }
    public void updateStaff(Staff newStaff) {
        Database.removeStaff(newStaff.getId(), false);
        Database.addStaff(newStaff);
    }
    public void updateDoctor(Doctor newDoctor) {
        Database.removeDoctor(newDoctor.getId(), false);
        Database.addDoctor(newDoctor);
    }
    public void updateMedicine(Medicine newMedicine) {
        Database.removeMedicine(newMedicine.getId(), false);
        Database.addMedicine(newMedicine);
    }

    public void removeManagerById(String managerId) {
        if (!managerId.equals(this.id)) {
            Database.removeManager(managerId);
        } else {
            throw new SelfDeletionUnsupportedException("Cannot delete yourself!");
        }
    }
    public void removeStaffById(String staffId) { Database.removeStaff(staffId, true); }
    public void removeDoctorById(String doctorId) { Database.removeDoctor(doctorId, true); }

    public void removeMedicineById(String medicineId) {
        Database.removeMedicine(medicineId, true);
    }

    public List<List<String>> getAllAppointmentPublicRecords() { return Database.getAllAppointmentPublicRecords(); }
    public List<List<String>> getAllCustomerFeedbackPublicRecords() { return Database.getAllCustomerFeedbackPublicRecords(); }
    public List<List<String>> getAllManagerPublicRecords() { return Database.getAllManagerPublicRecords(); }
    public List<List<String>> getAllStaffPublicRecords() { return Database.getAllStaffPublicRecords(); }
    public List<List<String>> getAllDoctorPublicRecords() { return Database.getAllDoctorPublicRecords(); }
    public List<List<String>> getAllMedicinePublicRecords() { return Database.getAllMedicinePublicRecords(); }
    public List<Invoice> getAllInvoicesOfMonth(Month month) { return Database.getAllInvoiceIdOfMonth(month).stream().map(Database::getInvoice).collect(Collectors.toCollection(ArrayList::new)); }

    public static void createManagerFromDbRecord(List<String> record) {
        String managerId = record.getFirst();
        String managerName = record.get(1);
        String managerEmail = record.get(2);
        String managerPassword = record.getLast();

        Manager manager = new Manager(managerId, managerName, managerEmail, managerPassword);
        Database.addManager(manager);
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