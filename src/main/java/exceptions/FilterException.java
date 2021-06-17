package main.java.exceptions;

public class FilterException extends Exception {
    public FilterException() {
        super();
    }

    public FilterException(String message) {
        super(message);
    }

    public FilterException(String message, Exception cause) {
        super(message, cause);
    }
}
