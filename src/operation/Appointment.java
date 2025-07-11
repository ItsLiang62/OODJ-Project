package operation;

import database.Database;
import database.Identifiable;
import database.Savable;

import java.util.*;

public class Appointment implements Savable {
    private String id;
    private String customerId;
    private String doctorId;
    private Set<String> medicineIds = new HashSet<>();
    private String doctorFeedback;
    private double charge;
    private String status = "Pending";

    public Appointment(String id, String customerId, String doctorId, Collection<String> medicineIds, String doctorFeedback, double charge, String status) {
        this.id = id;
        this.customerId = customerId;
        this.doctorId = doctorId;
        this.medicineIds = new HashSet<>(medicineIds);
        this.doctorFeedback = doctorFeedback;
        this.charge = charge;
        this.status = status;
    }

    public Appointment(String id, String customerId) {
        this.id = id;
        this.customerId = customerId;
    }

    // for customer use when requesting an appointment
    public Appointment(String customerId) {
        this(Identifiable.createId('A'), customerId);
    }

    public String getId() { return this.id; }
    public String getCustomerId() { return this.customerId; }
    public String getDoctorId() { return this.doctorId; }
    public Set<String> getMedicineIds() { return new HashSet<>(this.medicineIds); }
    public String getDoctorFeedback() { return this.doctorFeedback; }
    public double getCharge() { return this.charge; }
    public String getStatus() { return this.status; }

    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
    public void setDoctorFeedback(String doctorFeedback) { this.doctorFeedback = doctorFeedback; }
    public void setCharge(double charge) { this.charge = charge; }
    public void setStatus(String status) { this.status = status; }

    public void addMedicineId(String medicineId) {
        this.medicineIds.add(medicineId);
    }
    public void removeMedicineId(String medicineId) {
        this.medicineIds.remove(medicineId);
    }

    public List<String> createRecord() {
        String dbId = this.id;
        String dbCustomerId = this.customerId;
        String dbDoctorId = this.doctorId;
        String dbMedicineIds;
        if (this.medicineIds.isEmpty()) {
            dbMedicineIds = "NULL";
        } else {
            dbMedicineIds = String.join("&", medicineIds);
        }
        String dbDoctorFeedback = Objects.requireNonNullElse(doctorFeedback, "NULL");
        String dbCharge = String.valueOf(this.charge);
        String dbStatus = this.status;

        return new ArrayList<>(Arrays.asList(
                dbId, dbCustomerId, dbDoctorId, dbMedicineIds, dbDoctorFeedback, dbCharge, dbStatus
        ));
    }

    public static Appointment createAppointmentFromRecord(List<String> record) {
        String appointmentId = record.getFirst();
        String appointmentCustomerId = record.get(1);
        String appointmentDoctorId;
        if (record.get(2).equalsIgnoreCase("NULL")) {
            appointmentDoctorId = null;
        } else {
            appointmentDoctorId = record.get(2);
        }
        Collection<String> appointmentMedicineIds;
        if (record.get(3).equalsIgnoreCase("NULL")) {
            appointmentMedicineIds = new HashSet<>();
        } else {
            appointmentMedicineIds = new HashSet<>(Arrays.asList(record.get(3).split("&")));
        }
        String appointmentDoctorFeedback;
        if (record.get(4).equalsIgnoreCase("NULL")) {
            appointmentDoctorFeedback = null;
        } else {
            appointmentDoctorFeedback = record.get(4);
        }
        double appointmentCharge = Double.parseDouble(record.get(5));
        String appointmentStatus = record.getLast();

        return new Appointment(appointmentId, appointmentCustomerId, appointmentDoctorId,
                appointmentMedicineIds, appointmentDoctorFeedback, appointmentCharge, appointmentStatus
        );
    }
}
