package com.medical.med.annotation.validation.validator;

import com.medical.med.annotation.validation.ValidIdentityDocumentType;
import com.medical.med.model.enums.DocumentType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IdentityDocumentTypeValidator implements ConstraintValidator<ValidIdentityDocumentType, String> {

    @Override
    public void initialize(ValidIdentityDocumentType constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return false;
        }
        try {
            DocumentType.valueOf(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                    "Допустимые типы: PASSPORT, CERTIFICATE_OF_BIRTH")
                    .addConstraintViolation();
            return false;
        }
    }
}
