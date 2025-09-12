package operation;

import java.io.File;
import java.util.*;
import java.util.function.Function;

import customExceptions.*;
import database.*;
import user.Customer;

public class AppointmentMedicine implements Entity {

    private final String appointmentId;
    private final String medicineId;
    private String targetSymptom;

    private static final Set<AppointmentMedicine> appointmentMedicines = new TreeSet<>(Comparator.comparingInt(
            (AppointmentMedicine appointmentMedicine) -> Integer.parseInt(appointmentMedicine.getAppointmentId().substring(1))
    ).thenComparing(
            (AppointmentMedicine appointmentMedicine) -> Integer.parseInt(appointmentMedicine.getMedicineId().substring(1))
    ));;
    private static final File appointmentMedicineFile = new File("data/appointmentMedicine.txt");

    static {
        Database.populateFromRecords(AppointmentMedicine::createFromDbRecord, appointmentMedicineFile);
    }

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

    public String getAppointmentId() {
        return this.appointmentId;
    }

    public String getMedicineId() {
        return this.medicineId;
    }

    public String getTargetSymptom() {
        return this.targetSymptom;
    }

    public void setTargetSymptom(String targetSymptom) {
        checkTargetSymptom(targetSymptom);
        this.targetSymptom = targetSymptom;
        AppointmentMedicine.removeByIds(this.getAppointmentId(), this.getMedicineId());
        AppointmentMedicine.add(this);
    }

    public static void checkAppointmentId(String appointmentId, boolean prescribingMedicine) {
        if (appointmentId == null || appointmentId.isBlank()) {
            throw new NullOrEmptyValueRejectedException("Appointment ID must not be null or empty!");
        }
        if (!Appointment.getFieldVals(Appointment.getAll(), Appointment::getId).contains(appointmentId)) {
            throw new InvalidForeignKeyValueException("Appointment ID does not exist!");
        }
        if (prescribingMedicine) {
            if (Appointment.getById(appointmentId).getStatus().equals("Completed")) {
                throw new AppointmentCompletedException("Appointment is completed and is not subject to any modification!");
            }
        }
    }

    public static void checkMedicineId(String medicineId) {
        if (medicineId == null || medicineId.isBlank()) {
            throw new NullOrEmptyValueRejectedException("Medicine ID must not be null or empty!");
        }
        if (!Medicine.getFieldVals(Medicine.getAll(), Medicine::getId).contains(medicineId)) {
            throw new InvalidForeignKeyValueException("Medicine ID does not exist!");
        }
    }

    public static void checkTargetSymptom(String targetSymptom) {
        if (targetSymptom == null || targetSymptom.isBlank()) {
            throw new NullOrEmptyValueRejectedException("Target symptom must not be null or empty!");
        }
    }

    public static void checkPrescriptionUniqueness(String appointmentId, String medicineId) {
        if (AppointmentMedicine.getFieldVals(
                AppointmentMedicine.getFiltered(AppointmentMedicine::getAppointmentId, appointmentId),
                AppointmentMedicine::getMedicineId
        ).contains(medicineId)) {
            throw new RepeatedPrescriptionForAppointmentException("The prescription already exists!");
        }
    }

    public List<String> createDbRecord() {
        String dbTargetSymptom = this.targetSymptom;

        return new ArrayList<>(Arrays.asList(
                this.appointmentId, this.medicineId, dbTargetSymptom
        ));
    }

    public List<String> getPublicRecord() {
        return this.createDbRecord();
    }

    public static void createFromDbRecord(List<String> record) {
        String appointmentId = record.getFirst();
        String medicineId = record.get(1);
        String targetSymptom = record.getLast();

        AppointmentMedicine appointmentMedicine = new AppointmentMedicine(appointmentId, medicineId, targetSymptom);
        AppointmentMedicine.add(appointmentMedicine);
    }

    public static void add(AppointmentMedicine newAppointmentMedicine) {
        Database.add(newAppointmentMedicine, appointmentMedicines, appointmentMedicineFile);
    }

    public static Set<AppointmentMedicine> getAll() {
        return new LinkedHashSet<>(appointmentMedicines);
    }

    public static AppointmentMedicine getByIds(String appointmentId, String medicineId) {
        for (AppointmentMedicine appointmentMedicine: appointmentMedicines) {
            // Both appointment ID and medicine ID must match
            if (appointmentMedicine.getAppointmentId().equals(appointmentId) && appointmentMedicine.getMedicineId().equals(medicineId)) {
                return appointmentMedicine;
            }
        }
        throw new IdNotFoundException("Could not find prescription with the given appointment ID and medicine ID!");
    }

    public static <R> Set<AppointmentMedicine> getFiltered(
            Function<AppointmentMedicine, R> fieldValReturner,
            R fieldVal) {
        return Database.getFiltered(appointmentMedicines, fieldValReturner, fieldVal);
    }

    public static <R> List<R> getFieldVals(Set<AppointmentMedicine> appointmentMedicines, Function<AppointmentMedicine, R> fieldValReturner) {
        return Database.getFieldVals(appointmentMedicines, fieldValReturner);
    }

    public static List<List<String>> getPublicRecords(Set<AppointmentMedicine> appointmentMedicines) {
        return Database.getPublicRecords(appointmentMedicines);
    }

    public static void removeByIds(String appointmentId, String medicineId) {
        appointmentMedicines.removeIf(appointmentMedicine -> appointmentMedicine.getAppointmentId().equals(appointmentId) && appointmentMedicine.getMedicineId().equals(medicineId));
        Database.saveRecords(appointmentMedicines, appointmentMedicineFile);
    }

    public static String[] getColumnNames() { return new String[] {"Appointment ID", "Medicine ID", "Target Symptom"}; }
}
