package user;

import database.Database;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;

public abstract class User {
    protected String id;
    protected String name;
    protected String email;
    protected String password;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getId() { return this.id; }
    public String getName() { return this.name; }
    public String getEmail() { return this.email; }
    public String getPassword() { return this.password; }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }

    protected static String createId(char userType) {
        Queue<String> allUserTypeId = new PriorityQueue<>(Comparator.comparingInt(
                (String userTypeId) -> Integer.parseInt(userTypeId.substring(1))
        ).reversed());

        allUserTypeId.addAll(Database.getAllUserId().stream().filter(
                userId -> userId.charAt(0) == userType
        ).collect(Collectors.toSet()));

        String newestUserTypeId = allUserTypeId.peek();
        assert newestUserTypeId != null;
        return String.format("%s%03d", userType, Integer.parseInt(newestUserTypeId.substring(1)) + 1);
    }
}
