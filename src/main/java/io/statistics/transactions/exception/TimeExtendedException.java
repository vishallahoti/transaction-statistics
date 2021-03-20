package io.statistics.transactions.exception;

public class TimeExtendedException extends IllegalArgumentException {
    private static final long serialVersionUID = -12345654321L;

    public TimeExtendedException(String message) {
        super(message);
    }
}
