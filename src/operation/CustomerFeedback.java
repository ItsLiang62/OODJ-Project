package operation;

import customExceptions.InvalidForeignKeyValueException;
import customExceptions.NullOrEmptyValueRejectedException;
import database.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomerFeedback implements Identifiable {
    private final String id;
    private final String customerId;
    private final String nonManagerEmployeeId;
    private String content;


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
        this(IdCreator.createId('F'), customerId, nonManagerEmployeeId, content);

    }

    public String getId() { return this.id; }
    public String getCustomerId() { return this.customerId; }
    public String getNonManagerEmployeeId() { return this.nonManagerEmployeeId; }
    public String getContent() { return this.content; }

    public void setContent(String content) {
        checkContent(content);
        this.content = content;
        Database.removeCustomerFeedback(this.id);
        Database.addCustomerFeedback(this);
    }

    public static void checkCustomerId(String customerId) {
        if (customerId == null || customerId.isBlank()) {
            throw new NullOrEmptyValueRejectedException("Customer ID of customer feedback must not be null or empty!");
        }
        if (!Database.getAllCustomerId().contains(customerId)) {
            throw new InvalidForeignKeyValueException("Customer ID of CustomerFeedback does not exist!");
        }
    }

    public static void checkNonManagerEmployeeId(String nonManagerEmployeeId) {
        if (nonManagerEmployeeId == null || nonManagerEmployeeId.isBlank()) {
            throw new NullOrEmptyValueRejectedException("Non-manager employee ID of customer feedback must not be null or empty!");
        }
        if (!Database.getAllStaffId().contains(nonManagerEmployeeId) && !Database.getAllDoctorId().contains(nonManagerEmployeeId)) {
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

    public List<String> createPublicRecord() {
        return this.createDbRecord();
    }

    public static void createCustomerFeedbackFromRecord(List<String> record) {
        String id = record.getFirst();
        String customerId = record.get(1);
        String nonManagerEmployeeId = record.get(2);
        String content = record.getLast();

        CustomerFeedback customerFeedback = new CustomerFeedback(id, customerId, nonManagerEmployeeId, content);
        Database.addCustomerFeedback(customerFeedback);
    }

    public static String[] getColumnNames() { return new String[] {"Customer Feedback ID", "Customer ID", "Employee ID", "Content"}; }
}
