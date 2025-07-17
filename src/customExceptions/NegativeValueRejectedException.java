package customExceptions;

public class NegativeValueRejectedException extends RuntimeException {
    public NegativeValueRejectedException(String message) {
        super(message);
    }
}
