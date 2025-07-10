package operation;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import database.Identifiable;
import database.Savable;

public class Invoice implements Savable<Invoice> {
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

    public List<String> createRecord() {
        String dbId = this.id;
        String dbAppointmentId = this.id;
        String dbPaymentMethod = this.paymentMethod;

        return new ArrayList<>(Arrays.asList(
                dbId, dbAppointmentId, dbPaymentMethod
        ));
    }

    public Invoice createInstanceFromRecord(List<String> record) {
        String invoiceId = record.getFirst();
        String invoiceAppointmentId = record.get(1);
        String invoicePaymentMethod = record.getLast();

        return new Invoice(invoiceId, invoiceAppointmentId, invoicePaymentMethod);
    }
}
