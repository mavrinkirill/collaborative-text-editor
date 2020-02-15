package exceptions;

public class ApiValidationException extends Exception {
    public ApiValidationException() {
        super();
    }

    public ApiValidationException(String message) {
        super(message);
    }
}
