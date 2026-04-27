package com.medical.med.annotation.validation;

import com.medical.med.annotation.validation.validator.SinglePolicyNumberValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SinglePolicyNumberValidator.class)
@Documented
public @interface ValidSinglePolicyNumber {
    String message() default "Неверный номер единого медицинского полиса. Должно быть: ТТД ГГГГММДДNNNN К (16 цифр)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
