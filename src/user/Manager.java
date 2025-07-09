package user;

public class Manager extends User implements Employee {

    public Manager(String id, String name, String email, String password) {
        super(name, email, password);
        this.id = id;
    }

    public Manager(String name, String email, String password) {
        this(createId('M'), name, email, password);
    }

}
