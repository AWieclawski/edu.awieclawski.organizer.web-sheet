package edu.springboot.organizer.web.exceptions;

public class DtoValidationException extends RuntimeException {
    private static final long serialVersionUID = -631303318396233095L;

    public DtoValidationException(String message) {
        super(message);
    }

    public DtoValidationException() {
    }

    public DtoValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DtoValidationException(Throwable cause) {
        super(cause);
    }

    public DtoValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
