package ru.practicum.explorewithme.exception;

public class CommentingIsForbiddenException extends RuntimeException {
    public CommentingIsForbiddenException(String message) {
        super(message);
    }
}
