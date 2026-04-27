package com.medical.med.annotation.validation.validator;

import com.medical.med.annotation.validation.ValidPhone;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhone, String> {

    private static final String PHONE_PATTERN = "^\\+?[0-9]{10,15}$";

    @Override
    public void initialize(ValidPhone constraintAnnotation) {
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            return false;
        }

        return phoneNumber.matches(PHONE_PATTERN);
    }
}
