package customExceptions;

public class RecordAlreadyInDatabaseException extends RuntimeException {
    public RecordAlreadyInDatabaseException(String message) {
        super(message);
    }
}
