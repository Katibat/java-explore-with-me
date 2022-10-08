package ru.practicum.explorewithme.exception;

public class IncorrectEventStateException extends RuntimeException {
    public IncorrectEventStateException(String message) {
        super(message);
    }
}