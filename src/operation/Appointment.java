package operation;

import main.MedicalCentre;
import java.util.*;

public class Appointment {
    private String id;
    private String doctorId;
    private String customerId;
    private Set<Medicine> prescription = new HashSet<>();
    private String doctorFeedback;
    private double charge;
    private String status = "Pending";


    public String getId() { return this.id; }
    public String getDoctorId() { return this.doctorId; }
    public String getCustomerId() { return this.customerId; }
    public Set<Medicine> getPrescription() { return this.prescription; }
    public String getDoctorFeedback() { return this.doctorFeedback; }
    public double getCharge() { return this.charge; }
    public String getStatus() { return this.status; }
}
