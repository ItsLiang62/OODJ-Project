package customExceptions;

public class RootManagerUnmodifiableException extends RuntimeException {
    public RootManagerUnmodifiableException(String message) {
        super(message);
    }
}
