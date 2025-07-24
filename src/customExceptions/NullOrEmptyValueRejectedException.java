package customExceptions;

public class NullOrEmptyValueRejectedException extends RuntimeException {
    public NullOrEmptyValueRejectedException(String message) {
        super(message);
    }
}
