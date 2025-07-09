package customExceptions;

public class RecordAlreadyInDatabaseException extends Exception {
    public RecordAlreadyInDatabaseException(String message) {
        super(message);
    }
}
