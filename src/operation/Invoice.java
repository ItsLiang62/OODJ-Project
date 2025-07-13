package operation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import customExceptions.*;
import database.Database;
import database.Identifiable;
import database.Savable;

public class Invoice implements Savable {
    private String id;
    private String appointmentId;
    private String paymentMethod;
    private LocalDate paymentDate;

    public Invoice(String id, String appointmentId, String paymentMethod, String paymentDate) {
        checkAppointmentId(appointmentId);
        checkPaymentMethod(paymentMethod);
        this.id = id;
        this.appointmentId = appointmentId;
        this.paymentMethod = paymentMethod;
        this.paymentDate = LocalDate.parse(paymentDate, DateTimeFormatter.ofPattern("d/M/yyyy"));
    }

    public Invoice(String appointmentId, String paymentMethod, String paymentDate) {
        this(Identifiable.createId('I'), appointmentId, paymentMethod, paymentDate);
    }

    private void checkAppointmentId(String appointmentId) {
        if (appointmentId == null) {
            throw new NullValueRejectedException("--- appointmentId field of Invoice object must not be null ---");
        }
        if (!Database.getAllAppointmentId().contains(appointmentId)) {
            throw new InvalidForeignKeyValueException("--- appointmentId field of Invoice object does not have a primary key reference ---");
        }
        if (!Database.getAppointment(appointmentId).getStatus().equals("Completed")) {
            throw new AppointmentNotCompletedException("--- appointmentId field of Invoice object points to an Appointment whose status is not Completed ---");
        }
        if (Database.getAllAppointmentIdInInvoices().contains(appointmentId)) {
            throw new AppointmentAlreadyHasInvoiceException("--- appointmentId field of Invoice object points to an Appointment that already has an invoice ---");
        }
    }

    private void checkPaymentMethod(String paymentMethod) {
        if (!Arrays.asList(new String[] {"Cash", "Debit", "Credit", "Digital Wallet"}).contains(paymentMethod)) {
            throw new InvalidPaymentMethodException("--- paymentMethod field of Invoice object must be either Cash, Debit, Credit or Digital Wallet ---");
        }
    }

    public String getId() { return this.id; }
    public String getAppointmentId() { return this.appointmentId; }
    public String getPaymentMethod() { return this.paymentMethod; }
    public String getPaymentDate() { return this.paymentDate.toString(); }

    public void setAppointmentId(String appointmentId) {
        checkAppointmentId(appointmentId);
        this.appointmentId = appointmentId;
    }
    public void setPaymentMethod(String paymentMethod) {
        checkPaymentMethod(paymentMethod);
        this.paymentMethod = paymentMethod;
    }
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
