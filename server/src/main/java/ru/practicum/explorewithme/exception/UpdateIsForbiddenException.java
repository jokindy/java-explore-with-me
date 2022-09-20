package ru.practicum.explorewithme.exception;

public class UpdateIsForbiddenException extends RuntimeException {
    public UpdateIsForbiddenException(String message) {
        super(message);
    }
}
