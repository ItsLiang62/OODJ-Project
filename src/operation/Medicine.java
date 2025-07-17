package operation;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import customExceptions.InvalidMedicineNameException;
import customExceptions.NegativeValueRejectedException;
import customExceptions.NullOrEmptyValueRejectedException;
import customExceptions.RecordAlreadyInDatabaseException;
import database.Database;
import database.Identifiable;
import database.Savable;

public class Medicine implements Savable {
    private String id;
    private String name;
    private double charge;

    public Medicine(String id, String name, double charge) {
        checkName(name);
        checkCharge(charge);
        this.id = id;
        this.name = name;
        this.charge = charge;
        Database.addMedicine(this);
    }

    public Medicine(String name, double charge) {
        this(Identifiable.createId('P'), name, charge);
    }

    public static void checkName(String name) {
        Pattern namePattern = Pattern.compile("^([A-Z][A-Za-z0-9()/-]*(\\s([0-9()/-]|[A-Z])[A-Za-z0-9()/-]*)*)$");
        Matcher nameMatcher = namePattern.matcher(name);
        if (!nameMatcher.matches()) {
            throw new InvalidMedicineNameException("--- name field of Medicine object is invalid ---");
        }
        if (Database.getAllMedicineNames().contains(name)) {
            throw new RecordAlreadyInDatabaseException("--- name field of Medicine object is being used by another Medicine ---");
        }
    }

    public static void checkCharge(double charge) {
        if (charge < 0) {
            throw new NegativeValueRejectedException("--- charge field of Medicine object must be equal or more than 0 ---");
        }
    }

    public String getId() { return this.id; }
    public String getName() { return this.name; }
    public double getCharge() { return this.charge; }

    public void setName(String name) {
        checkName(name);
        this.name = name;
        Database.removeMedicine(this.id, false);
        Database.addMedicine(this);
    }

    public void setCharge(double charge) {
        checkCharge(charge);
        Database.removeMedicine(this.id, false);
        Database.addMedicine(this);
        this.charge = charge;
    }

    public List<String> createRecord() {
        String dbId = this.id;
        String dbName = this.name;
        String dbCharge = String.valueOf(this.charge);

        return new ArrayList<>(Arrays.asList(
                dbId, dbName, dbCharge
        ));
    }

    public static void createMedicineFromRecord(List<String> record) {
        String medicineId = record.getFirst();
        String medicineName = record.get(1);
        double medicineCharge = Double.parseDouble(record.getLast());

        new Medicine(medicineId, medicineName, medicineCharge);
    }
}
