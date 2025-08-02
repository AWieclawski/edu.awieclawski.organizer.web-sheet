package edu.springboot.organizer.data.exceptions;

public class NoEntitySavedException extends RuntimeException {
    private static final long serialVersionUID = -4575303318396233831L;

    public NoEntitySavedException(String message) {
        super(message);
    }

    public NoEntitySavedException() {
    }

    public NoEntitySavedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoEntitySavedException(Throwable cause) {
        super(cause);
    }

    public NoEntitySavedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
