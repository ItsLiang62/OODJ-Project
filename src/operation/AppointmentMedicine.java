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
    }

    public AppointmentMedicine(String appointmentId, String medicineId, String targetSymptom) {
        this(appointmentId, medicineId, targetSymptom, false);
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
            throw new NullOrEmptyValueRejectedException("--- appointmentId field of AppointmentMedicine object must not be null or empty ---");
        }
        if (!Database.getAllAppointmentId().contains(appointmentId)) {
            throw new InvalidForeignKeyValueException("--- appointmentId field of AppointmentMedicine object does not have a primary key reference ---");
        }
        if (prescribingMedicine) {
            if (Database.getAppointment(appointmentId).getStatus().equals("Completed")) {
                throw new AppointmentCompletedException("--- Appointment was completed and is not subject to any modification ---");
            }
        }
    }

    public static void checkMedicineId(String medicineId) {
        if (medicineId == null || medicineId.isBlank()) {
            throw new NullOrEmptyValueRejectedException("--- medicineId field of AppointmentMedicine object must not be null or empty ---");
        }
        if (!Database.getAllMedicineId().contains(medicineId)) {
            throw new InvalidForeignKeyValueException("--- medicineId field of AppointmentMedicine object does not have a primary key reference ---");
        }
    }

    public static void checkTargetSymptom(String targetSymptom) {
        if (targetSymptom == null || targetSymptom.isBlank()) {
            throw new NullOrEmptyValueRejectedException("--- targetSymptom field of AppointmentMedicine object must not be null or empty ---");
        }
    }

    public static void checkPrescriptionUniqueness(String appointmentId, String medicineId) {
        if (Database.getAllPrescriptionInfo().contains(Arrays.asList(appointmentId, medicineId))) {
            throw new RepeatedPrescriptionForAppointmentException("--- The combination of appointmentId and medicineId for an AppointmentMedicine object already exists ---");
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
