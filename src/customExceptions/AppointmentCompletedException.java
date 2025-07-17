package customExceptions;

public class AppointmentCompletedException extends RuntimeException {
    public AppointmentCompletedException(String message) {
        super(message);
    }
}
