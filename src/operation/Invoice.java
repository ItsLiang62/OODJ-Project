package operation;

import database.Identifiable;

public class Invoice implements Identifiable {
    private String id;
    private String appointmentId;
    private String paymentMethod;

    public Invoice(String id, String appointmentId, String paymentMethod) {
        this.id = id;
        this.appointmentId = appointmentId;
        this.paymentMethod = paymentMethod;
    }

    public Invoice(String appointmentId, String paymentMethod) {
        this(Identifiable.createId('I'), appointmentId, paymentMethod);
    }

    public String getId() { return this.id; }
    public String getAppointmentId() { return this.appointmentId; }
    public String getPaymentMethod() { return this.paymentMethod; }

    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
