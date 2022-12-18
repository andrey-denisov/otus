package org.example.exam.exception;

public class ExamExecutionException extends RuntimeException {
    public ExamExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
