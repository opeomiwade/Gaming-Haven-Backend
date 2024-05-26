package org.gaminghaven.exceptions;

import net.bytebuddy.implementation.bytecode.Throw;

public class UserCreationException extends Exception {
    public UserCreationException(String message) {
        super(message);
    }

    public UserCreationException(String message , Throwable cause) {
        super(message, cause);

    }

}
