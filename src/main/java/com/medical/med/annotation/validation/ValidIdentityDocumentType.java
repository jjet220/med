package com.medical.med.annotation.validation;

import com.medical.med.annotation.validation.validator.IdentityDocumentTypeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IdentityDocumentTypeValidator.class)
@Documented
public @interface ValidIdentityDocumentType {
    String message() default "Тип документа не существует";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
