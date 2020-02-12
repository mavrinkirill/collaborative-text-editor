package exceptions;

public class CommandNullValueException extends Exception {
    public CommandNullValueException() {
        super();
    }

    public CommandNullValueException(String message) {
        super(message);
    }
}
