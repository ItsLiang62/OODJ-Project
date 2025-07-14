package operation;

import customExceptions.InvalidForeignKeyValueException;
import customExceptions.NullValueRejectedException;
import database.Database;
import database.Identifiable;
import database.Savable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomerFeedback implements Savable {
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
        Database.addCustomerFeedback(this);
    }

    public CustomerFeedback(String customerId, String nonManagerEmployeeId, String content) {
        this(Identifiable.createId('F'), customerId, nonManagerEmployeeId, content);

    }

    public String getId() { return this.id; }
    public String getCustomerId() { return this.customerId; }
    public String getNonManagerEmployeeId() { return this.nonManagerEmployeeId; }
    public String getContent() { return this.content; }

    public void setCustomerId(String customerId) {
        checkCustomerId(customerId);
        this.customerId = customerId;
    }

    public void setNonManagerEmployeeId(String nonManagerEmployeeId) {
        checkNonManagerEmployeeId(nonManagerEmployeeId);
        this.nonManagerEmployeeId = nonManagerEmployeeId;
    }

    public void setContent(String content) {
        checkContent(content);
        this.content = content;
    }

    public static void checkCustomerId(String customerId) {
        if (customerId == null) {
            throw new NullValueRejectedException("--- customerId field of CustomerFeedback object must not be null ---");
        }
        if (!Database.getAllCustomerId().contains(customerId)) {
            throw new InvalidForeignKeyValueException("--- customerId field of CustomerFeedback object does not have a primary key reference ---");
        }
    }

    public static void checkNonManagerEmployeeId(String nonManagerEmployeeId) {
        if (nonManagerEmployeeId == null) {
            throw new NullValueRejectedException("--- nonManagerEmployeeId field of CustomerFeedback object must not be null ---");
        }
        if (!Database.getAllStaffId().contains(nonManagerEmployeeId) && !Database.getAllDoctorId().contains(nonManagerEmployeeId)) {
            throw new InvalidForeignKeyValueException("--- nonManagerEmployeeId field of CustomerFeedback object does not have a primary key reference ---");
        }
    }

    public static void checkContent(String content) {
        if (content == null) {
            throw new NullValueRejectedException("--- content field of CustomerFeedback object must not be null ---");
        }
    }

    public List<String> createRecord() {
        String dbCustomerId = this.customerId;
        String dbTargetEmployeeId = this.nonManagerEmployeeId;
        String dbContent = this.content;

        return new ArrayList<>(Arrays.asList(
                this.id, dbCustomerId, dbTargetEmployeeId, dbContent
        ));
    }

    public static void createCustomerFeedbackFromRecord(List<String> record) {
        String customerFeedbackId = record.getFirst();
        String customerFeedbackCustomerId = record.get(1);
        String customerFeedbackTargetEmployeeId = record.get(2);
        String customerFeedbackContent = record.getLast();

        new CustomerFeedback(customerFeedbackId, customerFeedbackCustomerId, customerFeedbackTargetEmployeeId, customerFeedbackContent);
    }
}
