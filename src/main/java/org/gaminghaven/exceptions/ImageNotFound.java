package org.gaminghaven.exceptions;

public class ImageNotFound extends Exception {

    public ImageNotFound(String message) {
        super(message);
    }
    public ImageNotFound(String message, Throwable cause) {
        super(message, cause);
    }

}
