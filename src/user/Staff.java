package user;

import database.Database;

import java.util.*;
import java.util.stream.Collectors;

public class Staff extends User implements Employee {

    public Staff(String id, String name, String email, String password) {
        super(name, email, password);
        this.id = id;
    }

    public Staff(String name, String email, String password) {
        this(createId('S'), name, email, password);
    }
}
