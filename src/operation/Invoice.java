package operation;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;

import customExceptions.*;
import database.*;
import user.Customer;

public class Invoice implements Identifiable {
    private final String id;
    private final String appointmentId;
    private final LocalDate creationDate;
    private final double totalAmount;

    private static final Set<Invoice> invoices = new TreeSet<>(Database.getOrderedById());
    private static final File invoiceFile = new File("data/invoice.txt");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");

    static {
        Database.populateFromRecords(Invoice::createFromDbRecord, invoiceFile);
    }

    public Invoice(
            String id, String appointmentId, LocalDate creationDate, double totalAmount
    ) {
        checkAppointmentId(appointmentId);
        this.id = id;
        this.appointmentId = appointmentId;
        this.creationDate = creationDate;
        this.totalAmount = totalAmount;
    }

    public Invoice(String appointmentId) {
        this(
                Invoice.createId(),
                appointmentId, LocalDate.now(),
                Appointment.getById(appointmentId).getTotalCharge()
        );
    }

    public String getId() {
        return this.id;
    }

    public String getAppointmentId() {
        return this.appointmentId;
    }

    public LocalDate getCreationDate() {
        return this.creationDate;
    }

    public String getCreationDateStr() {
        return this.creationDate.format(formatter);
    }

    public double getTotalAmount() {
        return this.totalAmount;
    }

    public List<String> createDbRecord() {
        String dbCreationDate = this.creationDate.format(formatter);
        String dbTotalCharge = String.valueOf(this.totalAmount);

        return new ArrayList<>(Arrays.asList(
                this.id, this.appointmentId, dbCreationDate, dbTotalCharge
        ));
    }

    public List<String> getPublicRecord() {
        return this.createDbRecord();
    }

    public static void checkAppointmentId(String appointmentId) {
        if (appointmentId == null || appointmentId.isBlank()) {
            throw new NullOrEmptyValueRejectedException("Appointment ID of invoice must not be empty!");
        }
        if (!Appointment.getFieldVals(Appointment.getAll(), Appointment::getId).contains(appointmentId)) {
            throw new InvalidForeignKeyValueException("Appointment ID does not exist!");
        }
        if (Invoice.getFieldVals(Invoice.getAll(), Invoice::getAppointmentId).contains(appointmentId)) {
            throw new AppointmentAlreadyHasInvoiceException("Appointment already has an invoice!");
        }
    }

    public static void createFromDbRecord(List<String> record) {
        String invoiceId = record.getFirst();
        String invoiceAppointmentId = record.get(1);
        String invoiceCreationDate = record.get(2);
        double invoiceTotalCharge = Double.parseDouble(record.getLast());

        Invoice invoice = new Invoice(
                invoiceId,
                invoiceAppointmentId,
                LocalDate.parse(invoiceCreationDate, formatter),
                invoiceTotalCharge
        );
        Invoice.add(invoice);
    }

    public static String createId() {
        return Database.createId(invoices, 'I');
    }

    public static void add(Invoice newInvoice) {
        Database.add(newInvoice, invoices, invoiceFile);
    }

    public static Set<Invoice> getAll() {
        return new LinkedHashSet<>(invoices);
    }

    public static Invoice getById(String invoiceId) {
        return Database.getById(invoices, invoiceId, "invoice");
    }

    public static <R> Set<Invoice> getFiltered(
            Function<Invoice, R> fieldValReturner,
            R fieldVal) {
        return Database.getFiltered(invoices, fieldValReturner, fieldVal);
    }

    public static <R> List<R> getFieldVals(Set<Invoice> invoices, Function<Invoice, R> fieldValReturner) {
        return Database.getFieldVals(invoices, fieldValReturner);
    }

    public static List<List<String>> getPublicRecords(Set<Invoice> invoice) {
        return Database.getPublicRecords(invoice);
    }

    public static void removeById(String id) {
        Database.removeById(invoices, id, invoiceFile);
    }

    public static String[] getColumnNames() { return new String[] {"Invoice ID", "Appointment ID", "Creation Date", "Total Charge"}; }
}
