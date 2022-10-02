package ru.practicum.explorewithme.exception;

public class EntityIsNotAvailableException extends RuntimeException {
    public EntityIsNotAvailableException(String message) {
        super(message);
    }
}
