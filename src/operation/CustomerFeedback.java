package operation;

import database.Identifiable;
import database.Savable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomerFeedback implements Savable<CustomerFeedback> {
    private String id;
    private String customerId;
    private String targetEmployeeId;
    private String content;


    public CustomerFeedback(String id, String customerId, String targetEmployeeId, String content) {
        this.id = id;
        this.customerId = customerId;
        this.targetEmployeeId = targetEmployeeId;
        this.content = content;
    }

    public CustomerFeedback(String customerId, String targetEmployeeId, String content) {
        this(Identifiable.createId('F'), customerId, targetEmployeeId, content);
    }

    public String getId() { return this.id; }
    public String getCustomerId() { return this.customerId; }
    public String getTargetEmployeeId() { return this.targetEmployeeId; }
    public String getContent() { return this.content; }

    public void setCustomerId(String customerId) { this.customerId = customerId ; }
    public void setTargetEmployeeId(String content) { this.content = content; }
    public void setContent(String content) { this.content = content; }

    public List<String> createRecord() {
        String dbId = this.id;
        String dbCustomerId = this.customerId;
        String dbTargetEmployeeId = this.targetEmployeeId;
        String dbContent = this.content;

        return new ArrayList<>(Arrays.asList(
                dbId, dbCustomerId, dbTargetEmployeeId, dbContent
        ));
    }

    public CustomerFeedback createInstanceFromRecord(List<String> record) {
        String customerFeedbackId = record.getFirst();
        String customerFeedbackCustomerId = record.get(1);
        String customerFeedbackTargetEmployeeId = record.get(2);
        String customerFeedbackContent = record.getLast();

        return new CustomerFeedback(customerFeedbackId, customerFeedbackCustomerId, customerFeedbackTargetEmployeeId, customerFeedbackContent);
    }
}
