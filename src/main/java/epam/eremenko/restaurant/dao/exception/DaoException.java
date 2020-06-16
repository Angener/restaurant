package epam.eremenko.restaurant.dao.exception;

public class DaoException extends Exception {
    private static final long serialVersionUID = 1L;

    public DaoException() {
    }

    public DaoException(String message) {
        super(message);
    }

    public DaoException(Exception ex) {
        super(ex);
    }

    public DaoException(String message, Exception ex) {
        super(message, ex);
    }
}
