package com.medical.med.annotation.validation.validator;

import com.medical.med.annotation.validation.ValidSinglePolicyNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SinglePolicyNumberValidator implements ConstraintValidator<ValidSinglePolicyNumber, String> {

    private static final String POLICY_PATTERN = "^[0-9]{16}$";

    @Override
    public void initialize(ValidSinglePolicyNumber constraintAnnotation) {
    }

    @Override
    public boolean isValid(String spn, ConstraintValidatorContext constraintValidatorContext) {

        if (spn == null || spn.isBlank()) {
            return false;
        }

        if (!spn.matches(POLICY_PATTERN)) {
            return false;
        }

        return true;
    }
}
