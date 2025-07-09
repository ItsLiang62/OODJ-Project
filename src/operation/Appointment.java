package operation;

import database.Identifiable;
import java.util.*;

public class Appointment {
    private String id;
    private String doctorId;
    private String customerId;
    private Set<String> medicineIds;
    private String doctorFeedback;
    private double charge;
    private String status;

    public Appointment(String id, String doctorId, String customerId, HashSet<String> medicineIds, String doctorFeedback, double charge, String status) {
        this.id = id;
        this.doctorId = doctorId;
        this.customerId = customerId;
        this.medicineIds = medicineIds;
        this.doctorFeedback = doctorFeedback;
        this.charge = charge;
        this.status = status;
    }

    // for customer use when requesting an appointment
    public Appointment(String customerId) {
        this(Identifiable.createId('A'), null, customerId, null, null, 0.0, "Pending");
    }

    public String getId() { return this.id; }
    public String getDoctorId() { return this.doctorId; }
    public String getCustomerId() { return this.customerId; }
    public Set<String> getMedicineIds() { return this.medicineIds; }
    public String getDoctorFeedback() { return this.doctorFeedback; }
    public double getCharge() { return this.charge; }
    public String getStatus() { return this.status; }




}
