package exceptions.command;

public class CommandOutRangeException extends Exception {
    public CommandOutRangeException(String message) {
        super(message);
    }
}
