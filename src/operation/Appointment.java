package operation;

import customExceptions.*;
import database.Database;
import database.Identifiable;
import user.Customer;
import user.Doctor;

import java.io.File;
import java.util.*;
import java.util.function.Function;

public class Appointment implements Identifiable {
    private final String id;
    private final String customerId;
    private String doctorId;
    private String doctorFeedback;
    private double consultationFee;
    private String status;

    private static final Set<Appointment> appointments;
    private static final File appointmentFile = new File("data/appointment.txt");

    static {
        // Ordered by Status -> ID Number
        Map<String, Integer> appointmentStatusPriority = new HashMap<>();
        appointmentStatusPriority.put("Pending", 1);
        appointmentStatusPriority.put("Confirmed", 2);
        appointmentStatusPriority.put("Completed", 3);
        appointments = new TreeSet<>(Comparator.comparingInt(
                (Appointment appointment) -> appointmentStatusPriority.get(appointment.getStatus())
        ).thenComparing(
                Database.getOrderedById()
        ));
        Database.populateFromRecords(Appointment::createFromDbRecord, appointmentFile);
    }

    public Appointment(String id, String customerId, String doctorId, String doctorFeedback, double consultationFee, String status) {
        checkCustomerId(customerId);
        checkDoctorId(doctorId);
        checkStatus(status);
        checkConsultationFee(consultationFee);

        this.id = id;
        this.customerId = customerId;
        this.doctorId = doctorId;
        this.doctorFeedback = doctorFeedback;
        this.consultationFee = consultationFee;
        this.status = status;

        Appointment.add(this);
    }


    public Appointment(String customerId) {
        this(Appointment.createId(), customerId, null, null, 0.0, "Pending");
    }

    public String getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getDoctorFeedback() {
        return doctorFeedback;
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public String getStatus() {
        return status;
    }

    public double getTotalMedicineCharges() {
        return AppointmentMedicine.getAll().stream().filter(
                appointmentMedicine -> appointmentMedicine.getAppointmentId().equals(this.id)
        ).mapToDouble(
                appointmentMedicine -> (Medicine.getById(appointmentMedicine.getMedicineId())).getCharge()
        ).sum();
    }

    public double getTotalCharge() {
        return getConsultationFee() + getTotalMedicineCharges();
    }

    public void setDoctorId(String doctorId) {
        if (status.equals("Completed")) {
            throw new AppointmentCompletedException(
                    "Appointment is completed and is not subject to any modification."
            );
        }
        checkDoctorId(doctorId);
        this.doctorId = doctorId;
        if (doctorId == null) { // Unassign a doctor
            status = "Pending";
        } else {
            status = "Confirmed";
        }
        Appointment.removeById(this.id, false);
        Appointment.add(this);
    }

    public void setDoctorFeedback(String doctorFeedback) {
        if (status.equals("Completed")) {
            throw new AppointmentCompletedException(
                    "Appointment is completed and is not subject to any modification."
            );
        }
        this.doctorFeedback = doctorFeedback;
        Appointment.removeById(id, false);
        Appointment.add(this);
    }

    public void setConsultationFee(double consultationFee) {
        if (status.equals("Completed")) {
            throw new AppointmentCompletedException(
                    "Appointment is completed and is not subject to any modification."
            );
        }
        checkConsultationFee(consultationFee);
        this.consultationFee = consultationFee;
        Appointment.removeById(id, false);
        Appointment.add(this);
    }

    public void setStatusToCompleted() {
        if (status.equals("Completed")) {
            throw new AppointmentCompletedException("Appointment is already completed.");
        }
        if (doctorId == null || !status.equals("Confirmed")) {
            throw new AppointmentNotCompletableException(
                    "Appointment is not completable. Make sure the appointment is confirmed and has a valid doctor ID.");
        } else {
            status = "Completed";
        }
        Appointment.removeById(this.id, false);
        Appointment.add(this);
    }

    public static void checkCustomerId(String customerId) {
        if (customerId == null || customerId.isBlank()) {
            throw new NullOrEmptyValueRejectedException("Customer ID of appointment object must not be null or empty!");
        }
        if (!Customer.getFieldVals(Customer.getAll(), Customer::getId).contains(customerId)) {
            throw new InvalidForeignKeyValueException("Customer ID does not exist!");
        }
    }

    public static void checkDoctorId(String doctorId) {
        if (doctorId != null && !Doctor.getFieldVals(Doctor.getAll(), Doctor::getId).contains(doctorId)) {
            throw new InvalidForeignKeyValueException("Doctor ID of appointment does not exist!");
        }
    }

    public static void checkStatus(String status) {
        if (!Arrays.asList(new String[] {"Pending", "Confirmed", "Completed"}).contains(status)) {
            throw new InvalidAppointmentStatusException("Status of appointment must be either Pending, Confirmed or Completed!");
        }
    }

    public static void checkConsultationFee(double consultationFee) {
        if (consultationFee < 0) {
            throw new NegativeValueRejectedException("Consultation fee of appointment must be equal or more than 0!");
        }
    }

    public List<String> createDbRecord() {
        String dbDoctorId = Objects.requireNonNullElse(doctorId, "NULL");
        String dbDoctorFeedback = Objects.requireNonNullElse(doctorFeedback, "NULL");
        String dbConsultationFee = String.valueOf(consultationFee);
        String dbStatus = status;

        return new ArrayList<>(Arrays.asList(
                id, customerId, dbDoctorId, dbDoctorFeedback, dbConsultationFee, dbStatus
        ));
    }

    public List<String> getPublicRecord() {
        return createDbRecord();
    }

    public static void createFromDbRecord(List<String> record) {
        String id = record.getFirst();
        String customerId = record.get(1);
        String doctorId;
        if (record.get(2).equalsIgnoreCase("NULL")) {
            doctorId = null;
        } else {
            doctorId = record.get(2);
        }
        String doctorFeedback;
        if (record.get(3).equalsIgnoreCase("NULL")) {
            doctorFeedback = null;
        } else {
            doctorFeedback = record.get(3);
        }
        double consultationFee = Double.parseDouble(record.get(4));
        String status = record.getLast();

        Appointment appointment = new Appointment(id, customerId, doctorId,
                doctorFeedback, consultationFee, status
        );
        Appointment.add(appointment);
    }

    public static String createId() {
        return Database.createId(appointments, 'A');
    }

    public static void add(Appointment newAppointment) {
        Database.add(newAppointment, appointments, appointmentFile);
    }

    public static Set<Appointment> getAll() {
        return new LinkedHashSet<>(appointments);
    }

    public static Appointment getById(String appointmentId) {
        return Database.getById(appointments, appointmentId, "appointment");
    }

    public static <R> Set<Appointment> getFiltered(
            Function<Appointment, R> fieldValReturner,
            R fieldVal) {
        return Database.getFiltered(appointments, fieldValReturner, fieldVal);
    }

    public static <R> List<R> getFieldVals(Set<Appointment> appointments, Function<Appointment, R> fieldValReturner) {
        return Database.getFieldVals(appointments, fieldValReturner);
    }

    public static List<List<String>> getPublicRecords(Set<Appointment> appointments) {
        return Database.getPublicRecords(appointments);
    }

    public static void removeById(String id, boolean removeDependencies) {
        Database.removeById(appointments, id, appointmentFile);

        if (removeDependencies) {
            for (AppointmentMedicine appointmentMedicine: AppointmentMedicine.getAll()) {
                if (appointmentMedicine.getAppointmentId().equals(id)) {
                    AppointmentMedicine.removeByIds(appointmentMedicine.getAppointmentId(), appointmentMedicine.getMedicineId());
                }
            }
            for (Invoice invoice: Invoice.getAll()) {
                if (invoice.getAppointmentId().equals(id)) {
                    Invoice.removeById(invoice.getId());
                }
            }
        }
    }

    public static String[] getColumnNames() {
        return new String[] {"Appointment ID", "Customer ID", "Doctor ID", "Doctor Feedback", "Consultation Fee", "Status"};
    }
}
