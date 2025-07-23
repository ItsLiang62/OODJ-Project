package database;

import user.*;
import operation.*;
import customExceptions.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

import java.io.FileWriter;
import java.util.stream.Collectors;

public final class Database {

    private final static File managerFile = new File("data/manager.txt");
    private final static File staffFile = new File("data/staff.txt");
    private final static File doctorFile = new File("data/doctor.txt");
    private final static File customerFile = new File("data/customer.txt");
    private final static File appointmentFile = new File("data/appointment.txt");
    private final static File medicineFile = new File("data/medicine.txt");
    private final static File appointmentMedicineFile = new File("data/appointmentmedicine.txt");
    private final static File customerFeedbackFile = new File("data/customerFeedback.txt");
    private final static File invoiceFile = new File("data/invoice.txt");

    private static Set<Manager> managers;
    private static Set<Staff> staffs;
    private static Set<Doctor> doctors;
    private static Set<Customer> customers;
    private static Set<Appointment> appointments;
    private static Set<Medicine> medicines;
    private static Set<AppointmentMedicine> appointmentMedicines;
    private static Set<CustomerFeedback> customerFeedbacks;
    private static Set<Invoice> invoices;

    static {
        managers = new TreeSet<>(Comparator.comparingInt(
                user -> Integer.parseInt(user.getId().substring(1))
        ));
        staffs = new TreeSet<>(Comparator.comparingInt(
                user -> Integer.parseInt(user.getId().substring(1))
        ));
        doctors = new TreeSet<>(Comparator.comparingInt(
                user -> Integer.parseInt(user.getId().substring(1))
        ));
        customers = new TreeSet<>(Comparator.comparingInt(
                user -> Integer.parseInt(user.getId().substring(1))
        ));

        Map<String, Integer> appointmentStatusPriority = new HashMap<>();
        appointmentStatusPriority.put("Pending", 1);
        appointmentStatusPriority.put("Confirmed", 2);
        appointmentStatusPriority.put("Completed", 3);
        appointments = new TreeSet<>(Comparator.comparingInt(
                (Appointment appointment) -> appointmentStatusPriority.get(appointment.getStatus())
        ).thenComparing(
                (Appointment appointment) -> Integer.parseInt(appointment.getId().substring(1))
        ));
        medicines = new TreeSet<>(Comparator.comparingInt(
                medicine -> Integer.parseInt(medicine.getId().substring(1))
        ));
        appointmentMedicines = new TreeSet<>(Comparator.comparingInt(
                (AppointmentMedicine appointmentMedicine) -> Integer.parseInt(appointmentMedicine.getAppointmentId().substring(1))
        ).thenComparing(
                (AppointmentMedicine appointmentMedicine) -> Integer.parseInt(appointmentMedicine.getMedicineId().substring(1))
        ));
        customerFeedbacks = new TreeSet<>(Comparator.comparingInt(
                customerFeedback -> Integer.parseInt(customerFeedback.getId().substring(1))
        ));
        invoices = new TreeSet<>(Comparator.comparingInt(
                invoice -> Integer.parseInt(invoice.getId().substring(1))
        ));
        Database.populate();
    }

    // add

    public static void addManager(Manager newManager) {
        managers.add(newManager);
        Database.saveRecords(managers, managerFile);
    }

    public static void addStaff(Staff newStaff) {
        staffs.add(newStaff);
        Database.saveRecords(staffs, staffFile);
    }

    public static void addDoctor(Doctor newDoctor) {
        doctors.add(newDoctor);
        Database.saveRecords(doctors, doctorFile);
    }

    public static void addCustomer(Customer newCustomer) {
        customers.add(newCustomer);
        Database.saveRecords(customers, customerFile);
    }

    public static void addAppointment(Appointment newAppointment) {
        appointments.add(newAppointment);
        Database.saveRecords(appointments, appointmentFile);
    }

    public static void addMedicine(Medicine newMedicine) {
        medicines.add(newMedicine);
        Database.saveRecords(medicines, medicineFile);
    }

    public static void addAppointmentMedicine(AppointmentMedicine newAppointmentMedicine) {
        appointmentMedicines.add(newAppointmentMedicine);
        Database.saveRecords(appointmentMedicines, appointmentMedicineFile);
    }

    public static void addCustomerFeedback(CustomerFeedback newCustomerFeedback) {
        customerFeedbacks.add(newCustomerFeedback);
        Database.saveRecords(customerFeedbacks, customerFeedbackFile);
    }

    public static void addInvoice(Invoice newInvoice) {
        invoices.add(newInvoice);
        Database.saveRecords(invoices, invoiceFile);
    }

    // get

    public static <T extends Identifiable> T getIdentifiable(String id, Set<T> identifiableSet, String identifiableType) {
        for (T identifiable: identifiableSet) {
            if (identifiable.getId().equals(id)) {
                return identifiable;
            }
        }
        throw new IdNotFoundException(String.format("Could not find %s with the given ID!", identifiableType));
    }
    public static Manager getManager(String managerId) { return getIdentifiable(managerId, managers, "manager"); }

    public static Staff getStaff(String staffId) { return getIdentifiable(staffId, staffs, "staff");  }

    public static Doctor getDoctor(String doctorId) { return getIdentifiable(doctorId, doctors, "doctor");  }

    public static Customer getCustomer(String customerId) { return getIdentifiable(customerId, customers, "customer");  }

    public static Appointment getAppointment(String appointmentId) { return getIdentifiable(appointmentId, appointments, "appointment");  }

    public static Medicine getMedicine(String medicineId) { return getIdentifiable(medicineId, medicines, "medicine"); }

    public static CustomerFeedback getCustomerFeedback(String customerFeedbackId) { return getIdentifiable(customerFeedbackId, customerFeedbacks, "customer feedback");}

    public static Invoice getInvoice(String invoiceId) { return getIdentifiable(invoiceId, invoices, "invoice"); }

    public static AppointmentMedicine getAppointmentMedicine(String appointmentId, String medicineId) {
        for (AppointmentMedicine appointmentMedicine: appointmentMedicines) {
            if (appointmentMedicine.getAppointmentId().equals(appointmentId) && appointmentMedicine.getMedicineId().equals(medicineId)) {
                return appointmentMedicine;
            }
        }
        throw new IdNotFoundException("Could not find AppointmentMedicine with the given appointmentId and medicineId!");
    }

    // remove

    public static <T extends Identifiable> void removeFrom(Set<T> identifiableSet, String id, File resultOutputFile) {
        identifiableSet.removeIf(identifiable -> identifiable.getId().equals(id));
        Database.saveRecords(identifiableSet, resultOutputFile);
    }

    public static void removeManager(String managerId) { removeFrom(managers, managerId, managerFile); }

    public static void removeStaff(String staffId, boolean removeAllDependencies) {
        removeFrom(staffs, staffId, managerFile);
        if (removeAllDependencies) {
            removeCustomerFeedbackByNonManagerEmployeeId(staffId);
        }
    }

    public static void removeDoctor(String doctorId, boolean removeAllDependencies) {
        removeFrom(doctors, doctorId, doctorFile);
        if (removeAllDependencies) {
            removeCustomerFeedbackByNonManagerEmployeeId(doctorId);
            removeAppointmentByDoctorId(doctorId);
        }
    }

    public static void removeCustomer(String customerId, boolean removeAllDependencies) {
        removeFrom(customers, customerId, customerFile);
        if (removeAllDependencies) {
            removeAppointmentByCustomerId(customerId);
            removeCustomerFeedbackByCustomerId(customerId);
        }
    }

    public static void removeAppointment(String appointmentId, boolean removeAllDependencies) {
        removeFrom(appointments, appointmentId, appointmentFile);
        if (removeAllDependencies) {
            removeAppointmentMedicineByAppointmentId(appointmentId);
            removeInvoiceByAppointmentId(appointmentId);
        }
    }

    public static void removeMedicine(String medicineId, boolean removeAllDependencies) {
        removeFrom(medicines, medicineId, medicineFile);
        if (removeAllDependencies) {
            removeAppointmentMedicineByMedicineId(medicineId);
        }
    }

    public static void removeCustomerFeedback(String customerFeedbackId) { removeFrom(customerFeedbacks, customerFeedbackId, customerFeedbackFile); }

    public static void removeInvoice(String invoiceId) { removeFrom(invoices, invoiceId, customerFeedbackFile); }

    public static void removeAppointmentMedicine(String appointmentId, String medicineId) {
        appointmentMedicines.removeIf(appointmentMedicine ->
                appointmentMedicine.getAppointmentId().equals(appointmentId) &&
                appointmentMedicine.getMedicineId().equals(medicineId)
        );
        Database.saveRecords(appointmentMedicines, appointmentMedicineFile);
    }

    // Basic utility methods

    private static <T extends Identifiable> Set<String> getAllIdOf(Set<T> identifiableSet) {
        Set<String> allIdOf = new LinkedHashSet<>();
        for (Identifiable identifiable: identifiableSet) {
            allIdOf.add(identifiable.getId());
        }
        return allIdOf;
    }

    public static Set<String> getAllManagerId() { return getAllIdOf(managers); }

    public static Set<String> getAllStaffId() { return getAllIdOf(staffs); }

    public static Set<String> getAllDoctorId() { return getAllIdOf(doctors); }

    public static Set<String> getAllCustomerId() { return getAllIdOf(customers); }

    public static Set<String> getAllAppointmentId() { return getAllIdOf(appointments); }

    public static Set<String> getAllMedicineId() { return getAllIdOf(medicines); }

    public static Set<String> getAllCustomerFeedbackId() { return getAllIdOf(customerFeedbacks); }

    public static Set<String> getAllInvoiceId() { return getAllIdOf(invoices); }

    public static Set<List<String>> getAllPrescriptionInfo() {
        Set<List<String>> allPrescriptionInfo = new LinkedHashSet<>();
        for (AppointmentMedicine appointmentMedicine: appointmentMedicines) {
            allPrescriptionInfo.add(Arrays.asList(appointmentMedicine.getAppointmentId(), appointmentMedicine.getMedicineId()));
        }
        return allPrescriptionInfo;
    }

    // Additional utility methods

    public static String getUserIdByEmail(String email) {
        Set<User> users = new LinkedHashSet<>();
        users.addAll(managers);
        users.addAll(staffs);
        users.addAll(doctors);
        users.addAll(customers);

        for (User user: users) {
            if (user.getEmail().equals(email)) {
                return user.getId();
            }
        }

        throw new EmailNotFoundException("--- Database.getUserIdWithEmail(String) failed. Could not find User with the given email ---");
    }

    private static <T extends Entity> Set<String> getAllFieldValueOf(Set<T> savableSet, FieldValueReturner<T> fieldValueReturner) {
        Set<String> allFieldValueOf = new LinkedHashSet<>();
        for (T savable: savableSet) {
            allFieldValueOf.add(fieldValueReturner.getFieldValue(savable));
        }
        return allFieldValueOf;
    }

    public static Set<String> getAllAppointmentIdInInvoices() { return getAllFieldValueOf(invoices, Invoice::getAppointmentId); }

    public static Set<String> getAllUserEmails() {
        Set<User> users = new LinkedHashSet<>();
        users.addAll(managers);
        users.addAll(staffs);
        users.addAll(doctors);
        users.addAll(customers);

        return getAllFieldValueOf(users, User::getEmail);
    }

    public static Set<String> getAllMedicineNames() { return getAllFieldValueOf(medicines, Medicine::getName); }

    private static <T extends Identifiable> Set<String> getAllIdOfWhereCondition(Set<T> identifiableSet, String fieldValue, FieldValueReturner<T> fieldValueReturner) {
        Set<String> allIdOfWhereCondition = new LinkedHashSet<>();
        for (T identifiable: identifiableSet) {
            if (fieldValueReturner.getFieldValue(identifiable).equals(fieldValue)) {
                allIdOfWhereCondition.add(identifiable.getId());
            }
        }
        return allIdOfWhereCondition;
    }

    public static Set<String> getAllAppointmentIdOfDoctor(String doctorId) { return getAllIdOfWhereCondition(appointments, doctorId, Appointment::getDoctorId); }

    public static Set<String> getAllAppointmentIdOfCustomer(String customerId) { return getAllIdOfWhereCondition(appointments, customerId, Appointment::getCustomerId); }

    public static Set<String> getAllCustomerFeedbackIdOfNonManagerEmployee(String nonManagerEmployeeId) { return getAllIdOfWhereCondition(customerFeedbacks, nonManagerEmployeeId, CustomerFeedback::getNonManagerEmployeeId); }

    public static Set<String> getAllCustomerFeedbackIdOfCustomer(String customerId) { return getAllIdOfWhereCondition(customerFeedbacks, customerId, CustomerFeedback::getCustomerId); }

    public static Set<List<String>> getAllPrescriptionInfoOfDoctor(String doctorId) {
        Set<List<String>> allPrescriptionInfoOfDoctor = new LinkedHashSet<>();
        for (String appointmentId: Database.getAllAppointmentIdOfDoctor(doctorId)) {
            for (List<String> prescriptionInfo: Database.getAllPrescriptionInfo()) {
                if (prescriptionInfo.getFirst().equals(appointmentId)) {
                    allPrescriptionInfoOfDoctor.add(prescriptionInfo);
                }
            }
        }
        return allPrescriptionInfoOfDoctor;
    }

    public static Set<List<String>> getAllPrescriptionInfoOfCustomer(String customerId) {
        Set<List<String>> allPrescriptionInfoOfCustomer = new LinkedHashSet<>();
        for (String appointmentId: Database.getAllAppointmentIdOfCustomer(customerId)) {
            for (List<String> prescriptionInfo: Database.getAllPrescriptionInfo()) {
                if (prescriptionInfo.getFirst().equals(appointmentId)) {
                    allPrescriptionInfoOfCustomer.add(prescriptionInfo);
                }
            }
        }
        return allPrescriptionInfoOfCustomer;
    }

    public static Set<String> getAllInvoiceIdOfCustomer(String customerId) {
        Set<String> allAppointmentIdInInvoices = getAllAppointmentIdInInvoices();
        Set<String> allAppointmentIdOfCustomer = getAllAppointmentIdOfCustomer(customerId);
        Set<String> allAppointmentIdOfCustomerWithInvoice = new HashSet<>(allAppointmentIdInInvoices);
        allAppointmentIdOfCustomerWithInvoice.retainAll(allAppointmentIdOfCustomer);
        return getAllInvoiceId().stream().filter(
                invoiceId -> allAppointmentIdOfCustomerWithInvoice.contains(Database.getInvoice(invoiceId).getAppointmentId())
        ).collect(Collectors.toSet());
    }

    public static double getTotalMedicineChargesOfAppointment(String appointmentId) {
        double totalMedicineChargesOfAppointment = 0;
        for (AppointmentMedicine appointmentMedicine: appointmentMedicines) {
            if (appointmentMedicine.getAppointmentId().equals(appointmentId)) {
                Medicine medicineForAppointment = getMedicine(appointmentMedicine.getMedicineId());
                totalMedicineChargesOfAppointment += medicineForAppointment.getCharge();
            }
        }
        return totalMedicineChargesOfAppointment;
    }

    public static Set<List<String>> getAllPublicRecordsOf(Set<String> idSet, IdentifiableReturner identifiableReturner) {
        Set<List<String>> allPublicRecordsOf = new LinkedHashSet<>();
        for (String id: idSet) {
            allPublicRecordsOf.add(identifiableReturner.getIdentifiable(id).createPublicRecord());
        }
        return allPublicRecordsOf;
    }

    // remove dependencies

    private static <T extends Entity> void removeLeafDependenciesFrom(Set<T> leafSet, String foreignKeyId, FieldValueReturner<T> fieldValueReturner, File resultOutputFile) {
        leafSet.removeIf(leaf -> fieldValueReturner.getFieldValue(leaf).equals(foreignKeyId));
        Database.saveRecords(leafSet, resultOutputFile);
    }

    private static <T extends Identifiable> void removeRootDependenciesFrom(Set<T> rootSet, String foreignKeyId, FieldValueReturner<T> fieldValueReturner, IdentifiableRemover identifiableRemover, File resultOutputFile) {
        List<T> rootsToRemove = new ArrayList<>();
        for (T root: rootSet) {
            if (fieldValueReturner.getFieldValue(root).equals(foreignKeyId)) {
                rootsToRemove.add(root);
            }
        }
        for (T root: rootsToRemove) {
            identifiableRemover.removeIdentifiable(root.getId(), true);
        }
        Database.saveRecords(rootSet, resultOutputFile);
    }

    public static void removeAppointmentMedicineByAppointmentId(String appointmentId) { removeLeafDependenciesFrom(appointmentMedicines, appointmentId, AppointmentMedicine::getAppointmentId, appointmentMedicineFile); }

    public static void removeAppointmentMedicineByMedicineId(String medicineId) { removeLeafDependenciesFrom(appointmentMedicines, medicineId, AppointmentMedicine::getMedicineId, appointmentMedicineFile); }

    public static void removeCustomerFeedbackByCustomerId(String customerId) { removeLeafDependenciesFrom(customerFeedbacks, customerId, CustomerFeedback::getCustomerId, customerFeedbackFile);}

    public static void removeCustomerFeedbackByNonManagerEmployeeId(String nonManagerEmployeeId) { removeLeafDependenciesFrom(customerFeedbacks, nonManagerEmployeeId, CustomerFeedback::getNonManagerEmployeeId, customerFeedbackFile);}

    public static void removeInvoiceByAppointmentId(String appointmentId) { removeLeafDependenciesFrom(invoices, appointmentId, Invoice::getAppointmentId, invoiceFile); }

    public static void removeAppointmentByCustomerId(String customerId) { removeRootDependenciesFrom(appointments, customerId, Appointment::getCustomerId, Database::removeAppointment, appointmentFile);}

    public static void removeAppointmentByDoctorId(String doctorId) { removeRootDependenciesFrom(appointments, doctorId, Appointment::getDoctorId, Database::removeAppointment, appointmentFile);}

    private static void populateFromRecords(InstantiatableFromRecord instantiatableFromRecord, File inputFile) {
        try {
            try (Scanner fileScanner = new Scanner(inputFile)) {
                while (fileScanner.hasNextLine()) {
                    List<String> record = new ArrayList<>(
                            Arrays.asList(fileScanner.nextLine().split(","))
                    );
                    instantiatableFromRecord.createInstanceFromRecord(record);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static void populate() {
        populateFromRecords(Manager::createManagerFromDbRecord, managerFile);
        populateFromRecords(Staff::createStaffFromRecord, staffFile);
        populateFromRecords(Doctor::createDoctorFromRecord, doctorFile);
        populateFromRecords(Customer::createCustomerFromRecord, customerFile);
        populateFromRecords(Appointment::createAppointmentFromRecord, appointmentFile);
        populateFromRecords(Medicine::createMedicineFromRecord, medicineFile);
        populateFromRecords(AppointmentMedicine::createAppointmentMedicineFromRecord, appointmentMedicineFile);
        populateFromRecords(CustomerFeedback::createCustomerFeedbackFromRecord, customerFeedbackFile);
        populateFromRecords(Invoice::createInvoiceFromRecord, invoiceFile);
    }


    public static void saveRecords(Set<? extends Entity> entityContainer, File outputFile) {
        try {
            try (FileWriter fileWriter = new FileWriter(outputFile)) {
                List<String> entityRecords = new ArrayList<>();
                for (Entity entity: entityContainer) {
                    String dbEntityRecord = String.join(",", entity.createDbRecord());
                    entityRecords.add(dbEntityRecord);
                }
                String dbEntityRecords = String.join("\n", entityRecords);

                fileWriter.write(dbEntityRecords);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void save() {
        saveRecords(managers, managerFile);
        saveRecords(staffs, staffFile);
        saveRecords(doctors, doctorFile);
        saveRecords(customers, customerFile);
        saveRecords(appointments, appointmentFile);
        saveRecords(medicines, medicineFile);
        saveRecords(appointmentMedicines, appointmentMedicineFile);
        saveRecords(customerFeedbacks, customerFeedbackFile);
        saveRecords(invoices, invoiceFile);
    }
}