package com.medical.med.exeption;

import org.springframework.http.HttpStatus;

public class ConflictException extends  BusinessException{

    public ConflictException(String field, String value) {
        super("DUPLICATE_" + field.toUpperCase(),
                String.format("%s с таким значением уже существует: %s", field, value)
                        ,HttpStatus.CONFLICT);
    }
}
