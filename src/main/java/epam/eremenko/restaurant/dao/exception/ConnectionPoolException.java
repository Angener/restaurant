package epam.eremenko.restaurant.dao.exception;

public class ConnectionPoolException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ConnectionPoolException() {
    }

    public ConnectionPoolException(String message) {
        super(message);
    }

    public ConnectionPoolException(Exception ex) {
        super(ex);
    }

    public ConnectionPoolException(String message, Exception ex) {
        super(message, ex);
    }
}
