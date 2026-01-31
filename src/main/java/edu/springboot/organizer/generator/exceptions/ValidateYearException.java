package edu.springboot.organizer.generator.exceptions;

public class ValidateYearException extends RuntimeException {
    private static final long serialVersionUID = 6319307428396235310L;

    public ValidateYearException(String message) {
        super(message);
    }

    public ValidateYearException() {
    }

    public ValidateYearException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidateYearException(Throwable cause) {
        super(cause);
    }

    public ValidateYearException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
