package com.medical.med.annotation.validation.validator;

import com.medical.med.DTO.request.CreateAttachmentRequest;
import com.medical.med.annotation.validation.ValidAttachmentDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class AttachmentDateValidator implements ConstraintValidator<ValidAttachmentDate, Object> {

    @Override
    public void initialize(ValidAttachmentDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {

        if (object instanceof CreateAttachmentRequest request) {

            LocalDate begin = request.getDateOfBegin();
            LocalDate end = request.getDateOfEnd();
            LocalDate currentDate = LocalDate.now();

            if (begin != null && begin.isAfter(currentDate)) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext
                        .buildConstraintViolationWithTemplate("У пациента найдено более актуальное прикрепление")
                        .addPropertyNode("dateOfBegin").addConstraintViolation();
                return false;
            }

            if (begin != null && end != null && begin.isAfter(end)) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext
                        .buildConstraintViolationWithTemplate("Дата начала прикрепления не может быть больше текущей даты")
                        .addPropertyNode("dateOfBegin").addConstraintViolation();
                return false;
            }
        }

        return true;
    }
}
