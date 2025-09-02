package user;

import customExceptions.SelfDeletionUnsupportedException;
import database.*;
import operation.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Month;
import java.util.*;
import java.util.function.Function;

public class Manager extends User {

    private static final Set<Manager> managers = new TreeSet<>(Database.getOrderedById());
    private static final File managerFile = new File("data/manager.txt");

    static {
        Database.createFile(managerFile);
        try {
            if (!new Scanner(managerFile).hasNextLine()) {  // Write the root manager record to manager.txt if the file is empty
                try (FileWriter managerFileWriter = new FileWriter(managerFile)) {
                    managerFileWriter.write("M001,Root Manager,root@email.com,123");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        Database.populateFromRecords(Manager::createFromDbRecord, managerFile);
    }

    public Manager(String id, String name, String email, String password) {
        super(id, name, email, password);
    }

    public Manager(String name, String email) {
        this(createId(), name, email, email);
    }

    // Manager functionalities

    public void addManager(Manager newManager) {
        checkName(newManager.getName());
        checkEmail(newManager.getEmail());
        checkPassword(newManager.getPassword());
        // Verify manager validity in case user did not enter constructor instantiated Manager
        Manager.add(newManager);
    }

    public void addStaff(Staff newStaff) {
        checkName(newStaff.getName());
        checkEmail(newStaff.getEmail());
        checkPassword(newStaff.getPassword());
        Staff.add(newStaff);
    }

    public void addDoctor(Doctor newDoctor) {
        checkName(newDoctor.getName());
        checkEmail(newDoctor.getEmail());
        checkPassword(newDoctor.getPassword());
        Doctor.add(newDoctor);
    }

    public void addMedicine(Medicine newMedicine) {
        Medicine.checkName(newMedicine.getName());
        Medicine.checkCharge(newMedicine.getCharge());
        Medicine.add(newMedicine);
    }

    public void updateManager(Manager newManager) { // expects a valid manager
        Manager.removeById(newManager.getId());
        Manager.add(newManager);
    }

    public void updateStaff(Staff newStaff) {
        Staff.removeById(newStaff.getId(), false);
        Staff.add(newStaff);
    }

    public void updateDoctor(Doctor newDoctor) {
        Doctor.removeById(newDoctor.getId(), false);
        Doctor.add(newDoctor);
    }

    public void updateMedicine(Medicine newMedicine) {
        Medicine.removeById(newMedicine.getId(), false);
        Medicine.add(newMedicine);
    }

    public void removeManagerById(String managerId) {
        if (!managerId.equals(this.id)) {
            Manager.removeById(managerId);
        } else {
            throw new SelfDeletionUnsupportedException("Cannot delete yourself!");
        }
    }

    public void removeStaffById(String staffId) {
        Staff.removeById(staffId, true);
    }

    public void removeDoctorById(String doctorId) {
        Doctor.removeById(doctorId, true);
    }

    public void removeMedicineById(String medicineId) {
        Medicine.removeById(medicineId, true);
    }

    public List<List<String>> getAppointmentPublicRecords() {
        return Appointment.getPublicRecords(Appointment.getAll());
    }

    public List<List<String>> getCustomerFeedbackPublicRecords() {
        return CustomerFeedback.getPublicRecords(CustomerFeedback.getAll());
    }

    public List<List<String>> getManagerPublicRecords() {
        return Manager.getPublicRecords(Manager.getAll());
    }

    public List<List<String>> getStaffPublicRecords() {
        return Staff.getPublicRecords(Staff.getAll());
    }

    public List<List<String>> getDoctorPublicRecords() {
        return Doctor.getPublicRecords(Doctor.getAll());
    }

    public List<List<String>> getMedicinePublicRecords() {
        return Medicine.getPublicRecords(Medicine.getAll());
    }

    public Set<Invoice> getInvoicesOfMonth(Month month) {
        return Invoice.getFiltered((Invoice invoice) -> invoice.getCreationDate().getMonth(), month);
    }

    public static void createFromDbRecord(List<String> record) {
        String managerId = record.getFirst();
        String managerName = record.get(1);
        String managerEmail = record.get(2);
        String managerPassword = record.getLast();

        Manager manager = new Manager(managerId, managerName, managerEmail, managerPassword);
        Manager.add(manager);
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        Manager.removeById(this.id);
        Manager.add(this);
    }

    @Override
    public void setEmail(String email) {
        super.setEmail(email);
        Manager.removeById(this.id);
        Manager.add(this);
    }

    @Override
    public void setPassword(String password) {
        super.setPassword(password);
        Manager.removeById(this.id);
        Manager.add(this);
    }

    public static String createId() {
        return Database.createId(managers, 'M');
    }

    public static void add(Manager newManager) {
        managers.add(newManager);
        Database.saveRecords(managers, managerFile);
    }

    public static Set<Manager> getAll() {
        return new LinkedHashSet<>(managers);
    }

    public static Manager getById(String managerId) {
        return Database.getById(getAll(), managerId, "manager");
    }

    public static <R> Set<Manager> getFiltered(
            Function<Manager, R> fieldValReturner,
            R fieldVal) {
        return Database.getFiltered(getAll(), fieldValReturner, fieldVal);
    }

    public static <R> List<R> getFieldVals(Set<Manager> managers, Function<Manager, R> fieldValReturner) {
        return Database.getFieldVals(managers, fieldValReturner);
    }

    public static List<List<String>> getPublicRecords(Set<Manager> managers) {
        return Database.getPublicRecords(managers);
    }

    public static void removeById(String id) {
        Database.removeById(getAll(), id, managerFile);
    }
}