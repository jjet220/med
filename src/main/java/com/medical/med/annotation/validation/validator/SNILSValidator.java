package com.medical.med.annotation.validation.validator;

import com.medical.med.annotation.validation.ValidSNILS;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class SNILSValidator implements ConstraintValidator<ValidSNILS, String> {

    private static final String SNILS_PATTERN = "^[0-9]{11}$";

    @Override
    public boolean isValid(String SNILS, ConstraintValidatorContext constraintValidatorContext) {
        if (SNILS == null || SNILS.isBlank()) {
            return false;
        }

        if (!SNILS.matches(SNILS_PATTERN)) {
            return false;
        }

        return isCorrectSNILS(SNILS);
    }

    private boolean isCorrectSNILS(String SNILS){

        String numberPart = SNILS.substring(0, 9);

        int expectedControl = Integer.parseInt(SNILS.substring(9, 11));

        int[] digits = numberPart.chars()
                .map(Character :: getNumericValue)
                .toArray();

        int sum = 0;

        for (int i = 0; i < digits.length; i++) {
            sum += digits[i] * (9 - i);
        }

        int calculatedControl;
        if (sum < 100) {
            calculatedControl = sum;
        } else if (sum == 100 || sum == 101) {
            calculatedControl = 0;
        } else {
            calculatedControl = sum % 101;
            if (calculatedControl == 100) {
                calculatedControl = 0;
            }
        }

        return calculatedControl == expectedControl;
    }
}
