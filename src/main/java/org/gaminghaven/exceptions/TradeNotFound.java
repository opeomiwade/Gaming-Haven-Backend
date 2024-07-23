package org.gaminghaven.exceptions;

public class TradeNotFound extends Exception {
    public TradeNotFound(String message) {
        super(message);
    }

    public TradeNotFound(String message, Throwable cause) {
        super(message, cause);

    }
}
