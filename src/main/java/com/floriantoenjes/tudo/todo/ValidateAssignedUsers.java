package com.floriantoenjes.tudo.todo;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = TodoCreatorValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateAssignedUsers {

    String message() default "Todo can only be assigned to creator contacts.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}