package database;

import user.*;
import operation.*;
import customExceptions.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

import java.io.FileWriter;

public final class Database {

    private final static File userFile = new File("data/user.txt");
    private final static File appointmentFile = new File("data/appointment.txt");
    private final static File customerFeedbackFile = new File("data/customerFeedback.txt");
    private final static File medicineFile = new File("data/medicine.txt");
    private final static File invoiceFile = new File("data/invoice.txt");

    private static Queue<User> users;
    private static Queue<Appointment> appointments;
    private static Queue<CustomerFeedback> customerFeedbacks;
    private static Queue<Medicine> medicines;
    private static Queue<Invoice> invoices;

    static {

        users = new PriorityQueue<>(Comparator.comparingInt(
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

    public static void addUser(User newUser) throws RecordAlreadyInDatabaseException {
        if (!getAllUserId().contains(newUser.getId())) {
            users.add(newUser);
            Account.addAccount(newUser.getEmail(), newUser.getPassword(), newUser.getId());
        } else {
            throw new RecordAlreadyInDatabaseException("--- Database.addUser(User) failed. User already in database ---");
        }
    }

    public static void addAppointment(Appointment newAppointment) throws RecordAlreadyInDatabaseException {
        if (getAllAppointmentId().contains(newAppointment.getId())) {
            throw new RecordAlreadyInDatabaseException("--- Database.addAppointment(Appointment) failed. Appointment already in database ---");
        } else if (getAllUpcomingAppointmentCustomerId().contains(newAppointment.getCustomerId())) {
            throw new MultipleAppointmentsForUserException("--- Database.addAppointment(Appointment) failed. Customer already has an upcoming appointment  ---");
        } else {
            appointments.add(newAppointment);
        }
    }

    public static User getUser(String userId) {
        for (User user: users) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    public static Appointment getAppointment(String appointmentId) {
        for (Appointment appointment: appointments) {
            if (appointment.getId().equals(appointmentId)) {
                return appointment;
            }
        }
        return null;
    }

    public static List<String> getAllUserId() {
        List<String> allUserId = new ArrayList<>();
        for (User user: users) {
            allUserId.add(user.getId());
        }
        return allUserId;
    }

    public static List<String> getAllAppointmentId() {
        List<String> allAppointmentId = new ArrayList<>();
        for (Appointment appointment: appointments) {
            allAppointmentId.add(appointment.getId());
        }
        return allAppointmentId;
    }

    public static List<String> getAllCustomerFeedbackId() {
        List<String> allCustomerFeedbackId = new ArrayList<>();
        for (CustomerFeedback customerFeedback: customerFeedbacks) {
            allCustomerFeedbackId.add(customerFeedback.getId());
        }
        return allCustomerFeedbackId;
    }

    public static List<String> getAllMedicineId() {
        List<String> allMedicineId = new ArrayList<>();
        for (Medicine medicine: medicines) {
            allMedicineId.add(medicine.getId());
        }
        return allMedicineId;
    }

    public static List<String> getAllInvoiceId() {
        List<String> allInvoiceId = new ArrayList<>();
        for (Invoice invoice: invoices) {
            allInvoiceId.add(invoice.getId());
        }
        return allInvoiceId;
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

    private static void populate() throws IOException {
        try (Scanner userFileScanner = new Scanner(userFile)) {
            while (userFileScanner.hasNextLine()) {
                List<String> userData = new ArrayList<>(
                        Arrays.asList(userFileScanner.nextLine().split(","))
                );
                //0
                String userId = userData.getFirst();
                String userName = userData.get(1);
                String userEmail = userData.getLast();
                String userPassword = Account.getPassword(userEmail);
                if (userId.charAt(0) == 'C') {
                    users.add(new Customer(userId, userName, userEmail, userPassword));
                } else if (userId.charAt(0) == 'D') {
                    users.add(new Doctor(userId, userName, userEmail, userPassword));
                } else if (userId.charAt(0) == 'M') {
                    users.add(new Manager(userId, userName, userEmail, userPassword));
                } else if (userId.charAt(0) == 'S') {
                    users.add(new Staff(userId, userName, userEmail, userPassword));
                } else {
                    throw new InvalidUserIdException(
                            String.format("--- Database.populate() failed when populating users. Encountered invalid user ID: %s ---", userId)
                    );
                }
                //2
            }
        }

        try (Scanner appointmentFileScanner = new Scanner(appointmentFile)) {
            while (appointmentFileScanner.hasNextLine()) {
                List<String> appointmentData = new ArrayList<>(
                        Arrays.asList(appointmentFileScanner.nextLine().split(","))
                );

                String id = appointmentData.getFirst();
                String doctorId;
                if (appointmentData.get(1).equalsIgnoreCase("NULL")) {
                    doctorId = null;
                } else {
                    doctorId = appointmentData.get(1);
                }
                String customerId = appointmentData.get(2);
                HashSet<String> medicineIds;
                if (appointmentData.get(3).equalsIgnoreCase("NULL")) {
                    medicineIds = null;
                } else {
                    medicineIds = new HashSet<>(
                            Arrays.asList(appointmentData.get(3).split("&"))
                    );
                }
                String doctorFeedback;
                if (appointmentData.get(4).equalsIgnoreCase("NULL")) {
                    doctorFeedback = null;
                } else {
                    doctorFeedback = appointmentData.get(4);
                }
                double charge = Double.parseDouble(appointmentData.get(5));
                String status = appointmentData.getLast();

                Appointment appointment = new Appointment(id, doctorId, customerId, medicineIds, doctorFeedback, charge, status);
                appointments.add(appointment);
            }
        }
    }

    public static void save() throws IOException {
        try (FileWriter userFileWriter = new FileWriter(userFile)) {
            List<String> userRecords = new ArrayList<>();
            for (User user: users) {
                List<String> userData = new ArrayList<>(Arrays.asList(
                        user.getId(), user.getName(), user.getEmail()
                ));
                String csUserData = String.join(",", userData);
                userRecords.add(csUserData);
            }
            String nsUserRecords = String.join("\n", userRecords);
            userFileWriter.write(nsUserRecords);
        }

        try (FileWriter appointmentFileWriter = new FileWriter(appointmentFile)) {
            List<String> appointmentRecords = new ArrayList<>();
            for (Appointment appointment: appointments) {
                String id = appointment.getId();
                String doctorId = appointment.getDoctorId();
                if (doctorId == null) {
                    doctorId = "NULL";
                }
                String customerId = appointment.getCustomerId();
                String medicineIds;
                try {
                    medicineIds = String.join("&", appointment.getMedicineIds());
                } catch (NullPointerException e) {
                    medicineIds = "NULL";
                }
                String doctorFeedback = appointment.getDoctorFeedback();
                if (doctorFeedback == null) {
                    doctorFeedback = "NULL";
                }
                String charge = String.valueOf(appointment.getCharge());
                String status = appointment.getStatus();
                List<String> appointmentData = new ArrayList<>(Arrays.asList(
                        id, doctorId, customerId, medicineIds, doctorFeedback, charge, status
                ));
                String csAppointmentData = String.join(",", appointmentData);
                appointmentRecords.add(csAppointmentData);
            }
            String nsAppointmentRecords = String.join("\n", appointmentRecords);
            appointmentFileWriter.write(nsAppointmentRecords);
        }

        try (FileWriter customerFeedbackFileWriter = new FileWriter(customerFeedbackFile)) {

        }

        try (FileWriter medicineFileWriter = new FileWriter(medicineFile)) {

        }

        try (FileWriter invoiceFileWriter = new FileWriter(invoiceFile)) {

        }
    }
}
