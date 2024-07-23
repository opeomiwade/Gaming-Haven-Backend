package org.gaminghaven.exceptions;

public class OfferNotFound extends Exception {
    public OfferNotFound(String message) {
        super(message);
    }

    public OfferNotFound(String message, Throwable cause) {
        super(message, cause);

    }

}
