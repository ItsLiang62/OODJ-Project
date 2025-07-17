package customExceptions;

public class InvalidMedicineNameException extends RuntimeException {
    public InvalidMedicineNameException(String message) {
        super(message);
    }
}
