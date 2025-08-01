package customExceptions;

public class CustomerFeedbackDoesNotBelongToCustomerException extends RuntimeException {
    public CustomerFeedbackDoesNotBelongToCustomerException(String message) {
        super(message);
    }
}
