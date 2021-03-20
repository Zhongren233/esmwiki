package moe.zr.esmwiki.producer.exception.handler;

public class MultiResultException extends RuntimeException{
    public MultiResultException() {
        super();
    }

    public MultiResultException(String message) {
        super(message);
    }

    public MultiResultException(String message, Throwable cause) {
        super(message, cause);
    }

    public MultiResultException(Throwable cause) {
        super(cause);
    }

    protected MultiResultException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
