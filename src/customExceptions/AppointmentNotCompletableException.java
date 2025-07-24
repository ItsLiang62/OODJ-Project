package customExceptions;

public class AppointmentNotCompletableException extends RuntimeException {
    public AppointmentNotCompletableException(String message) {
        super(message);
    }
}
