package user;

import customExceptions.NullValueRejectedException;
import customExceptions.RecordAlreadyInDatabaseException;
import database.*;

import java.util.*;
import java.util.stream.Collectors;

public abstract class User implements Savable {
    protected String id;
    protected String name;
    protected String email;
    protected String password;

    public User(String id, String name, String email, String password) {
        checkName(name);
        checkEmail(email);
        checkPassword(password);
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static void checkName(String name) {
        if (name == null) {
            throw new NullValueRejectedException("--- name field of User object must not be null ---");
        }
    }

    public static void checkEmail(String email) {
        if (email == null) {
            throw new NullValueRejectedException("--- email field of User object must not be null ---");
        }
        if (Database.getAllUserEmails().contains(email)) {
            throw new RecordAlreadyInDatabaseException("--- email field of User object is being used by another User ---");
        }
    }

    public static void checkPassword(String password) {
        if (password == null) {
            throw new NullValueRejectedException("--- password field of User object must not be null ---");
        }
    }

    public String getId() { return this.id; }
    public String getName() { return this.name; }
    public String getEmail() { return this.email; }
    public String getPassword() { return this.password; }

    public void setName(String name) {
        checkName(name);
        this.name = name;
    }
    public void setEmail(String email) {
        checkEmail(email);
        this.email = email;
    }
    public void setPassword(String password) {
        checkPassword(password);
        this.password = password;
    }

    //public abstract List<String> getProfileInfo();


}
