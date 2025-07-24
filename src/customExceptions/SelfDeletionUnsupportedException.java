package customExceptions;

public class SelfDeletionUnsupportedException extends RuntimeException {
    public SelfDeletionUnsupportedException(String message) {
        super(message);
    }
}
