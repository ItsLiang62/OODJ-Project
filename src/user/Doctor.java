package user;

import database.Database;
import database.Identifiable;

import java.util.*;

public class Doctor extends User {

    public Doctor(String id, String name, String email, String password) {
        super(id, name, email, password);
        Database.addDoctor(this);
    }

    public Doctor(String name, String email, String password) {
        super(Identifiable.createId('D'), name, email, password);
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

    public static void createDoctorFromRecord(List<String> record) {
        String doctorId = record.getFirst();
        String doctorName = record.get(1);
        String doctorEmail = record.get(2);
        String doctorPassword = record.getLast();

        new Doctor(doctorId, doctorName, doctorEmail, doctorPassword);
    }
}
