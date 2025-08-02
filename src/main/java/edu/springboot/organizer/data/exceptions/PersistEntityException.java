package edu.springboot.organizer.data.exceptions;

public class PersistEntityException extends RuntimeException {
    private static final long serialVersionUID = -7315303318396233319L;

    public PersistEntityException(String message) {
        super(message);
    }

    public PersistEntityException() {
    }

    public PersistEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistEntityException(Throwable cause) {
        super(cause);
    }

    public PersistEntityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
