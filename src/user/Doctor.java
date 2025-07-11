package user;

import database.Identifiable;

import java.util.*;

public class Doctor extends User implements Employee {

    private Queue<String> appointmentIdRecord = new PriorityQueue<>(Comparator.comparingInt(
            appointmentId -> Integer.parseInt(appointmentId.substring(1))
    ));
    private Queue<String> customerFeedbackIdRecord = new PriorityQueue<>(Comparator.comparingInt(
            feedbackId -> Integer.parseInt(feedbackId.substring(1))
    ));

    public Doctor(String id, String name, String email, String password, Collection<String> appointmentIdRecord, Collection<String> customerFeedbackRecord) {
        super(id, name, email, password);
        Comparator<String> ascendingId = Comparator.comparingInt(
                appointmentId -> Integer.parseInt(appointmentId.substring(1))
        );
        this.appointmentIdRecord = new PriorityQueue<>(ascendingId);
        this.appointmentIdRecord.addAll(appointmentIdRecord);
        this.customerFeedbackIdRecord = new PriorityQueue<>(ascendingId);
        this.customerFeedbackIdRecord.addAll(customerFeedbackRecord);
    }

    public Doctor(String name, String email, String password) {
        super(Identifiable.createId('D'), name, email, password);
    }

    public void addAppointmentIdToRecord(String appointmentId) {
        this.appointmentIdRecord.add(appointmentId);
    }

    public void removeAppointmentIdFromRecord(String appointmentId) {
        this.appointmentIdRecord.remove(appointmentId);
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
            doctorAppointmentIdRecord = new PriorityQueue<>();
        } else {
            doctorAppointmentIdRecord = new PriorityQueue<>(Arrays.asList(record.get(4).split("&")));
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
