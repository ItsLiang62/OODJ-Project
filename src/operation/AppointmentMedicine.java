package operation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import customExceptions.AppointmentCompletedException;
import customExceptions.InvalidForeignKeyValueException;
import customExceptions.NullOrEmptyValueRejectedException;
import customExceptions.RepeatedPrescriptionForAppointmentException;
import database.*;

public class AppointmentMedicine implements Entity {

    private String appointmentId;
    private String medicineId;
    private String targetSymptom;

    public AppointmentMedicine(String appointmentId, String medicineId, String targetSymptom, boolean prescribingMedicine) {
        checkAppointmentId(appointmentId, prescribingMedicine);
        checkMedicineId(medicineId);
        checkTargetSymptom(targetSymptom);
        checkPrescriptionUniqueness(appointmentId, medicineId);
        this.appointmentId = appointmentId;
        this.medicineId = medicineId;
        this.targetSymptom = targetSymptom;
    }

    public AppointmentMedicine(String appointmentId, String medicineId, String targetSymptom) {
        this(appointmentId, medicineId, targetSymptom, false); // allow safe population of completed AppointmentMedicine record
    }

    public String getAppointmentId() { return this.appointmentId; }

    public String getMedicineId() { return this.medicineId; }

    public String getTargetSymptom() { return this.targetSymptom; }

    public void setTargetSymptom(String targetSymptom) {
        checkTargetSymptom(targetSymptom);
        this.targetSymptom = targetSymptom;
        Database.removeAppointmentMedicine(this.appointmentId, this.medicineId);
        Database.addAppointmentMedicine(this);
    }

    public static void checkAppointmentId(String appointmentId, boolean prescribingMedicine) {
        if (appointmentId == null || appointmentId.isBlank()) {
            throw new NullOrEmptyValueRejectedException("Appointment ID must not be null or empty!");
        }
        if (!Database.getAllAppointmentId().contains(appointmentId)) {
            throw new InvalidForeignKeyValueException("Appointment ID does not exist!");
        }
        if (prescribingMedicine) {
            if (Database.getAppointment(appointmentId).getStatus().equals("Completed")) {
                throw new AppointmentCompletedException("Appointment is completed and is not subject to any modification!");
            }
        }
    }

    public static void checkMedicineId(String medicineId) {
        if (medicineId == null || medicineId.isBlank()) {
            throw new NullOrEmptyValueRejectedException("Medicine ID must not be null or empty!");
        }
        if (!Database.getAllMedicineId().contains(medicineId)) {
            throw new InvalidForeignKeyValueException("Medicine ID does not exist!");
        }
    }

    public static void checkTargetSymptom(String targetSymptom) {
        if (targetSymptom == null || targetSymptom.isBlank()) {
            throw new NullOrEmptyValueRejectedException("Target symptom must not be null or empty!");
        }
    }

    public static void checkPrescriptionUniqueness(String appointmentId, String medicineId) {
        if (Database.getAllPrescriptionInfo().contains(Arrays.asList(appointmentId, medicineId))) {
            throw new RepeatedPrescriptionForAppointmentException("The prescription already exists!");
        }
    }

    public List<String> createDbRecord() {
        String dbAppointmentId = this.appointmentId;
        String dbMedicineId = this.medicineId;
        String dbTargetSymptom = this.targetSymptom;

        return new ArrayList<>(Arrays.asList(
                dbAppointmentId, dbMedicineId, dbTargetSymptom
        ));
    }

    public List<String> createPublicRecord() {
        return this.createDbRecord();
    }

    public static void createAppointmentMedicineFromRecord(List<String> record) {
        String appointmentId = record.getFirst();
        String medicineId = record.get(1);
        String targetSymptom = record.getLast();

        AppointmentMedicine appointmentMedicine = new AppointmentMedicine(appointmentId, medicineId, targetSymptom);
        Database.addAppointmentMedicine(appointmentMedicine);
    }
}
