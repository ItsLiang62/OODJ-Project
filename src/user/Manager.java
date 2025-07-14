package user;

import database.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Manager extends User {

    public Manager(String id, String name, String email, String password) {
        super(id, name, email, password);
        Database.addManager(this);
    }

    public Manager(String name, String email, String password) {
        this(Identifiable.createId('M'), name, email, password);
    }

    public List<String> createRecord() {
        String dbId = this.id;
        String dbName = this.name;
        String dbEmail = this.email;
        String dbPassword = this.password;
        return new ArrayList<>(Arrays.asList(
                dbId, dbName, dbEmail, dbPassword
        ));
    }

    public static void createManagerFromRecord(List<String> record) {
        String managerId = record.getFirst();
        String managerName = record.get(1);
        String managerEmail = record.get(2);
        String managerPassword = record.getLast();

        new Manager(managerId, managerName, managerEmail, managerPassword);
    }
}
