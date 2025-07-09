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
}
