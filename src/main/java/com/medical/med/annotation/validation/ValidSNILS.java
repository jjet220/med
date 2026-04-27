package com.medical.med.annotation.validation;

import com.medical.med.annotation.validation.validator.SNILSValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SNILSValidator.class)
@Documented
public @interface ValidSNILS {
    String message() default "Неверный формат СНИЛС (должно быть 11 цифр)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

