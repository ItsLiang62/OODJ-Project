package user;

import database.*;

import java.util.*;

public class Customer extends User {

    private double apWallet;

    public Customer(String id, String name, String email, String password, double apWallet) {
        super(id, name, email, password);
        this.apWallet = apWallet;
        Database.addCustomer(this);
    }

    public Customer(String name, String email, String password, double apWallet) {
        this(Identifiable.createId('C'), name, email, password, apWallet);
    }

    public List<String> createRecord() {
        String dbId = this.id;
        String dbName = this.name;
        String dbEmail = this.email;
        String dbPassword = this.password;
        String dbApWallet = String.valueOf(this.apWallet);

        return new ArrayList<>(Arrays.asList(
                dbId, dbName, dbEmail, dbPassword, dbApWallet
        ));
    }

    public static void createCustomerFromRecord(List<String> record) {
        String customerId = record.getFirst();
        String customerName = record.get(1);
        String customerEmail = record.get(2);
        String customerPassword = record.get(3);
        double customerApWallet = Double.parseDouble(record.getLast());

        new Customer(customerId, customerName, customerEmail, customerPassword, customerApWallet);
    }
}
