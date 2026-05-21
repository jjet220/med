package com.medical.med.annotation.validation;

import com.medical.med.annotation.validation.validator.AttachmentDateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AttachmentDateValidator.class)
@Documented
public @interface ValidAttachmentDate {
    String message() default "Дата прикрепления некорректны";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
