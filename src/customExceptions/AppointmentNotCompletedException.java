package customExceptions;

public class AppointmentNotCompletedException extends RuntimeException {
    public AppointmentNotCompletedException(String message) {
        super(message);
    }
}
