package main.java.exceptions;

public class InvalidCoffeeSortException extends Exception {
    public InvalidCoffeeSortException() {
        super();
    }

    public InvalidCoffeeSortException(String message) {
        super(message);
    }

    public InvalidCoffeeSortException(String message, Exception e) {
        super(message, e);
    }
}
