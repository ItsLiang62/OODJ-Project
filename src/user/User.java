package user;

import customExceptions.InvalidEmailException;
import customExceptions.InvalidUserNameException;
import customExceptions.NullOrEmptyValueRejectedException;
import customExceptions.RecordAlreadyInDatabaseException;
import database.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Pattern namePattern = Pattern.compile("^([A-Z][A-Za-z]*(\\s[A-Z][A-Za-z]*)*)$");
        Matcher nameMatcher = namePattern.matcher(name);
        if (!nameMatcher.matches()) {
            throw new InvalidUserNameException("--- name field of User object is invalid ---");
        }
    }

    public static void checkEmail(String email) {
        Pattern emailPattern = Pattern.compile("^([\\w.+%-]+@[\\w.-]+\\.\\w{2,})$");
        Matcher emailMatcher = emailPattern.matcher(email);
        if (!emailMatcher.matches()) {
            throw new InvalidEmailException("--- email field of User object must follow the correct email format ---");
        }
        if (Database.getAllUserEmails().contains(email)) {
            throw new RecordAlreadyInDatabaseException("--- email field of User object is being used by another User ---");
        }
    }

    public static void checkPassword(String password) {
        if (password == null || password.isBlank()) {
            throw new NullOrEmptyValueRejectedException("--- password field of User object must not be null or empty ---");
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
