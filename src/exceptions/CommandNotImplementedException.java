package exceptions;

public class CommandNotImplementedException extends Exception {
    public CommandNotImplementedException() {
        super();
    }

    public CommandNotImplementedException(String message) {
        super(message);
    }
}
