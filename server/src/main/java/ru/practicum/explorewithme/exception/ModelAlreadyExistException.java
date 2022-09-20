package ru.practicum.explorewithme.exception;

public class ModelAlreadyExistException extends RuntimeException {
    public ModelAlreadyExistException(String s) {
        super(s);
    }
}