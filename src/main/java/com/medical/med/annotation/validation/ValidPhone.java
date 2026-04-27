package com.medical.med.annotation.validation;

import com.medical.med.annotation.validation.validator.PhoneNumberValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy =  PhoneNumberValidator.class)
@Documented
public @interface ValidPhone {
    String message() default "Неверный формат номера телефона. Должен быть: +7XXXXXXXXXX (10-15 цифр)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

