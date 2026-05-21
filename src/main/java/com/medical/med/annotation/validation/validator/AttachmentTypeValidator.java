package com.medical.med.annotation.validation.validator;

import com.medical.med.annotation.validation.ValidAttachmentType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class AttachmentTypeValidator implements ConstraintValidator<ValidAttachmentType, String> {

    private static final Set<String> ALLOWED_TYPES = Set.of("АМБУЛАТОРНОЕ", "СТОМАТОЛОГИЧЕСКОЕ");

    @Override
    public void initialize(ValidAttachmentType constraintAnnotation) {
    }

    @Override
    public boolean isValid(String attachmentType, ConstraintValidatorContext constraintValidatorContext) {

        if (attachmentType == null) {
            return false;
        }

        return ALLOWED_TYPES.contains(attachmentType.toUpperCase());
    }
}
