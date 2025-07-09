package user;

import database.Identifiable;

public class Customer extends User {

    public Customer(String id, String name, String email, String password) {
        super(id, name, email, password);
    }

    public Customer(String name, String email, String password) {
        this(Identifiable.createId('C'), name, email, password);
    }
}
