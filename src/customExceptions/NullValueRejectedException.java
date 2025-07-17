package customExceptions;

public class NullValueRejectedException extends RuntimeException {
    public NullValueRejectedException(String message) {
        super(message);
    }
}
