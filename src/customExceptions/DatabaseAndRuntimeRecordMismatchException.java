package customExceptions;

public class DatabaseAndRuntimeRecordMismatchException extends RuntimeException {
    public DatabaseAndRuntimeRecordMismatchException(String message) {
        super(message);
    }
}
