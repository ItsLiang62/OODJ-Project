package database;

import user.*;
import operation.*;
import customExceptions.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

import java.io.FileWriter;

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
                (Appointment appointment) -> appointmentStatusPriority.get(appointment.getStatus()))
                .thenComparing(
                Appointment::getId
        ));
        medicines = new TreeSet<>(Comparator.comparingInt(
                medicine -> Integer.parseInt(medicine.getId().substring(1))
        ));
        appointmentMedicines = new TreeSet<>(Comparator.comparingInt(
                appointmentMedicine -> Integer.parseInt(appointmentMedicine.getAppointmentId().substring(1))
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

    public static Manager getManager(String managerId) {
        for (Manager manager: managers) {
            if (manager.getId().equals(managerId)) {
                return manager;
            }
        }
        throw new IdNotFoundException("--- Database.getManager(String) failed. Could not find Manager with the given managerId ---");
    }

    public static Staff getStaff(String staffId) {
        for (Staff staff: staffs) {
            if (staff.getId().equals(staffId)) {
                return staff;
            }
        }
        throw new IdNotFoundException("--- Database.getStaff(String) failed. Could not find Staff with the given staffId ---");
    }

    public static Doctor getDoctor(String doctorId) {
        for (Doctor doctor: doctors) {
            if (doctor.getId().equals(doctorId)) {
                return doctor;
            }
        }
        throw new IdNotFoundException("--- Database.getDoctor(String) failed. Could not find Doctor with the given doctorId ---");
    }

    public static Customer getCustomer(String customerId) {
        for (Customer customer: customers) {
            if (customer.getId().equals(customerId)) {
                return customer;
            }
        }
        throw new IdNotFoundException("--- Database.getCustomer(String) failed. Could not find Customer with the given customerId ---");
    }

    public static Appointment getAppointment(String appointmentId) {
        for (Appointment appointment: appointments) {
            if (appointment.getId().equals(appointmentId)) {
                return appointment;
            }
        }
        throw new IdNotFoundException("--- Database.getAppointment(String) failed. Could not find Appointment with the given appointmentId ---");
    }

    public static AppointmentMedicine getAppointmentMedicine(String appointmentId, String medicineId) {
        for (AppointmentMedicine appointmentMedicine: appointmentMedicines) {
            if (appointmentMedicine.getAppointmentId().equals(appointmentId) && appointmentMedicine.getMedicineId().equals(medicineId)) {
                return appointmentMedicine;
            }
        }
        throw new IdNotFoundException("--- Database.getAppointmentMedicine(String) failed. Could not find AppointmentMedicine with the given appointmentId and medicineId ---");
    }

    public static CustomerFeedback getCustomerFeedback(String customerFeedbackId) {
        for (CustomerFeedback customerFeedback: customerFeedbacks) {
            if (customerFeedback.getId().equals(customerFeedbackId)) {
                return customerFeedback;
            }
        }
        throw new IdNotFoundException("--- Database.getCustomerFeedback(String) failed. Could not find CustomerFeedback with the given customerFeedbackId ---");
    }

    public static Invoice getInvoice(String invoiceId) {
        for (Invoice invoice: invoices) {
            if (invoice.getId().equals(invoiceId)) {
                return invoice;
            }
        }
        throw new IdNotFoundException("--- Database.getInvoice(String) failed. Could not find Invoice with the given invoiceId ---");
    }

    public static Medicine getMedicine(String medicineId) {
        for (Medicine medicine: medicines) {
            if (medicine.getId().equals(medicineId)) {
                return medicine;
            }
        }
        throw new IdNotFoundException("--- Database.getMedicine(String) failed. Could not find Medicine with the given medicineId ---");
    }

    // remove

    public static void removeManager(String managerId) {
        managers.removeIf(manager -> manager.getId().equals(managerId));
        Database.saveRecords(managers, managerFile);
    }

    public static void removeStaff(String staffId) {
        staffs.removeIf(staff -> staff.getId().equals(staffId));
        Database.saveRecords(staffs, staffFile);
    }

    public static void removeDoctor(String doctorId) {
        doctors.removeIf(doctor -> doctor.getId().equals(doctorId));
        Database.saveRecords(doctors, doctorFile);
    }

    public static void removeCustomer(String customerId) {
        customers.removeIf(customer -> customer.getId().equals(customerId));
        Database.saveRecords(customers, customerFile);
    }

    public static void removeAppointment(String appointmentId) {
        appointments.removeIf(appointment -> appointment.getId().equals(appointmentId));
        Database.saveRecords(appointments, appointmentFile);
    }

    public static void removeMedicine(String medicineId) {
        medicines.removeIf(medicine -> medicine.getId().equals(medicineId));
        Database.saveRecords(medicines, medicineFile);
    }

    public static void removeAppointmentMedicine(String appointmentId, String medicineId) {
        appointmentMedicines.removeIf(appointmentMedicine ->
                appointmentMedicine.getAppointmentId().equals(appointmentId) &&
                appointmentMedicine.getMedicineId().equals(medicineId)
        );
        Database.saveRecords(appointmentMedicines, appointmentMedicineFile);
    }

    public static void removeCustomerFeedback(String customerFeedbackId) {
        customerFeedbacks.removeIf(customerFeedback -> customerFeedback.getId().equals(customerFeedbackId));
        Database.saveRecords(customerFeedbacks, customerFeedbackFile);
    }

    public static void removeInvoice(String invoiceId) {
        invoices.removeIf(invoice -> invoice.getId().equals(invoiceId));
        Database.saveRecords(invoices, invoiceFile);
    }

    // Basic utility methods

    public static Set<String> getAllManagerId() {

        Set<String> allManagerId = new LinkedHashSet<>();
        for (Manager manager: managers) {
            allManagerId.add(manager.getId());
        }
        return allManagerId;
    }

    public static Set<String> getAllStaffId() {
        Set<String> allStaffId = new LinkedHashSet<>();
        for (Staff staff: staffs) {
            allStaffId.add(staff.getId());
        }
        return allStaffId;
    }

    public static Set<String> getAllDoctorId() {
        Set<String> allDoctorId = new LinkedHashSet<>();
        for (Doctor doctor: doctors) {
            allDoctorId.add(doctor.getId());
        }
        return allDoctorId;
    }

    public static Set<String> getAllCustomerId() {
        Set<String> allCustomerId = new LinkedHashSet<>();
        for (Customer customer: customers) {
            allCustomerId.add(customer.getId());
        }
        return allCustomerId;
    }

    public static Set<String> getAllAppointmentId() {
        Set<String> allAppointmentId = new LinkedHashSet<>();
        for (Appointment appointment: appointments) {
            allAppointmentId.add(appointment.getId());
        }
        return allAppointmentId;
    }

    public static Set<String> getAllMedicineId() {
        Set<String> allMedicineId = new LinkedHashSet<>();
        for (Medicine medicine: medicines) {
            allMedicineId.add(medicine.getId());
        }
        return allMedicineId;
    }

    public static Set<List<String>> getAllPrescriptionInfo() {
        Set<List<String>> allPrescriptionInfo = new LinkedHashSet<>();
        for (AppointmentMedicine appointmentMedicine: appointmentMedicines) {
            allPrescriptionInfo.add(Arrays.asList(appointmentMedicine.getAppointmentId(), appointmentMedicine.getMedicineId()));
        }
        return allPrescriptionInfo;
    }

    public static Set<String> getAllCustomerFeedbackId() {
        Set<String> allCustomerFeedbackId = new LinkedHashSet<>();
        for (CustomerFeedback customerFeedback: customerFeedbacks) {
            allCustomerFeedbackId.add(customerFeedback.getId());
        }
        return allCustomerFeedbackId;
    }

    public static Set<String> getAllInvoiceId() {
        Set<String> allInvoiceId = new LinkedHashSet<>();
        for (Invoice invoice: invoices) {
            allInvoiceId.add(invoice.getId());
        }
        return allInvoiceId;
    }

    // Additional utility methods

    public static String getUserIdWithEmail(String email) {
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

    public static Set<String> getAllAppointmentIdInInvoices() {
        Set<String> allAppointmentIdInInvoices = new LinkedHashSet<>();
        for (Invoice invoice: invoices) {
            allAppointmentIdInInvoices.add(invoice.getAppointmentId());
        }
        return allAppointmentIdInInvoices;
    }

    public static Set<String> getAllUserEmails() {
        Set<String> allUserEmails = new LinkedHashSet<>();

        List<User> users = new ArrayList<>();
        users.addAll(managers);
        users.addAll(staffs);
        users.addAll(doctors);
        users.addAll(customers);

        for (User user: users) {
            allUserEmails.add(user.getEmail());
        }
        return allUserEmails;
    }

    public static Set<String> getAllMedicineNames() {
        Set<String> allMedicineNames = new LinkedHashSet<>();

        for (Medicine medicine: medicines) {
            allMedicineNames.add(medicine.getName());
        }
        return allMedicineNames;
    }

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
        populateFromRecords(Manager::createManagerFromRecord, managerFile);
        populateFromRecords(Staff::createStaffFromRecord, staffFile);
        populateFromRecords(Doctor::createDoctorFromRecord, doctorFile);
        populateFromRecords(Customer::createCustomerFromRecord, customerFile);
        populateFromRecords(Appointment::createAppointmentFromRecord, appointmentFile);
        populateFromRecords(Medicine::createMedicineFromRecord, medicineFile);
        populateFromRecords(AppointmentMedicine::createAppointmentMedicineFromRecord, appointmentMedicineFile);
        populateFromRecords(CustomerFeedback::createCustomerFeedbackFromRecord, customerFeedbackFile);
        populateFromRecords(Invoice::createInvoiceFromRecord, invoiceFile);
    }


    public static void saveRecords(Set<? extends Savable> objectContainer, File outputFile) {
        try {
            try (FileWriter fileWriter = new FileWriter(outputFile)) {
                List<String> objectRecords = new ArrayList<>();
                for (Savable object: objectContainer) {
                    String dbObjectRecord = String.join(",", object.createRecord());
                    objectRecords.add(dbObjectRecord);
                }
                String dbObjectRecords = String.join("\n", objectRecords);

                fileWriter.write(dbObjectRecords);
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