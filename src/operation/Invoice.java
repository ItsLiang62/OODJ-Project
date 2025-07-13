package operation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import database.Identifiable;
import database.Savable;

public class Invoice implements Savable {
    private String id;
    private String appointmentId;
    private String paymentMethod;
    private LocalDate paymentDate;

    public Invoice(String id, String appointmentId, String paymentMethod, String paymentDate) {
        this.id = id;
        this.appointmentId = appointmentId;
        this.paymentMethod = paymentMethod;
        this.paymentDate = LocalDate.parse(paymentDate, DateTimeFormatter.ofPattern("d/M/yyyy"));
    }

    public Invoice(String appointmentId, String paymentMethod, String paymentDate) {
        this(Identifiable.createId('I'), appointmentId, paymentMethod, paymentDate);
    }

    public String getId() { return this.id; }
    public String getAppointmentId() { return this.appointmentId; }
    public String getPaymentMethod() { return this.paymentMethod; }
    public String getPaymentDate() { return this.paymentDate.toString(); }

    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public void setPaymentDate(String paymentDate) { this.paymentDate = LocalDate.parse(paymentDate, DateTimeFormatter.ofPattern("d/M/yyyy")); }

    public List<String> createRecord() {
        String dbId = this.id;
        String dbAppointmentId = this.id;
        String dbPaymentMethod = this.paymentMethod;
        String dbPaymentDate = this.paymentDate.toString();

        return new ArrayList<>(Arrays.asList(
                dbId, dbAppointmentId, dbPaymentMethod, dbPaymentDate
        ));
    }

    public static Invoice createInvoiceFromRecord(List<String> record) {
        String invoiceId = record.getFirst();
        String invoiceAppointmentId = record.get(1);
        String invoicePaymentMethod = record.get(2);
        String invoicePaymentDate = record.getLast();

        return new Invoice(invoiceId, invoiceAppointmentId, invoicePaymentMethod, invoicePaymentDate);
    }
}
