package com.floriantoenjes.tudo.util;

public class NoContactException extends Exception {
    public NoContactException() {
        super();
    }

    public NoContactException(String message) {
        super(message);
    }

    public NoContactException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoContactException(Throwable cause) {
        super(cause);
    }

    protected NoContactException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
