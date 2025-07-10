package customExceptions;

public class MultipleAppointmentsForUserException extends RuntimeException {
    public MultipleAppointmentsForUserException(String message) {
        super(message);
    }
}
