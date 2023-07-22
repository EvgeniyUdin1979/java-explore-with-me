package ru.practicum.events.validate;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = EventDatePrivateUpdateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD,})
@Retention(RetentionPolicy.RUNTIME)
public @interface EventDatePrivateUpdateConstraint {
    String message() default "{validation.EventDatePrivateUpdateConstraint}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
