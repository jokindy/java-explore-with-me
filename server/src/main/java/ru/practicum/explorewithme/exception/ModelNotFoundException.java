package ru.practicum.explorewithme.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModelNotFoundException extends RuntimeException {

    private final String message;

    public ModelNotFoundException(long id, String className) {
        this.message = String.format("%s id: %s not found", className, id);
    }
}
