package database;

import user.*;
import operation.*;
import customExceptions.*;

import java.io.File;
import java.io.IOException;
import java.time.Month;
import java.util.*;

import java.io.FileWriter;
import java.util.stream.Collectors;

public final class Database {
    private final static String dataFolderPath = "data";
    private final static File managerFile = new File(dataFolderPath + "/manager.txt");
    private final static File staffFile = new File(dataFolderPath + "/staff.txt");
    private final static File doctorFile = new File(dataFolderPath + "/doctor.txt");
    private final static File customerFile = new File(dataFolderPath + "/customer.txt");
    private final static File appointmentFile = new File(dataFolderPath + "/appointment.txt");
    private final static File medicineFile = new File(dataFolderPath + "/medicine.txt");
    private final static File appointmentMedicineFile = new File(dataFolderPath + "/appointmentmedicine.txt");
    private final static File customerFeedbackFile = new File(dataFolderPath + "/customerFeedback.txt");
    private final static File invoiceFile = new File(dataFolderPath + "/invoice.txt");

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
        String rootManagerDbRecord = "M001,Root Manager,root@email.com,123";
        try {
            File dataFolder = new File(dataFolderPath);
            if (!dataFolder.exists()) {
                dataFolder.mkdirs(); // Create the data folder if it doesn't exist
            }
            for (File file: new File[] {managerFile, staffFile, doctorFile, customerFile, appointmentFile, medicineFile, appointmentMedicineFile, customerFeedbackFile, invoiceFile}) {
                if (!file.exists()) {
                    file.createNewFile(); // Create the text file in the data folder if it doesn't exist
                }
                if (file == managerFile && !new Scanner(file).hasNextLine()) {  // Write the root manager record to manager.txt if the file is empty
                    try (FileWriter managerFileWriter = new FileWriter(managerFile)) {
                        managerFileWriter.write(rootManagerDbRecord);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        // Create Entity sets that will store Entity objects created from text file records

        // Ordered by ID Number
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
        // Ordered by Status -> ID Number
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
        // Ordered by Appointment ID -> Medicine ID
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
        Database.populate(); // Fill the sets with objects created from text file records
    }

    // Add

    // Add new Manager to Manager set and auto save to manager.txt
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

    // Get

    // Return an Identifiable object using its ID, searched from the Identifiable set
    // String identifiableType is used for error message customization
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

    // Appointment is not an Identifiable (composite primary key, no single ID can identify it)
    public static AppointmentMedicine getAppointmentMedicine(String appointmentId, String medicineId) {
        for (AppointmentMedicine appointmentMedicine: appointmentMedicines) {
            // Both appointment ID and medicine ID must match
            if (appointmentMedicine.getAppointmentId().equals(appointmentId) && appointmentMedicine.getMedicineId().equals(medicineId)) {
                return appointmentMedicine;
            }
        }
        throw new IdNotFoundException("Could not find prescription with the given appointment ID and medicine ID!");
    }

    // Remove

    // Remove an Identifiable object from the Identifiable set using its ID, and auto save the new set to a file
    public static <T extends Identifiable> void removeFrom(Set<T> identifiableSet, String id, File resultOutputFile) {
        identifiableSet.removeIf(identifiable -> identifiable.getId().equals(id));
        Database.saveRecords(identifiableSet, resultOutputFile);
    }

    public static void removeManager(String managerId) {
        // Restrict deletion of root manager (ID of M001)
        if (managerId.equals("M001")) {
            throw new RootManagerUnmodifiableException("Root manager is unmodifiable!");
        }
        removeFrom(managers, managerId, managerFile);
    }

    // Dependencies of a record are defined as external records that references the record's primary key, typically in a foreign key column
    // If removeAllDependencies is true, all dependencies of the record being removed will also be removed, to ensure data integrity
    // removeAllDependencies can be false if the removal is part of updating the record (to be followed by add)
    public static void removeStaff(String staffId, boolean removeAllDependencies) {
        removeFrom(staffs, staffId, staffFile); // Remove the staff record by staff ID first
        if (removeAllDependencies) {
            removeCustomerFeedbackByNonManagerEmployeeId(staffId); // Remove all customer feedbacks that has references to the staff ID
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

    // No references made to the primary / composite primary key of manager, customer feedback, invoice and prescription
    public static void removeAppointmentMedicine(String appointmentId, String medicineId) {
        appointmentMedicines.removeIf(appointmentMedicine ->
                appointmentMedicine.getAppointmentId().equals(appointmentId) &&
                        appointmentMedicine.getMedicineId().equals(medicineId)
        );
        Database.saveRecords(appointmentMedicines, appointmentMedicineFile);
    }
    public static void removeCustomerFeedback(String customerFeedbackId) { removeFrom(customerFeedbacks, customerFeedbackId, customerFeedbackFile); }
    public static void removeInvoice(String invoiceId) { removeFrom(invoices, invoiceId, customerFeedbackFile); }

    // Basic utility methods

    // Get all IDs of Identifiable objects in the Identifiable set
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

    // Prescription info includes appointment ID and medicine ID
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

        throw new EmailNotFoundException("Could not find user with the given email!");
    }

    // Get values of a specific instance field of all Entity objects in an Entity set
    private static <T extends Entity> Set<String> getAllFieldValueOf(Set<T> entitySet, FieldValueReturner<T> fieldValueReturner) { return entitySet.stream().map(fieldValueReturner::getFieldValue).collect(Collectors.toCollection(LinkedHashSet::new)); }

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

    // Get all IDs of Identifiable objects in an Identifiable set, given (where) the value of a specific instance field matches
    // Simulate the WHERE keyword for filtering records in actual databases
    private static <T extends Identifiable> Set<String> getAllIdOfWhereCondition(Set<T> identifiableSet, String fieldValue, FieldValueReturner<T> fieldValueReturner) { return identifiableSet.stream().filter(identifiable -> fieldValueReturner.getFieldValue(identifiable).equals(fieldValue)).map(Identifiable::getId).collect(Collectors.toCollection(LinkedHashSet::new)); }

    // Get all appointment IDs of appointments with the doctor ID (WHERE getDoctorId() == doctorId)
    public static Set<String> getAllAppointmentIdOfDoctor(String doctorId) { return getAllIdOfWhereCondition(appointments, doctorId, Appointment::getDoctorId); }
    public static Set<String> getAllAppointmentIdOfCustomer(String customerId) { return getAllIdOfWhereCondition(appointments, customerId, Appointment::getCustomerId); }
    public static Set<String> getAllCustomerFeedbackIdOfCustomer(String customerId) { return getAllIdOfWhereCondition(customerFeedbacks, customerId, CustomerFeedback::getCustomerId); }

    public static double getTotalMedicineChargesOfAppointment(String appointmentId) { return appointmentMedicines.stream().filter(appointmentMedicine -> appointmentMedicine.getAppointmentId().equals(appointmentId)).mapToDouble(appointmentMedicine -> getMedicine(appointmentMedicine.getMedicineId()).getCharge()).sum(); }
    public static Set<String> getAllInvoiceIdOfMonth(Month month) { return invoices.stream().filter(invoice -> invoice.getCreationDate().getMonth().equals(month)).map(Invoice::getId).collect(Collectors.toCollection(LinkedHashSet::new)); }

    // Get all public records of Entity objects in an Entity set
    public static <T extends Entity> List<List<String>> getAllPublicRecordsOf(Set<T> entitySet) { return entitySet.stream().map(Entity::createPublicRecord).collect(Collectors.toCollection(ArrayList::new)); }

    public static List<List<String>> getAllManagerPublicRecords() { return getAllPublicRecordsOf(managers); }
    public static List<List<String>> getAllStaffPublicRecords() { return getAllPublicRecordsOf(staffs); }
    public static List<List<String>> getAllDoctorPublicRecords() { return getAllPublicRecordsOf(doctors); }
    public static List<List<String>> getAllCustomerPublicRecords() { return getAllPublicRecordsOf(customers); }
    public static List<List<String>> getAllAppointmentPublicRecords() { return getAllPublicRecordsOf(appointments); }
    public static List<List<String>> getAllMedicinePublicRecords() { return getAllPublicRecordsOf(medicines); }
    public static List<List<String>> getAllAppointmentMedicinePublicRecords() { return  getAllPublicRecordsOf(appointmentMedicines); }
    public static List<List<String>> getAllCustomerFeedbackPublicRecords() { return getAllPublicRecordsOf(customerFeedbacks); }
    public static List<List<String>> getAllInvoicePublicRecords() { return getAllPublicRecordsOf(invoices); }
    public static List<List<String>> getAllAppointmentPublicRecordsOfDoctor(String doctorId) { return getAllPublicRecordsOf(appointments.stream().filter(appointment -> appointment.getDoctorId().equals(doctorId)).collect(Collectors.toCollection(LinkedHashSet::new))); }
    public static List<List<String>> getAllAppointmentPublicRecordsOfCustomer(String customerId) { return getAllPublicRecordsOf(appointments.stream().filter(appointment -> appointment.getCustomerId().equals(customerId)).collect(Collectors.toCollection(LinkedHashSet::new))); }
    public static List<List<String>> getAllCustomerFeedbackPublicRecordsOfNonManagerEmployee(String nonManagerEmployeeId) { return getAllPublicRecordsOf(customerFeedbacks.stream().filter(customerFeedback -> customerFeedback.getNonManagerEmployeeId().equals(nonManagerEmployeeId)).collect(Collectors.toCollection(LinkedHashSet::new))); }
    public static List<List<String>> getAllCustomerFeedbackPublicRecordsOfCustomer(String customerId) { return getAllPublicRecordsOf(customerFeedbacks.stream().filter(customerFeedback -> customerFeedback.getCustomerId().equals(customerId)).collect(Collectors.toCollection(LinkedHashSet::new))); }
    public static List<List<String>> getAllAppointmentMedicinePublicRecordsOfDoctor(String doctorId) { return getAllPublicRecordsOf(appointmentMedicines.stream().filter(appointmentMedicine -> getAllAppointmentIdOfDoctor(doctorId).contains(appointmentMedicine.getAppointmentId())).collect(Collectors.toCollection(LinkedHashSet::new))); }
    public static List<List<String>> getAllAppointmentMedicinePublicRecordsOfCustomer(String customerId) { return getAllPublicRecordsOf(appointmentMedicines.stream().filter(appointmentMedicine -> getAllAppointmentIdOfCustomer(customerId).contains(appointmentMedicine.getAppointmentId())).collect(Collectors.toCollection(LinkedHashSet::new))); }
    public static List<List<String>> getAllInvoicePublicRecordsOfCustomer(String customerId) { return getAllPublicRecordsOf(invoices.stream().filter(invoice -> getAllAppointmentIdOfCustomer(customerId).contains(invoice.getAppointmentId())).collect(Collectors.toCollection(LinkedHashSet::new))); }

    // Remove dependencies

    // Leaf dependencies are defined here as dependencies whose primary key is not referenced by external records
    // Can be directly removed
    
    // Removes Entity objects from the Entity set, given the value of a specific instance field of the object matches, save the new set to a file
    private static <T extends Entity> void removeLeafDependenciesFrom(Set<T> leafSet, String fieldValue, FieldValueReturner<T> fieldValueReturner, File resultOutputFile) {
        leafSet.removeIf(leaf -> fieldValueReturner.getFieldValue(leaf).equals(fieldValue));
        Database.saveRecords(leafSet, resultOutputFile);
    }

    // Root dependencies are defined here as dependencies whose primary key is referenced by external records (sub-dependencies)
    // Removal of these dependencies requires a "chain reaction" that also removes their dependencies, and so on if needed
    
    // Removes Identifiable objects from the Identifiable set, given the value of a specific instance field (restricted to ID-based instance fields only) of the object matches (to be removed PK found FK match, confirmed is dependency), save the new set to a file
    // Restriction to only ID-based instance fields for referential checking is because root dependency removal uses DependableIdentifiableRemover.removeDependableIdentifiable(String id, boolean removeAllDependencies)
    private static <T extends Identifiable> void removeRootDependenciesFrom(Set<T> rootSet, String idFieldValue, FieldValueReturner<T> fieldValueReturner, DependableIdentifiableRemover dependableIdentifiableRemover, File resultOutputFile) {
        List<T> rootsToRemove = new ArrayList<>();
        for (T root: rootSet) {
            if (fieldValueReturner.getFieldValue(root).equals(idFieldValue)) {
                rootsToRemove.add(root);
            }
        }
        for (T root: rootsToRemove) {
            dependableIdentifiableRemover.removeDependableIdentifiable(root.getId(), true);
        }
        Database.saveRecords(rootSet, resultOutputFile);
    }


    // Remove prescriptions with the given appointment ID (no external references to composite primary key = leaf dependency)
    public static void removeAppointmentMedicineByAppointmentId(String appointmentId) { removeLeafDependenciesFrom(appointmentMedicines, appointmentId, AppointmentMedicine::getAppointmentId, appointmentMedicineFile); }
    public static void removeAppointmentMedicineByMedicineId(String medicineId) { removeLeafDependenciesFrom(appointmentMedicines, medicineId, AppointmentMedicine::getMedicineId, appointmentMedicineFile); }
    public static void removeCustomerFeedbackByCustomerId(String customerId) { removeLeafDependenciesFrom(customerFeedbacks, customerId, CustomerFeedback::getCustomerId, customerFeedbackFile);}
    public static void removeCustomerFeedbackByNonManagerEmployeeId(String nonManagerEmployeeId) { removeLeafDependenciesFrom(customerFeedbacks, nonManagerEmployeeId, CustomerFeedback::getNonManagerEmployeeId, customerFeedbackFile);}
    public static void removeInvoiceByAppointmentId(String appointmentId) { removeLeafDependenciesFrom(invoices, appointmentId, Invoice::getAppointmentId, invoiceFile); }

    // Remove appointments with the given customer ID (has external references to primary key appointment ID in invoices = root dependency)
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
            throw new RuntimeException(e.getMessage());
        }
    }
}