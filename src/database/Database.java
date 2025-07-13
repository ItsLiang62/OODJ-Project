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
    private final static File customerFeedbackFile = new File("data/customerFeedback.txt");
    private final static File medicineFile = new File("data/medicine.txt");
    private final static File invoiceFile = new File("data/invoice.txt");

    private static Queue<Manager> managers;
    private static Queue<Staff> staffs;
    private static Queue<Doctor> doctors;
    private static Queue<Customer> customers;
    private static Queue<Appointment> appointments;
    private static Queue<CustomerFeedback> customerFeedbacks;
    private static Queue<Medicine> medicines;
    private static Queue<Invoice> invoices;

    static {
        managers = new PriorityQueue<>(Comparator.comparingInt(
                user -> Integer.parseInt(user.getId().substring(1))
        ));
        staffs = new PriorityQueue<>(Comparator.comparingInt(
                user -> Integer.parseInt(user.getId().substring(1))
        ));
        doctors = new PriorityQueue<>(Comparator.comparingInt(
                user -> Integer.parseInt(user.getId().substring(1))
        ));
        customers = new PriorityQueue<>(Comparator.comparingInt(
                user -> Integer.parseInt(user.getId().substring(1))
        ));

        Map<String, Integer> appointmentStatusPriority = new HashMap<>();
        appointmentStatusPriority.put("Pending", 1);
        appointmentStatusPriority.put("Confirmed", 2);
        appointmentStatusPriority.put("Completed", 3);
        appointments = new PriorityQueue<>(Comparator.comparingInt(
                appointment -> appointmentStatusPriority.get(appointment.getStatus())
        ));
        customerFeedbacks = new PriorityQueue<>(Comparator.comparingInt(
                customerFeedback -> Integer.parseInt(customerFeedback.getId().substring(1))
        ));
        medicines = new PriorityQueue<>(Comparator.comparingInt(
                medicine -> Integer.parseInt(medicine.getId().substring(1))
        ));
        invoices = new PriorityQueue<>(Comparator.comparingInt(
                invoice -> Integer.parseInt(invoice.getId().substring(1))
        ));
        try {
            Database.populate();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addManager(Manager newManager) {
        if (getAllManagerId().contains(newManager.getId())) {
            throw new RecordAlreadyInDatabaseException("--- Database.addManager(Manager) failed. Manager already in database ---");
        }
        managers.add(newManager);
    }

    public static void addStaff(Staff newStaff) {
        if (getAllStaffId().contains(newStaff.getId())) {
            throw new RecordAlreadyInDatabaseException("--- Database.addStaff(Staff) failed. Staff already in database ---");
        }
        staffs.add(newStaff);
    }

    public static void addDoctor(Doctor newDoctor) {
        if (getAllDoctorId().contains(newDoctor.getId())) {
            throw new RecordAlreadyInDatabaseException("--- Database.addDoctor(Doctor) failed. Doctor already in database ---");
        } else {
            doctors.add(newDoctor);
        }
    }

    public static void addCustomer(Customer newCustomer) {
        if (getAllCustomerId().contains(newCustomer.getId())) {
            throw new RecordAlreadyInDatabaseException("--- Database.addCustomer(Customer) failed. Customer already in database ---");
        } else {
            customers.add(newCustomer);
        }
    }

    public static void addAppointment(Appointment newAppointment) {
        if (getAllAppointmentId().contains(newAppointment.getId())) {
            throw new RecordAlreadyInDatabaseException("--- Database.addAppointment(Appointment) failed. Appointment already in database ---");
        } else {
            appointments.add(newAppointment);
        }
    }

    public static void addCustomerFeedback(CustomerFeedback newCustomerFeedback) {
        if (getAllCustomerFeedbackId().contains(newCustomerFeedback.getId())) {
            throw new RecordAlreadyInDatabaseException("--- Database.addCustomerFeedback(CustomerFeedback) failed. CustomerFeedback already in database ---");
        } else {
            customerFeedbacks.add(newCustomerFeedback);
        }
    }

    public static void addMedicine(Medicine newMedicine) {

    }

    public static void addInvoice(Invoice newInvoice) {

    }

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

    public static CustomerFeedback getCustomerFeedback(String customerFeedbackId) {
        for (CustomerFeedback customerFeedback: customerFeedbacks) {
            if (customerFeedback.getId().equals(customerFeedbackId)) {
                return customerFeedback;
            }
        }
        throw new IdNotFoundException("--- Database.getCustomerFeedback(String) failed. Could not find CustomerFeedback with the given customerFeedbackId ---");
    }

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

    public static Set<String> getAllCustomerFeedbackId() {
        Set<String> allCustomerFeedbackId = new LinkedHashSet<>();
        for (CustomerFeedback customerFeedback: customerFeedbacks) {
            allCustomerFeedbackId.add(customerFeedback.getId());
        }
        return allCustomerFeedbackId;
    }

    public static Set<String> getAllMedicineId() {
        Set<String> allMedicineId = new LinkedHashSet<>();
        for (Medicine medicine: medicines) {
            allMedicineId.add(medicine.getId());
        }
        return allMedicineId;
    }

    public static Set<String> getAllInvoiceId() {
        Set<String> allInvoiceId = new LinkedHashSet<>();
        for (Invoice invoice: invoices) {
            allInvoiceId.add(invoice.getId());
        }
        return allInvoiceId;
    }

    public static Set<String> getAllAppointmentIdInInvoices() {
        Set<String> allAppointmentIdInInvoices = new LinkedHashSet<>();
        for (Invoice invoice: invoices) {
            allAppointmentIdInInvoices.add(invoice.getAppointmentId());
        }
        return allAppointmentIdInInvoices;
    }

    public static List<String> getAllUpcomingAppointmentCustomerId() {
        Set<String> allNotCompletedAppointmentCustomerId = new LinkedHashSet<>();

        for (Appointment appointment: appointments) {
            if (!appointment.getStatus().equals("Completed")) {
                allNotCompletedAppointmentCustomerId.add(appointment.getCustomerId());
            }
        }

        return new ArrayList<>(allNotCompletedAppointmentCustomerId);
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

    private static <T extends Savable> void populateFromRecords(InstantiatableFromRecord<T> instantiatableFromRecord, Queue<T> objects, File inputFile) throws IOException {
        try (Scanner fileScanner = new Scanner(inputFile)) {
            while (fileScanner.hasNextLine()) {
                List<String> record = new ArrayList<>(
                        Arrays.asList(fileScanner.nextLine().split(","))
                );
                objects.add(instantiatableFromRecord.createInstanceFromRecord(record));
            }
        }
    }

    private static void populate() throws IOException {
        populateFromRecords(Manager::createManagerFromRecord, managers, managerFile);
        populateFromRecords(Staff::createStaffFromRecord, staffs, staffFile);
        populateFromRecords(Doctor::createDoctorFromRecord, doctors, doctorFile);
        populateFromRecords(Customer::createCustomerFromRecord, customers, customerFile);
        populateFromRecords(Appointment::createAppointmentFromRecord, appointments, appointmentFile);
        populateFromRecords(CustomerFeedback::createCustomerFeedbackFromRecord, customerFeedbacks, customerFeedbackFile);
        populateFromRecords(Medicine::createMedicineFromRecord, medicines, medicineFile);
        populateFromRecords(Invoice::createInvoiceFromRecord, invoices, invoiceFile);
    }


    public static void saveRecords(Queue<? extends Savable> objectContainer, File outputFile) throws IOException {
        try (FileWriter fileWriter = new FileWriter(outputFile)) {
            List<String> objectRecords = new ArrayList<>();
            for (Savable object: objectContainer) {
                String dbObjectRecord = String.join(",", object.createRecord());
                objectRecords.add(dbObjectRecord);
            }
            String dbObjectRecords = String.join("\n", objectRecords);

            fileWriter.write(dbObjectRecords);
        }
    }

    public static void save() throws IOException {
        saveRecords(managers, managerFile);
        saveRecords(staffs, staffFile);
        saveRecords(doctors, doctorFile);
        saveRecords(customers, customerFile);
        saveRecords(appointments, appointmentFile);
        saveRecords(customerFeedbacks, customerFeedbackFile);
        saveRecords(medicines, medicineFile);
        saveRecords(invoices, invoiceFile);
    }
}
