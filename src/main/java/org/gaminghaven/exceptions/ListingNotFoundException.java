package org.gaminghaven.exceptions;

public class ListingNotFoundException extends Exception {
    public ListingNotFoundException(String message) {
        super(message);
    }

    public ListingNotFoundException(String message , Throwable cause) {
        super(message, cause);

    }

}

