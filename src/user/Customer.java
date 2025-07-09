package user;

public class Customer extends User {

    public Customer(String id, String name, String email, String password) {
        super(name, email, password);
        this.id = id;
    }

    public Customer(String name, String email, String password) {
        this(createId('C'), name, email, password);
    }
}
