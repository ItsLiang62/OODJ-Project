package operation;

import customExceptions.*;
import database.Database;
import database.IdCreator;
import database.Identifiable;

import java.util.*;

public class Appointment implements Identifiable {
    private final String id;
    private final String customerId;
    private String doctorId;
    private String doctorFeedback;
    private double consultationFee;
    private String status;

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

        Database.addAppointment(this);
    }


    public Appointment(String customerId) {
        this(IdCreator.createId('A'), customerId, null, null, 0.0, "Pending");
    }

    public static void checkCustomerId(String customerId) {
        if (customerId == null || customerId.isBlank()) {
            throw new NullOrEmptyValueRejectedException("Customer ID of appointment object must not be null or empty!");
        }
        if (!Database.getAllCustomerId().contains(customerId)) {
            throw new InvalidForeignKeyValueException("Customer ID does not exist!");
        }
    }

    public static void checkDoctorId(String doctorId) {
        if (doctorId != null && !Database.getAllDoctorId().contains(doctorId)) {
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

    public String getId() { return id; }
    public String getCustomerId() { return customerId; }
    public String getDoctorId() { return doctorId; }
    public String getDoctorFeedback() { return doctorFeedback; }
    public double getConsultationFee() { return consultationFee; }
    public String getStatus() { return status; }

    public double getTotalMedicineCharges() {
        return Database.getTotalMedicineChargesOfAppointment(id);
    }

    public double getTotalCharge() {
        return getConsultationFee() + getTotalMedicineCharges();
    }

    public void setDoctorId(String doctorId) {
        if (status.equals("Completed")) {
            throw new AppointmentCompletedException("Appointment is completed and is not subject to any modification.");
        }
        checkDoctorId(doctorId);
        this.doctorId = doctorId;
        if (doctorId == null) { // Unassign a doctor
            status = "Pending";
        } else {
            status = "Confirmed";
        }
        Database.removeAppointment(id, false);
        Database.addAppointment(this);
    }

    public void setDoctorFeedback(String doctorFeedback) {
        if (status.equals("Completed")) {
            throw new AppointmentCompletedException("Appointment is completed and is not subject to any modification.");
        }
        this.doctorFeedback = doctorFeedback;
        Database.removeAppointment(id, false);
        Database.addAppointment(this);
    }

    public void setConsultationFee(double consultationFee) {
        if (status.equals("Completed")) {
            throw new AppointmentCompletedException("Appointment is completed and is not subject to any modification.");
        }
        checkConsultationFee(consultationFee);
        this.consultationFee = consultationFee;
        Database.removeAppointment(id, false);
        Database.addAppointment(this);
    }

    public void setStatusToCompleted() {
        if (status.equals("Completed")) {
            throw new AppointmentCompletedException("Appointment is already completed.");
        }
        if (doctorId == null || !status.equals("Confirmed")) {
            throw new AppointmentNotCompletableException("Appointment is not completable. Make sure the appointment is confirmed and has a valid doctor ID.");
        } else {
            status = "Completed";
        }
        Database.removeAppointment(this.id, false);
        Database.addAppointment(this);
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

    public List<String> createPublicRecord() {
        return createDbRecord();
    }

    public static String[] getColumnNames() { return new String[] {"Appointment ID", "Customer ID", "Doctor ID", "Doctor Feedback", "Consultation Fee", "Status"}; }

    public static void createAppointmentFromRecord(List<String> record) {
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
        Database.addAppointment(appointment);
    }
}
