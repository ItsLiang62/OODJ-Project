package customExceptions;

public class InvalidForeignKeyValueException extends RuntimeException {
    public InvalidForeignKeyValueException(String message) {
        super(message);
    }
}
