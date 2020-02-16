package exceptions;

public class ApiValidationException extends Exception {
    public ApiValidationException(String message) {
        super(message);
    }
}
