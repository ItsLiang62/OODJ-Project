package user;

import database.Identifiable;

public class Manager extends User implements Employee {

    public Manager(String id, String name, String email, String password) {
        super(id, name, email, password);
    }

    public Manager(String name, String email, String password) {
        this(Identifiable.createId('M'), name, email, password);
    }

}
