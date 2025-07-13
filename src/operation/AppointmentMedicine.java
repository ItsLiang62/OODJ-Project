package operation;

import java.util.List;

import customExceptions.InvalidForeignKeyValueException;
import customExceptions.NullValueRejectedException;
import database.*;

public class AppointmentMedicine implements Savable {

    private String appointmentId;
    private String medicineId;
    private String targetSymptom;

    public AppointmentMedicine(String appointmentId, String medicineId, String targetSymptom) {
        checkAppointmentId(appointmentId);
        checkMedicineId(medicineId);
        checkTargetSymptom(targetSymptom);
        this.appointmentId = appointmentId;
        this.medicineId = medicineId;
        this.targetSymptom = targetSymptom;
    }

    public List<String> createRecord() {
        return null;
    }

    public AppointmentMedicine createAppointmentMedicineFromRecord(List<String> record) {
        return null;
    }

    public void setAppointmentId(String appointmentId) {
        checkAppointmentId(appointmentId);
        this.appointmentId = appointmentId;
    }

    public void setMedicineId(String medicineId) {
        checkMedicineId(medicineId);
        this.medicineId = medicineId;

    }

    private void checkAppointmentId(String appointmentId) {
        if (appointmentId == null) {
            throw new NullValueRejectedException("--- appointmentId field of AppointmentMedicine object must not be null ---");
        }
        if (!Database.getAllAppointmentId().contains(appointmentId)) {
            throw new InvalidForeignKeyValueException("--- appointmentId field of AppointmentMedicine object does not have a primary key reference ---");
        }
    }

    private void checkMedicineId(String medicineId) {
        if (medicineId == null) {
            throw new NullValueRejectedException("--- medicineId field of AppointmentMedicine object must not be null ---");
        }
        if (!Database.getAllMedicineId().contains(medicineId)) {
            throw new InvalidForeignKeyValueException("--- medicineId field of AppointmentMedicine object does not have a primary key reference ---");
        }
    }

    private void checkTargetSymptom(String targetSymptom) {
        if (targetSymptom == null) {
            throw new NullValueRejectedException("--- targetSymptom field of AppointmentMedicine object must not be null ---");
        }
    }
}
