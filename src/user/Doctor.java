package user;

import customExceptions.AppointmentNotAssignedToDoctorException;
import database.Database;
import database.Identifiable;

import java.util.*;

public class Doctor extends User implements Employee {

    private Comparator<String> ascendingId = Comparator.comparingInt(
            id -> Integer.parseInt(id.substring(1))
    );

    private Set<String> appointmentIdRecord = new TreeSet<>(ascendingId);;
    private Set<String> customerFeedbackIdRecord = new TreeSet<>(ascendingId);

    public Doctor(String id, String name, String email, String password, Collection<String> appointmentIdRecord, Collection<String> customerFeedbackRecord) {
        super(id, name, email, password);
        Comparator<String> ascendingId = Comparator.comparingInt(
                appointmentId -> Integer.parseInt(appointmentId.substring(1))
        );
        this.appointmentIdRecord = new TreeSet<>(ascendingId);
        this.appointmentIdRecord.addAll(appointmentIdRecord);
        this.customerFeedbackIdRecord = new TreeSet<>(ascendingId);
        this.customerFeedbackIdRecord.addAll(customerFeedbackRecord);
    }

    public Doctor(String name, String email, String password) {
        super(Identifiable.createId('D'), name, email, password);
    }

    public void addAppointmentIdToRecord(String appointmentId) {
        if (Database.getAppointment(appointmentId).getDoctorId().equals(this.id)) {
            this.appointmentIdRecord.add(appointmentId);
        } else {
            throw new AppointmentNotAssignedToDoctorException(
                    String.format("--- Doctor.addAppointmentIdToRecord(String) failed. Appointment %s is never assigned to doctor %s. Have you assigned the doctor ID to the appointment using Appointment.setDoctorId(String) already?  ---", appointmentId, this.id));
        }
    }

    public void removeAppointmentIdFromRecord(String appointmentId) {

        if (this.appointmentIdRecord.contains(appointmentId)) {
            if (Database.getAppointment(appointmentId).getDoctorId().equals(appointmentId)) {
                this.appointmentIdRecord.remove(appointmentId);
            } else {

            }
        } else {
            throw new AppointmentNotAssignedToDoctorException(
                    String.format("--- Doctor.removeAppointmentIdToRecord(String) failed. Appointment %s is never assigned to doctor %s ---", appointmentId, this.id));
        }
    }

    public void addCustomerFeedbackIdToRecord(String customerFeedbackId) {
        this.customerFeedbackIdRecord.add(customerFeedbackId);
    }

    public void removeCustomerFeedbackFromRecord(String customerFeedbackId) {
        this.customerFeedbackIdRecord.remove(customerFeedbackId);
    }

    public List<String> createRecord() {
        String dbId = this.id;
        String dbName = this.name;
        String dbEmail = this.email;
        String dbPassword = this.password;
        String dbAppointmentIdRecord;
        if (this.appointmentIdRecord.isEmpty()) {
            dbAppointmentIdRecord = "NULL";
        } else {
            dbAppointmentIdRecord = String.join("&", appointmentIdRecord);
        }
        String dbCustomerFeedbackIdRecord;
        if (this.customerFeedbackIdRecord.isEmpty()) {
            dbCustomerFeedbackIdRecord = "NULL";
        } else {
            dbCustomerFeedbackIdRecord = String.join("&", appointmentIdRecord);
        }

        return new ArrayList<>(Arrays.asList(
                dbId, dbName, dbEmail, dbPassword, dbAppointmentIdRecord, dbCustomerFeedbackIdRecord
        ));
    }

    public static Doctor createDoctorFromRecord(List<String> record) {
        String doctorId = record.getFirst();
        String doctorName = record.get(1);
        String doctorEmail = record.get(2);
        String doctorPassword = record.get(3);
        Collection<String> doctorAppointmentIdRecord;
        if (record.get(4).equalsIgnoreCase("NULL")) {
            doctorAppointmentIdRecord = new TreeSet<>();
        } else {
            doctorAppointmentIdRecord = new TreeSet<>(Arrays.asList(record.get(4).split("&")));
        }
        Collection<String> doctorCustomerFeedbackIdRecord;
        if (record.getLast().equalsIgnoreCase("NULL")) {
            doctorCustomerFeedbackIdRecord = new PriorityQueue<>();
        } else {
            doctorCustomerFeedbackIdRecord = new PriorityQueue<>(Arrays.asList(record.getLast().split("&")));
        }

        return new Doctor(doctorId, doctorName, doctorEmail, doctorPassword, doctorAppointmentIdRecord, doctorCustomerFeedbackIdRecord);
    }
}
