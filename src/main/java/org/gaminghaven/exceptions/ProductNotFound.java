package org.gaminghaven.exceptions;

public class ProductNotFound extends Exception {
    public ProductNotFound(String message) {
        super(message);
    }

    public ProductNotFound(String message , Throwable cause) {
        super(message, cause);

    }

}

