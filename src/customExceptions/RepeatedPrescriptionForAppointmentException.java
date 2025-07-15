package customExceptions;

public class RepeatedPrescriptionForAppointmentException extends RuntimeException {
    public RepeatedPrescriptionForAppointmentException(String message) {
        super(message);
    }
}
