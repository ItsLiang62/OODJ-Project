package customExceptions;

public class AppointmentNotAssignedToDoctorException extends RuntimeException {
    public AppointmentNotAssignedToDoctorException(String message) {
        super(message);
    }
}
