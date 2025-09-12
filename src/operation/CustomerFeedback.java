package operation;

import customExceptions.InvalidForeignKeyValueException;
import customExceptions.NullOrEmptyValueRejectedException;
import database.*;
import user.Customer;
import user.Doctor;
import user.Staff;

import java.io.File;
import java.util.*;
import java.util.function.Function;

public class CustomerFeedback implements Identifiable {
    private final String id;
    private final String customerId;
    private final String nonManagerEmployeeId;
    private String content;

    private static final Set<CustomerFeedback> customerFeedbacks = new TreeSet<>(Database.getOrderedById());
    private static final File customerFeedbackFile = new File("data/customerFeedback.txt");

    static {
        Database.populateFromRecords(CustomerFeedback::createFromDbRecord, customerFeedbackFile);
    }

    public CustomerFeedback(String id, String customerId, String nonManagerEmployeeId, String content) {
        checkCustomerId(customerId);
        checkNonManagerEmployeeId(nonManagerEmployeeId);
        checkContent(content);
        this.id = id;
        this.customerId = customerId;
        this.nonManagerEmployeeId = nonManagerEmployeeId;
        this.content = content;
    }

    public CustomerFeedback(String customerId, String nonManagerEmployeeId, String content) {
        this(CustomerFeedback.createId(), customerId, nonManagerEmployeeId, content);

    }

    public String getId() {
        return this.id;
    }

    public String getCustomerId() {
        return this.customerId;
    }

    public String getNonManagerEmployeeId() {
        return this.nonManagerEmployeeId;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        checkContent(content);
        this.content = content;
        CustomerFeedback.removeById(this.id);
        CustomerFeedback.add(this);
    }

    public static void checkCustomerId(String customerId) {
        if (customerId == null || customerId.isBlank()) {
            throw new NullOrEmptyValueRejectedException("Customer ID of customer feedback must not be null or empty!");
        }
        if (!Customer.getFieldVals(Customer.getAll(), Customer::getId).contains(customerId)) {
            throw new InvalidForeignKeyValueException("Customer ID of CustomerFeedback does not exist!");
        }
    }

    public static void checkNonManagerEmployeeId(String nonManagerEmployeeId) {
        if (nonManagerEmployeeId == null || nonManagerEmployeeId.isBlank()) {
            throw new NullOrEmptyValueRejectedException("Non-manager employee ID of customer feedback must not be null or empty!");
        }
        if (!Staff.getFieldVals(Staff.getAll(), Staff::getId).contains(nonManagerEmployeeId) && !Doctor.getFieldVals(Doctor.getAll(), Doctor::getId).contains(nonManagerEmployeeId)) {
            throw new InvalidForeignKeyValueException("Non-manager employee ID of customer feedback object does not exist!");
        }
    }

    public static void checkContent(String content) {
        if (content == null || content.isBlank()) {
            throw new NullOrEmptyValueRejectedException("Customer feedback content must not be null or empty!");
        }
    }

    public List<String> createDbRecord() {
        String dbContent = this.content;

        return new ArrayList<>(Arrays.asList(
                id, customerId, nonManagerEmployeeId, dbContent
        ));
    }

    public List<String> getPublicRecord() {
        return this.createDbRecord();
    }

    public static void createFromDbRecord(List<String> record) {
        String id = record.getFirst();
        String customerId = record.get(1);
        String nonManagerEmployeeId = record.get(2);
        String content = record.getLast();

        CustomerFeedback customerFeedback = new CustomerFeedback(id, customerId, nonManagerEmployeeId, content);
        CustomerFeedback.add(customerFeedback);
    }

    public static String createId() {
        return Database.createId(customerFeedbacks, 'F');
    }

    public static void add(CustomerFeedback newCustomerFeedback) {
        Database.add(newCustomerFeedback, customerFeedbacks, customerFeedbackFile);
    }

    public static Set<CustomerFeedback> getAll() {
        return new LinkedHashSet<>(customerFeedbacks);
    }

    public static CustomerFeedback getById(String customerFeedbackId) {
        return Database.getById(customerFeedbacks, customerFeedbackId, "customer feedback");
    }

    public static <R> Set<CustomerFeedback> getFiltered(
            Function<CustomerFeedback, R> fieldValReturner,
            R fieldVal) {
        return Database.getFiltered(customerFeedbacks, fieldValReturner, fieldVal);
    }

    public static <R> List<R> getFieldVals(Set<CustomerFeedback> customerFeedbacks, Function<CustomerFeedback, R> fieldValReturner) {
        return Database.getFieldVals(customerFeedbacks, fieldValReturner);
    }

    public static List<List<String>> getPublicRecords(Set<CustomerFeedback> customerFeedbacks) {
        return Database.getPublicRecords(customerFeedbacks);
    }

    public static void removeById(String id) {
        Database.removeById(customerFeedbacks, id, customerFeedbackFile);
    }

    public static String[] getColumnNames() {
        return new String[] {"Customer Feedback ID", "Customer ID", "Employee ID", "Content"};
    }
}
