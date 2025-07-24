package customExceptions;

public class InsufficientApWalletException extends RuntimeException {
    public InsufficientApWalletException(String message) {
        super(message);
    }
}
