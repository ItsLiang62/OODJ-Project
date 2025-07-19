package operation;

import customExceptions.InvalidForeignKeyValueException;
import customExceptions.NullOrEmptyValueRejectedException;
import database.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomerFeedback implements Identifiable {
    private final String id;
    private String customerId;
    private String nonManagerEmployeeId;
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
            throw new NullOrEmptyValueRejectedException("--- customerId field of CustomerFeedback object must not be null or empty ---");
        }
        if (!Database.getAllCustomerId().contains(customerId)) {
            throw new InvalidForeignKeyValueException("--- customerId field of CustomerFeedback object does not have a primary key reference ---");
        }
    }

    public static void checkNonManagerEmployeeId(String nonManagerEmployeeId) {
        if (nonManagerEmployeeId == null || nonManagerEmployeeId.isBlank()) {
            throw new NullOrEmptyValueRejectedException("--- nonManagerEmployeeId field of CustomerFeedback object must not be null or empty ---");
        }
        if (!Database.getAllStaffId().contains(nonManagerEmployeeId) && !Database.getAllDoctorId().contains(nonManagerEmployeeId)) {
            throw new InvalidForeignKeyValueException("--- nonManagerEmployeeId field of CustomerFeedback object does not have a primary key reference ---");
        }
    }

    public static void checkContent(String content) {
        if (content == null || content.isBlank()) {
            throw new NullOrEmptyValueRejectedException("--- content field of CustomerFeedback object must not be null or empty ---");
        }
    }

    public List<String> createDbRecord() {
        String dbCustomerId = this.customerId;
        String dbTargetEmployeeId = this.nonManagerEmployeeId;
        String dbContent = this.content;

        return new ArrayList<>(Arrays.asList(
                this.id, dbCustomerId, dbTargetEmployeeId, dbContent
        ));
    }

    public List<String> createPublicRecord() {
        return this.createDbRecord();
    }

    public static void createCustomerFeedbackFromRecord(List<String> record) {
        String customerFeedbackId = record.getFirst();
        String customerFeedbackCustomerId = record.get(1);
        String customerFeedbackTargetEmployeeId = record.get(2);
        String customerFeedbackContent = record.getLast();

        CustomerFeedback customerFeedback = new CustomerFeedback(customerFeedbackId, customerFeedbackCustomerId, customerFeedbackTargetEmployeeId, customerFeedbackContent);
        Database.addCustomerFeedback(customerFeedback);
    }
}
