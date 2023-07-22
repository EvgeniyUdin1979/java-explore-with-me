package ru.practicum.events.validate;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = EventDateAdminUpdateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD,})
@Retention(RetentionPolicy.RUNTIME)
public @interface EventDateAdminUpdateConstraint {
    String message() default "{validation.EventDateAdminUpdateConstraint}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
