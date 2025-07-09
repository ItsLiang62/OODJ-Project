package operation;

public class Invoice {
    private String id;
    private String appointmentId;
    private String paymentMethod;

    public String getId() { return this.id; }
    public String getAppointmentId() { return this.appointmentId; }
    public String getPaymentMethod() { return this.paymentMethod; }

    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
