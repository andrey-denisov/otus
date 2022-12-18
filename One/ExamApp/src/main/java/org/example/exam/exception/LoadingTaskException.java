package org.example.exam.exception;

public class LoadingTaskException extends RuntimeException {
    public LoadingTaskException(String message) {
        super(message);
    }

    public LoadingTaskException(String message, Throwable cause) {
        super(message, cause);
    }
}
