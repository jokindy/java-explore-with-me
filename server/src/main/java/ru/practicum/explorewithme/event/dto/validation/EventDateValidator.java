package ru.practicum.explorewithme.event.dto.validation;

import ru.practicum.explorewithme.event.dto.NewEventDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Duration;
import java.time.LocalDateTime;

public class EventDateValidator implements ConstraintValidator<ValidEventDate, Object> {

    private static final int SECONDS_PER_HOUR = 3600;

    @Override
    public void initialize(ValidEventDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
        NewEventDto eventDto = (NewEventDto) obj;
        LocalDateTime eventDate = eventDto.getEventDate();
        Duration duration = Duration.between(LocalDateTime.now(), eventDate);
        long hours = duration.toSeconds() / SECONDS_PER_HOUR;
        return hours > 2;
    }
}
