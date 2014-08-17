package net.elehack.repoproxy.util;

public class InvalidPathException extends IllegalArgumentException {
    public InvalidPathException() {
    }

    public InvalidPathException(String s) {
        super(s);
    }

    public InvalidPathException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidPathException(Throwable cause) {
        super(cause);
    }
}
