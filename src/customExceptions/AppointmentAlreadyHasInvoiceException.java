package customExceptions;

public class AppointmentAlreadyHasInvoiceException extends RuntimeException {
    public AppointmentAlreadyHasInvoiceException(String message) {
        super(message);
    }
}
