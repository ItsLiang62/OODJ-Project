package user;

import java.util.*;

public class Doctor extends User implements Employee {

    private Queue<String> appointmentIdHistory = new PriorityQueue<>(Comparator.comparingInt(
            appointmentId -> Integer.parseInt(appointmentId.substring(1))
    ));
    private Queue<String> customerFeedbackIdHistory = new PriorityQueue<>(Comparator.comparingInt(
            appointmentId -> Integer.parseInt(appointmentId.substring(1))
    ));

    public Doctor(String id, String name, String email, String password) {
        super(name, email, password);
        this.id = id;
    }

    public Doctor(String name, String email, String password) {
        this(createId('D'), name, email, password);
    }

}
