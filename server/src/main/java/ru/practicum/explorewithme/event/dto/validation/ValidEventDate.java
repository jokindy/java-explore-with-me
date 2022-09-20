package ru.practicum.explorewithme.event.dto.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = EventDateValidator.class)
@Documented
public @interface ValidEventDate {

    String message() default "Invalid event date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
