package operation;

import database.Identifiable;

public class CustomerFeedback implements Identifiable {
    private String id;
    private String customerId;
    private String content;
    private String targetEmployeeId;

    public CustomerFeedback(String id, String customerId, String content, String targetEmployeeId) {
        this.id = id;
        this.customerId = customerId;
        this.content = content;
        this.targetEmployeeId = targetEmployeeId;
    }

    public CustomerFeedback(String customerId, String content, String targetEmployeeId) {
        this(Identifiable.createId('F'), customerId, content, targetEmployeeId);
    }

    public String getId() { return this.id; }
    public String getCustomerId() { return this.customerId; }
    public String getContent() { return this.content; }
    public String getTargetEmployeeId() { return this.targetEmployeeId; }

    public void setCustomerId(String customerId) { this.customerId = customerId ; }
    public void setContent(String content) { this.content = content; }
    public void setTargetEmployeeId(String content) { this.content = content; }
}
