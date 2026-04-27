package com.medical.med.exeption;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BusinessException{

    public ResourceNotFoundException(String resourceName, Long id) {
        super(Code.RESOURCE_NOT_FOUND.name(), String.format("%s не найден с id: %d", resourceName, id),
                HttpStatus.NOT_FOUND);
    }

    public ResourceNotFoundException(String resourceName, String field, String value) {
        super(Code.RESOURCE_NOT_FOUND.name(),
                String.format("%s не найден с %s: %s", resourceName, field, value)
                        ,HttpStatus.NOT_FOUND);
    }

    public ResourceNotFoundException(String field, String value) {
        super(Code.RESOURCE_NOT_FOUND.name(),
                String.format("%s не найден со зачением: %s", field, value)
                ,HttpStatus.NOT_FOUND);
    }

    public ResourceNotFoundException(String resourceName, Object object) {
        super(Code.RESOURCE_NOT_FOUND.name(),
                String.format("%s не найден со значением: %s", resourceName, object)
                ,HttpStatus.NOT_FOUND);
    }

}
