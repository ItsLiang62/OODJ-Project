package customExceptions;

public class AppointmentDoesNotBelongToDoctorException extends RuntimeException {
    public AppointmentDoesNotBelongToDoctorException(String message) {
        super(message);
    }
}
