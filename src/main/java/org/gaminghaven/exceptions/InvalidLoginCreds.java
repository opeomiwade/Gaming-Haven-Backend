package org.gaminghaven.exceptions;

public class InvalidLoginCreds extends Exception {
    public InvalidLoginCreds(String message) {
        super(message);
    }

    public InvalidLoginCreds(String message, Throwable cause) {
        super(message, cause);
    }
}
