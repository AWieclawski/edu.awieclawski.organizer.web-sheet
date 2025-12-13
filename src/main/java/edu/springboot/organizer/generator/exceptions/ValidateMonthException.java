package edu.springboot.organizer.generator.exceptions;

public class ValidateMonthException extends RuntimeException {
    private static final long serialVersionUID = 4575307428396233692L;

    public ValidateMonthException(String message) {
        super(message);
    }

    public ValidateMonthException() {
    }

    public ValidateMonthException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidateMonthException(Throwable cause) {
        super(cause);
    }

    public ValidateMonthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
