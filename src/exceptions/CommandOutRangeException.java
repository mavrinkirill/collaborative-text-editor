package exceptions;

public class CommandOutRangeException extends Exception {
    public CommandOutRangeException() {
        super();
    }

    public CommandOutRangeException(String message) {
        super(message);
    }
}
