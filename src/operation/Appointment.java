package operation;

import customExceptions.*;
import database.Database;
import database.IdCreator;
import database.Identifiable;

import java.util.*;

public class Appointment implements Identifiable {
    private String id;
    private String customerId;
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

    public String getId() { return this.id; }
    public String getCustomerId() { return this.customerId; }
    public String getDoctorId() { return this.doctorId; }
    public String getDoctorFeedback() { return this.doctorFeedback; }
    public double getConsultationFee() { return this.consultationFee; }
    public String getStatus() { return this.status; }

    public double getTotalMedicineCharges() {
        return Database.getTotalMedicineChargesOfAppointment(this.id);
    }

    public double getTotalCharge() {
        return getConsultationFee() + getTotalMedicineCharges();
    }

    public void setDoctorId(String doctorId) {
        if (this.status.equals("Completed")) {
            throw new AppointmentCompletedException("Appointment is completed and is not subject to any modification.");
        }
        checkDoctorId(doctorId);
        this.doctorId = doctorId;
        if (this.doctorId == null) { // Unassign a doctor
            this.status = "Pending";
        } else {
            this.status = "Confirmed";
        }
        Database.removeAppointment(this.id, false);
        Database.addAppointment(this);
    }

    public void setDoctorFeedback(String doctorFeedback) {
        if (this.status.equals("Completed")) {
            throw new AppointmentCompletedException("Appointment is completed and is not subject to any modification.");
        }
        this.doctorFeedback = doctorFeedback;
        Database.removeAppointment(this.id, false);
        Database.addAppointment(this);
    }

    public void setConsultationFee(double consultationFee) {
        if (this.status.equals("Completed")) {
            throw new AppointmentCompletedException("Appointment is completed and is not subject to any modification.");
        }
        checkConsultationFee(consultationFee);
        this.consultationFee = consultationFee;
        Database.removeAppointment(this.id, false);
        Database.addAppointment(this);
    }

    public void setStatusToCompleted() {
        if (this.status.equals("Completed")) {
            throw new AppointmentCompletedException("Appointment is already completed.");
        }
        if (this.doctorId == null || !this.status.equals("Confirmed")) {
            throw new AppointmentNotCompletableException("Appointment is not completable. Make sure the appointment is confirmed and has a valid doctor ID.");
        } else {
            this.status = "Completed";
        }
        Database.removeAppointment(this.id, false);
        Database.addAppointment(this);
    }

    public List<String> createDbRecord() {
        String dbId = this.id;
        String dbCustomerId = this.customerId;
        String dbDoctorId = Objects.requireNonNullElse(doctorId, "NULL");
        String dbDoctorFeedback = Objects.requireNonNullElse(doctorFeedback, "NULL");
        String dbConsultationFee = String.valueOf(this.consultationFee);
        String dbStatus = this.status;

        return new ArrayList<>(Arrays.asList(
                dbId, dbCustomerId, dbDoctorId, dbDoctorFeedback, dbConsultationFee, dbStatus
        ));
    }

    public List<String> createPublicRecord() {
        return this.createDbRecord();
    }

    public static void createAppointmentFromRecord(List<String> record) {
        String appointmentId = record.getFirst();
        String appointmentCustomerId = record.get(1);
        String appointmentDoctorId;
        if (record.get(2).equalsIgnoreCase("NULL")) {
            appointmentDoctorId = null;
        } else {
            appointmentDoctorId = record.get(2);
        }
        String appointmentDoctorFeedback;
        if (record.get(3).equalsIgnoreCase("NULL")) {
            appointmentDoctorFeedback = null;
        } else {
            appointmentDoctorFeedback = record.get(3);
        }
        double appointmentConsultationFee = Double.parseDouble(record.get(4));
        String appointmentStatus = record.getLast();

        Appointment appointment = new Appointment(appointmentId, appointmentCustomerId, appointmentDoctorId,
                appointmentDoctorFeedback, appointmentConsultationFee, appointmentStatus
        );
        Database.addAppointment(appointment);
    }
}
