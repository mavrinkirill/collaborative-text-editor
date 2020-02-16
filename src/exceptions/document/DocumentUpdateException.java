package exceptions.document;

public class DocumentUpdateException extends Exception {
    public DocumentUpdateException(String message) {
        super(message);
    }

    public DocumentUpdateException(Throwable cause) {
        super(cause);
    }
}
