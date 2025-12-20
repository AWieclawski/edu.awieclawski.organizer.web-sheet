package edu.springboot.organizer.data.exceptions;

public class SanitizeQueryException extends RuntimeException {
    private static final long serialVersionUID = -4852303318396926831L;

    public SanitizeQueryException(String message) {
        super(message);
    }

    public SanitizeQueryException() {
    }

    public SanitizeQueryException(String message, Throwable cause) {
        super(message, cause);
    }

    public SanitizeQueryException(Throwable cause) {
        super(cause);
    }

    public SanitizeQueryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
