package operation;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import customExceptions.InvalidMedicineNameException;
import customExceptions.NegativeValueRejectedException;
import customExceptions.RecordAlreadyInDatabaseException;
import database.*;
import user.Customer;

public class Medicine implements Identifiable {
    private final String id;
    private String name;
    private double charge;

    private static final Set<Medicine> medicines = new TreeSet<>(Database.getOrderedById());
    private static final File medicineFile = new File("data/medicine.txt");

    static {
        Database.populateFromRecords(Medicine::createFromDbRecord, medicineFile);
    }

    public Medicine(String id, String name, double charge) {
        checkName(name);
        checkCharge(charge);
        this.id = id;
        this.name = name;
        this.charge = charge;
    }

    public Medicine(String name, double charge) {
        this(Medicine.createId(), name, charge);
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public double getCharge() {
        return this.charge;
    }

    public void setName(String name) {
        checkName(name);
        this.name = name;
        Medicine.removeById(this.id, false);
        Medicine.add(this);
    }

    public void setCharge(double charge) {
        checkCharge(charge);
        this.charge = charge;
        Medicine.removeById(this.id, false);
        Medicine.add(this);
    }

    public static void checkName(String name) {
        Pattern namePattern = Pattern.compile("^([A-Z][A-Za-z0-9()/-]*(\\s([0-9()/-]|[A-Z])[A-Za-z0-9()/-]*)*)$");
        Matcher nameMatcher = namePattern.matcher(name);
        if (!nameMatcher.matches()) {
            throw new InvalidMedicineNameException("Invalid Medicine Name!");
        }
        if (Medicine.getFieldVals(Medicine.getAll(), Medicine::getName).contains(name)) {
            throw new RecordAlreadyInDatabaseException("Medicine name already exists!");
        }
    }

    public static void checkCharge(double charge) {
        if (charge < 0) {
            throw new NegativeValueRejectedException("Medicine charge must be equal or more than 0!");
        }
    }

    public List<String> createDbRecord() {
        String dbName = this.name;
        String dbCharge = String.valueOf(this.charge);

        return new ArrayList<>(Arrays.asList(
                this.id, dbName, dbCharge
        ));
    }

    public List<String> getPublicRecord() {
        return this.createDbRecord();
    }

    public static void createFromDbRecord(List<String> record) {
        String medicineId = record.getFirst();
        String medicineName = record.get(1);
        double medicineCharge = Double.parseDouble(record.getLast());

        Medicine medicine = new Medicine(medicineId, medicineName, medicineCharge);
        Medicine.add(medicine);
    }

    public static String createId() {
        return Database.createId(medicines, 'P');
    }

    public static void add(Medicine newMedicine) {
        Database.add(newMedicine, medicines, medicineFile);
    }

    public static Set<Medicine> getAll() {
        return new LinkedHashSet<>(medicines);
    }

    public static Medicine getById(String medicineId) {
        return Database.getById(medicines, medicineId, "medicine");
    }

    public static <R> Set<Medicine> getFiltered(
            Function<Medicine, R> fieldValReturner,
            R fieldVal) {
        return Database.getFiltered(medicines, fieldValReturner, fieldVal);
    }

    public static <R> List<R> getFieldVals(Set<Medicine> medicines, Function<Medicine, R> fieldValReturner) {
        return Database.getFieldVals(medicines, fieldValReturner);
    }

    public static List<List<String>> getPublicRecords(Set<Medicine> medicines) {
        return Database.getPublicRecords(medicines);
    }

    public static void removeById(String id, boolean removeDependencies) {
        if (removeDependencies) {
            for (AppointmentMedicine appointmentMedicine: AppointmentMedicine.getAll()) {
                if (appointmentMedicine.getMedicineId().equals(id)) {
                    AppointmentMedicine.removeByIds(appointmentMedicine.getAppointmentId(), appointmentMedicine.getMedicineId());
                }
            }
        }
        Database.removeById(medicines, id, medicineFile);
    }

    public static String[] getColumnNames() {
        return new String[]{"Medicine ID", "Medicine Name", "Charge"};
    }
}
