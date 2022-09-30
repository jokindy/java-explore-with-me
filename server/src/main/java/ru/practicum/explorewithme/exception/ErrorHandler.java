package ru.practicum.explorewithme.exception;

import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleUnrelatedEntitiesException(final CommentingIsForbiddenException e) {
        return ApiError.builder()
                .status(HttpStatus.FORBIDDEN)
                .reason("For the requested operation the conditions are not met.")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    // Попытка оставить запрос на отмененное или неопубликованное событие

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleEventIsNotAvailableException(final EventIsNotAvailableException e) {
        return ApiError.builder()
                .status(HttpStatus.FORBIDDEN)
                .reason("For the requested operation the conditions are not met.")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    //Попытка оставить запрос на свое собственное мероприятие

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleUserIsInitiatorException(final UserIsInitiatorException e) {
        return ApiError.builder()
                .status(HttpStatus.CONFLICT)
                .reason("Integrity constraint has been violated")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    // Попытка обновить чужой запрос или событие

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleUserNotInitiatorException(final UserNotInitiatorException e) {
        return ApiError.builder()
                .status(HttpStatus.FORBIDDEN)
                .reason("For the requested operation the conditions are not met.")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    // Попытка обновить уже обновленный объект

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleUpdateIsForbiddenException(final UpdateIsForbiddenException e) {
        return ApiError.builder()
                .status(HttpStatus.FORBIDDEN)
                .reason("For the requested operation the conditions are not met.")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    // Попытка добавить существующий объект в БД (нарушение уникальности поля)

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
        String message = NestedExceptionUtils.getMostSpecificCause(e).getMessage();
        return ApiError.builder()
                .status(HttpStatus.CONFLICT)
                .reason("Integrity constraint has been violated")
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // Объект не найден

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleModelNotFoundException(final ModelNotFoundException e) {
        return ApiError.builder()
                .status(HttpStatus.NOT_FOUND)
                .reason("The required object not found")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    // Ошибка валидации

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValid(final MethodArgumentNotValidException e) {
        String objectName = e.getObjectName();
        int errorCount = e.getErrorCount();
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason(String.format("During [%s] validation found %s errors", objectName, errorCount))
                .errors(getErrors(e.getFieldErrors()))
                .timestamp(LocalDateTime.now())
                .build();
    }

    private List<String> getErrors(List<FieldError> fieldErrors) {
        List<String> errors = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            errors.add("Field: " + fieldError.getField() +
                    ". Error: " + fieldError.getDefaultMessage() +
                    ". Value: " + fieldError.getRejectedValue());
        }
        return errors;
    }


}
