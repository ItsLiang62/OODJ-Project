package operation;

import java.util.List;
import database.*;

public class AppointmentMedicine implements Savable {

    private String appointmentId;
    private String medicineId;
    private String targetSymptom;

    public AppointmentMedicine(String appointmentId, String medicineId, String targetSymptom) {
        this.appointmentId = appointmentId;
        this.medicineId = medicineId;
        this.targetSymptom = targetSymptom;
    }

    private 
    public List<String> createRecord() {
        return null;
    }

    public AppointmentMedicine createAppointmentMedicineFromRecord(List<String> record) {
        return null;
    }
}
