package com.floriantoenjes.tudo.util;

public class NoContactRequestException extends Exception {
    public NoContactRequestException() {
        super();
    }

    public NoContactRequestException(String message) {
        super(message);
    }

    public NoContactRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoContactRequestException(Throwable cause) {
        super(cause);
    }

    protected NoContactRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
