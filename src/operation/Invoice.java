package operation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import customExceptions.*;
import database.*;

public class Invoice implements Identifiable {
    private String id;
    private String appointmentId;;
    private LocalDate creationDate;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");

    public Invoice(String id, String appointmentId, LocalDate creationDate) {
        checkAppointmentId(appointmentId);
        this.id = id;
        this.appointmentId = appointmentId;
        this.creationDate = creationDate;
    }

    public Invoice(String appointmentId) {
        this(IdCreator.createId('I'), appointmentId, LocalDate.now());
    }

    public static void checkAppointmentId(String appointmentId) {
        if (appointmentId == null || appointmentId.isBlank()) {
            throw new NullOrEmptyValueRejectedException("Appointment ID of invoice must not be empty!");
        }
        if (!Database.getAllAppointmentId().contains(appointmentId)) {
            throw new InvalidForeignKeyValueException("Appointment ID does not exist!");
        }
        if (Database.getAllAppointmentIdInInvoices().contains(appointmentId)) {
            throw new AppointmentAlreadyHasInvoiceException("Appointment already has an invoice!");
        }
    }

    public String getId() { return this.id; }
    public String getAppointmentId() { return this.appointmentId; }
    public String getCreationDate() { return this.creationDate.format(formatter); }

    public List<String> createDbRecord() {
        String dbId = this.id;
        String dbAppointmentId = this.appointmentId;
        String dbCreationDate = this.creationDate.format(formatter);

        return new ArrayList<>(Arrays.asList(
                dbId, dbAppointmentId, dbCreationDate
        ));
    }

    public List<String> createPublicRecord() {
        return this.createDbRecord();
    }

    public static void createInvoiceFromRecord(List<String> record) {
        String invoiceId = record.getFirst();
        String invoiceAppointmentId = record.get(1);
        String invoiceCreationDate = record.getLast();

        Invoice invoice = new Invoice(invoiceId, invoiceAppointmentId, LocalDate.parse(invoiceCreationDate, formatter));
        Database.addInvoice(invoice);
    }
}
