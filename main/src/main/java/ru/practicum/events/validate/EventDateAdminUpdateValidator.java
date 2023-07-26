package ru.practicum.events.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class EventDateAdminUpdateValidator implements ConstraintValidator<EventDateAdminUpdateConstraint, LocalDateTime> {
    @Override
    public void initialize(EventDateAdminUpdateConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        return value == null || LocalDateTime.now().plusHours(1L).isBefore(value);
    }
}
