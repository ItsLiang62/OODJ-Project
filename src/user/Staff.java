package user;

import database.Database;
import database.Identifiable;

import java.util.*;
import java.util.stream.Collectors;

public class Staff extends User implements Employee {

    public Staff(String id, String name, String email, String password) {
        super(id, name, email, password);
    }

    public Staff(String name, String email, String password) {
        this(Identifiable.createId('S'), name, email, password);
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

    public Staff createInstanceFromRecord(List<String> record) {
        String staffId = record.getFirst();
        String staffName = record.get(1);
        String staffEmail = record.get(2);
        String staffPassword = record.getLast();

        return new Staff(staffId, staffName, staffEmail, staffPassword);
    }
}
