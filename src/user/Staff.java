package user;

import database.Database;
import database.Identifiable;

import java.util.*;

public class Staff extends User {

    public Staff(String id, String name, String email, String password) {
        super(id, name, email, password);
        Database.addStaff(this);
    }

    public Staff(String name, String email, String password) {
        this(Identifiable.createId('S'), name, email, password);
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        Database.removeStaff(this.id);
        Database.addStaff(this);
    }

    @Override
    public void setEmail(String email) {
        super.setEmail(email);
        Database.removeStaff(this.id);
        Database.addStaff(this);
    }

    @Override
    public void setPassword(String password) {
        super.setPassword(password);
        Database.removeStaff(this.id);
        Database.addStaff(this);
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

    public static void createStaffFromRecord(List<String> record) {
        String staffId = record.getFirst();
        String staffName = record.get(1);
        String staffEmail = record.get(2);
        String staffPassword = record.getLast();

        new Staff(staffId, staffName, staffEmail, staffPassword);
    }
}
