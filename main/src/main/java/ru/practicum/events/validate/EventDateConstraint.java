package ru.practicum.events.validate;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = EventDateValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, })
@Retention(RetentionPolicy.RUNTIME)
public @interface EventDateConstraint {
    String message() default "validation.EventDateConstraint";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
