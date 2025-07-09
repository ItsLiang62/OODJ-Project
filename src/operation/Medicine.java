package operation;

import database.Identifiable;

public class Medicine implements Identifiable {
    private String id;
    private String name;
    private double charge;

    public Medicine(String id, String name, double charge) {
        this.id = id;
        this.name = name;
        this.charge = charge;
    }

    public Medicine(String name, double charge) {
        this(Identifiable.createId('P'), name, charge);
    }

    public String getId() { return this.id; }
    public String getName() { return this.name; }
    public double getCharge() { return this.charge; }

    public void setName(String name) { this.name = name; }
    public void setCharge(double charge) { this.charge = charge; }
}
