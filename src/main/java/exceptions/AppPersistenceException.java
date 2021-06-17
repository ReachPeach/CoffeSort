package main.java.exceptions;

public class AppPersistenceException extends Exception {
    public AppPersistenceException() {
    }

    public AppPersistenceException(String message) {
        super(message);
    }

    public AppPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
