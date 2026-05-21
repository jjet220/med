package com.medical.med.annotation.validation;

import com.medical.med.annotation.validation.validator.AttachmentTypeValidator;
import com.medical.med.annotation.validation.validator.PhoneNumberValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AttachmentTypeValidator.class)
@Documented
public @interface ValidAttachmentType {
    String message() default "Тип прикрепления не существует";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
