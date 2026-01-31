package edu.springboot.organizer.data.exceptions;

public class NotImplementedException extends RuntimeException {

    private static final long serialVersionUID = -3467857708926624876L;

    public NotImplementedException(String message) {
        super(message);
    }

    public NotImplementedException() {
    }
}
