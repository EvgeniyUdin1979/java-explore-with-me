package ru.practicum.events.validate;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = EventDatePrivateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD,})
@Retention(RetentionPolicy.RUNTIME)
public @interface EventDatePrivateConstraint {
    String message() default "{validation.EventDatePrivateConstraint}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
