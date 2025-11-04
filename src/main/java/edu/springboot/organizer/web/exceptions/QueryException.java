package edu.springboot.organizer.web.exceptions;

public class QueryException extends RuntimeException {
    private static final long serialVersionUID = -432303318396233531L;

    public QueryException(String message) {
        super(message);
    }

    public QueryException() {
    }

    public QueryException(String message, Throwable cause) {
        super(message, cause);
    }

    public QueryException(Throwable cause) {
        super(cause);
    }

    public QueryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
