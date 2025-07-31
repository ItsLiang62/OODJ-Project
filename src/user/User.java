package user;

import customExceptions.InvalidEmailException;
import customExceptions.InvalidUserNameException;
import customExceptions.NullOrEmptyValueRejectedException;
import customExceptions.RecordAlreadyInDatabaseException;
import database.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class User implements Identifiable {
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

    public void checkName(String name) {
        if (name == null) {
            throw new InvalidUserNameException("Invalid user name!");
        }
        if (name.equals(this.name)) {
            return;
        }
        Pattern namePattern = Pattern.compile("^([A-Z][A-Za-z]*(\\s[A-Z][A-Za-z]*)*)$");
        Matcher nameMatcher = namePattern.matcher(name);
        if (!nameMatcher.matches()) {
            throw new InvalidUserNameException("Invalid user name!");
        }
    }
    public void checkEmail(String email) {
        if (email == null) {
            throw new InvalidEmailException("Invalid email format!");
        }
        if (email.equals(this.email)) {
            return;
        }
        Pattern emailPattern = Pattern.compile("^([\\w.+%-]+@[\\w.-]+\\.\\w{2,})$");
        Matcher emailMatcher = emailPattern.matcher(email);
        if (!emailMatcher.matches()) {
            throw new InvalidEmailException("Invalid email format!");
        }
        if (Database.getAllUserEmails().contains(email)) {
            throw new RecordAlreadyInDatabaseException("Email is already in use by another user!");
        }
    }
    public static void checkPassword(String password) {
        if (password == null || password.isBlank()) {
            throw new NullOrEmptyValueRejectedException("Password must not be empty!");
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

    public List<String> createDbRecord() {
        String dbId = this.id;
        String dbName = this.name;
        String dbEmail = this.email;
        String dbPassword = this.password;

        return new ArrayList<>(Arrays.asList(
                dbId, dbName, dbEmail, dbPassword
        ));
    }

    public List<String> createPublicRecord() {
        String id = this.id;
        String name = this.name;
        String email = this.email;

        return new ArrayList<>(Arrays.asList(
                id, name, email
        ));
    }

    public static String[] getColumnNames() {
        return new String[] {"ID", "Name", "Email"};
    }
}
