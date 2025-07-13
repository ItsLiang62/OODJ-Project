package operation;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import customExceptions.NullValueRejectedException;
import database.Identifiable;
import database.Savable;

public class Medicine implements Savable {
    private String id;
    private String name;
    private double charge;

    public Medicine(String id, String name, double charge) {
        checkName(name);
        this.id = id;
        this.name = name;
        this.charge = charge;
    }

    public Medicine(String name, double charge) {
        this(Identifiable.createId('P'), name, charge);
    }

    private void checkName(String name) {
        if (name == null) {
            throw new NullValueRejectedException("--- name field of Medicine object must not be null ---");
        }
    }
    public String getId() { return this.id; }
    public String getName() { return this.name; }
    public double getCharge() { return this.charge; }

    public void setName(String name) {
        checkName(name);
        this.name = name;
    }
    public void setCharge(double charge) { this.charge = charge; }

    public List<String> createRecord() {
        String dbId = this.id;
        String dbName = this.name;
        String dbCharge = String.valueOf(this.charge);

        return new ArrayList<>(Arrays.asList(
                dbId, dbName, dbCharge
        ));
    }

    public static Medicine createMedicineFromRecord(List<String> record) {
        String medicineId = record.getFirst();
        String medicineName = record.get(1);
        double medicineCharge = Double.parseDouble(record.getLast());

        return new Medicine(medicineId, medicineName, medicineCharge);
    }
}
