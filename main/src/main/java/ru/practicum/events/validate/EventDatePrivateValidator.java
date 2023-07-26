package ru.practicum.events.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class EventDatePrivateValidator implements ConstraintValidator<EventDatePrivateConstraint, LocalDateTime> {
    @Override
    public void initialize(EventDatePrivateConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        return value != null && LocalDateTime.now().plusHours(2L).isBefore(value);
    }
}
