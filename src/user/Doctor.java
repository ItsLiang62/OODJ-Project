package user;

import database.Identifiable;

import java.util.*;

public class Doctor extends User implements Employee {

    private Queue<String> appointmentIdRecord = new PriorityQueue<>(Comparator.comparingInt(
            appointmentId -> Integer.parseInt(appointmentId.substring(1))
    ));
    private Queue<String> customerFeedbackIdRecord = new PriorityQueue<>(Comparator.comparingInt(
            appointmentId -> Integer.parseInt(appointmentId.substring(1))
    ));

    public Doctor(String id, String name, String email, String password) {
        super(id, name, email, password);
    }

    public Doctor(String name, String email, String password) {
        this(Identifiable.createId('D'), name, email, password);
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
}
