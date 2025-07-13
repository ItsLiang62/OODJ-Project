package operation;

import customExceptions.InvalidAppointmentStatusException;
import customExceptions.InvalidForeignKeyValueException;
import customExceptions.NullValueRejectedException;
import database.Database;
import database.Identifiable;
import database.Savable;

import java.util.*;

public class Appointment implements Savable {
    private String id;
    private String customerId;
    private String doctorId;
    private String doctorFeedback;
    private double charge;
    private String status;

    public Appointment(String id, String customerId, String doctorId, String doctorFeedback, double charge, String status) {
        checkCustomerId(customerId);
        checkDoctorId(doctorId);
        checkStatus(status);
        this.id = id;
        this.customerId = customerId;
        this.doctorId = doctorId;
        this.doctorFeedback = doctorFeedback;
        this.charge = charge;
        this.status = status;
    }

    public Appointment(String id, String customerId) {
        this(id, customerId, null, null, 0.0, "Pending");
    }

    private void checkCustomerId(String customerId) {
        if (customerId == null) {
            throw new NullValueRejectedException("--- customerId field of Appointment object must not be null ---");
        }
        if (!Database.getAllCustomerId().contains(customerId)) {
            throw new InvalidForeignKeyValueException("--- customerId field of Appointment object does not have a primary key reference ---");
        }
    }

    private void checkDoctorId(String doctorId) {
        if (doctorId != null && !Database.getAllDoctorId().contains(doctorId)) {
            throw new InvalidForeignKeyValueException("--- doctorId field of Appointment object does not have a primary key reference ---");
        }
    }

    private void checkStatus(String status) {
        if (!Arrays.asList(new String[] {"Pending", "Confirmed", "Completed"}).contains(status)) {
            throw new InvalidAppointmentStatusException("--- status of Appointment object must be either Pending, Confirmed or Completed ---");
        }
    }

    // for customer use when requesting an appointment
    public Appointment(String customerId) {
        this(Identifiable.createId('A'), customerId);
    }

    public String getId() { return this.id; }
    public String getCustomerId() { return this.customerId; }
    public String getDoctorId() { return this.doctorId; }
    public String getDoctorFeedback() { return this.doctorFeedback; }
    public double getCharge() { return this.charge; }
    public String getStatus() { return this.status; }

    public void setCustomerId(String customerId) {
        checkCustomerId(customerId);
        this.customerId = customerId;
    }
    public void setDoctorId(String doctorId) {
        checkDoctorId(doctorId);
        this.doctorId = doctorId;
    }
    public void setDoctorFeedback(String doctorFeedback) { this.doctorFeedback = doctorFeedback; }
    public void setCharge(double charge) { this.charge = charge; }
    public void setStatus(String status) {
        checkStatus(status);
        this.status = status;
    }


    public List<String> createRecord() {
        String dbId = this.id;
        String dbCustomerId = this.customerId;
        String dbDoctorId = Objects.requireNonNullElse(doctorId, "NULL");
        String dbDoctorFeedback = Objects.requireNonNullElse(doctorFeedback, "NULL");
        String dbCharge = String.valueOf(this.charge);
        String dbStatus = this.status;

        return new ArrayList<>(Arrays.asList(
                dbId, dbCustomerId, dbDoctorId, dbDoctorFeedback, dbCharge, dbStatus
        ));
    }

    public static Appointment createAppointmentFromRecord(List<String> record) {
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
        double appointmentCharge = Double.parseDouble(record.get(4));
        String appointmentStatus = record.getLast();

        return new Appointment(appointmentId, appointmentCustomerId, appointmentDoctorId,
                appointmentDoctorFeedback, appointmentCharge, appointmentStatus
        );
    }
}
