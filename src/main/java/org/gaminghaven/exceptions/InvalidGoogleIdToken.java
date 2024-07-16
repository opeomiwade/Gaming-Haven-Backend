package org.gaminghaven.exceptions;

public class InvalidGoogleIdToken extends Exception {

    public InvalidGoogleIdToken(String message) {
        super(message);
    }
    public InvalidGoogleIdToken(String message, Throwable cause) {
        super(message, cause);
    }

}
