package uk.gov.dwp.uc.pairtest.exception;

public class InvalidPurchaseException extends RuntimeException {
    public InvalidPurchaseException(String message) {
        super(message); // Call the superclass constructor with the message
    }

    // Optional: You can add more constructors if needed
    public InvalidPurchaseException(String message, Throwable cause) {
        super(message, cause); // Call the superclass constructor with message and cause
    }
}
