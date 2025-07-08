package database;

import user.*;
import operation.*;

import java.util.Map;
import java.util.HashMap;
import java.util.Queue;
import java.util.PriorityQueue;

import java.util.Comparator;

public final class Database {
    private static Queue<User> users;
    private static Queue<Appointment> appointments;
    private static Queue<CustomerFeedback> customerFeedbacks = new PriorityQueue<>();
    private static Queue<Medicine> medicines = new PriorityQueue<>();
    private static Queue<Invoice> invoices = new PriorityQueue<>();

    static {

        users = new PriorityQueue<>(Comparator.comparingInt(
                user -> Integer.parseInt(user.getId().substring(1))
        ));

        Map<String, Integer> appointmentStatusPriority = new HashMap<>();
        appointmentStatusPriority.put("Pending", 1);
        appointmentStatusPriority.put("Confirmed", 2);
        appointmentStatusPriority.put("Completed", 3);
        appointments = new PriorityQueue<>(Comparator.comparingInt(
                appointment -> appointmentStatusPriority.get(appointment.getStatus())
        ));

        
    }
}
