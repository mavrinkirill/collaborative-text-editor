package exceptions;

public class DocumentInvalidVersionException extends Exception {
    public DocumentInvalidVersionException() {
        super();
    }

    public DocumentInvalidVersionException(String message) {
        super(message);
    }
}
