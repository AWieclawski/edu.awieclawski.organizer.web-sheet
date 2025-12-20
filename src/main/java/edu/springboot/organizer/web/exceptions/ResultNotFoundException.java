package edu.springboot.organizer.web.exceptions;

public class ResultNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -425303318396233652L;

    public ResultNotFoundException(String message) {
        super(message);
    }

    public ResultNotFoundException() {
    }

    public ResultNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResultNotFoundException(Throwable cause) {
        super(cause);
    }

    public ResultNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
