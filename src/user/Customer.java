package user;

import database.*;

import java.util.*;

public class Customer extends User {

    private double apWallet;
    private Queue<String> appointmentIdRecord = new PriorityQueue<>(Comparator.comparingInt(
    appointmentId -> Integer.parseInt(appointmentId.substring(1))
    ));

    public Customer(String id, String name, String email, String password, double apWallet, Collection<String> appointmentIdRecord) {
        super(id, name, email, password);
        Comparator<String> ascendingAppointmentId = Comparator.comparingInt(
                appointmentId -> Integer.parseInt(appointmentId.substring(1))
        );
        this.apWallet = apWallet;
        this.appointmentIdRecord = new PriorityQueue<>(ascendingAppointmentId);
        this.appointmentIdRecord.addAll(appointmentIdRecord);
    }

    public Customer(String name, String email, String password, double apWallet) {
        super(Identifiable.createId('C'), name, email, password);
        this.apWallet = apWallet;
    }

    public void addAppointmentIdToRecord(String appointmentId) {
        this.appointmentIdRecord.add(appointmentId);
    }

    public void removeAppointmentIdFromRecord(String appointmentId) {
        this.appointmentIdRecord.remove(appointmentId);
    }

    public List<String> createRecord() {
        String dbId = this.id;
        String dbName = this.name;
        String dbEmail = this.email;
        String dbPassword = this.password;
        String dbApWallet = String.valueOf(this.apWallet);
        String dbAppointmentIdRecord;
        if (appointmentIdRecord.isEmpty()) {
            dbAppointmentIdRecord = "NULL";
        } else {
            dbAppointmentIdRecord = String.join("&", appointmentIdRecord);
        }
        return new ArrayList<>(Arrays.asList(
                dbId, dbName, dbEmail, dbPassword, dbApWallet, dbAppointmentIdRecord
        ));
    }

    public static Customer createCustomerFromRecord(List<String> record) {
        String customerId = record.getFirst();
        String customerName = record.get(1);
        String customerEmail = record.get(2);
        String customerPassword = record.get(3);
        double customerApWallet = Double.parseDouble(record.get(4));
        Collection<String> customerAppointmentIdRecord;
        if (record.getLast().equalsIgnoreCase("NULL")) {
            customerAppointmentIdRecord = new PriorityQueue<>();
        } else {
            customerAppointmentIdRecord = new PriorityQueue<>(Arrays.asList(record.getLast().split("&")));
        }

        return new Customer(customerId, customerName, customerEmail, customerPassword, customerApWallet, customerAppointmentIdRecord);
    }
}
