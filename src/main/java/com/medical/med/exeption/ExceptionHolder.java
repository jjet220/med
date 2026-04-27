package com.medical.med.exeption;

import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ExceptionHolder {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        log.warn("Validation error: {}", ex.getMessage());

        List<ErrorDetail> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toErrorDetail)
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(errors));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstrainViolation(
            ConstraintViolationException ex) {

        log.warn("Constraint violation: {}", ex.getMessage());

        List<ErrorDetail> errors = ex.getConstraintViolations()
                .stream()
                .map(this::toErrorDetail)
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(errors));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {
        String paramName = ex.getName();
        String requiredType = ex.getRequiredType() != null
                ? ex.getRequiredType().getSimpleName()
                : "unknown";

        log.warn("Type mismatch for parameter '{}': {}", paramName, ex.getMessage());

        ErrorResponse error = ErrorResponse.of(
                Code.INVALID_PARAMETER.name(),
                String.format("Параметр '%s' должен быть типа %s", paramName, requiredType));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParams(
            MissingServletRequestParameterException ex) {

        log.warn("Missing parameter: {}", ex.getParameterName());

        ErrorResponse error = ErrorResponse.of(
                Code.MISSING_PARAMETER.name(),
                String.format("Параметр '%s' обязателен", ex.getParameterName()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(error);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonParseError(
            HttpMessageNotReadableException ex) {

        log.warn("JSON parse error: {}", ex.getMessage());

        ErrorResponse error = ErrorResponse.of(
                Code.INVALID_JSON.name(),
                "Неверный формат JSON запроса"
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ErrorResponse> handleTimeParse(
            DateTimeParseException ex) {

        log.warn("Date parse error: {}", ex.getMessage());

        ErrorResponse error = ErrorResponse.of(
                Code.INVALID_DATE_FORMAT.name(),
                "Неверный формат даты. Используйте гггг-ММ-дд"
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex) {

        log.warn("Business error [{}]: {}", ex.getCode(), ex.getMessage());

        ErrorResponse error = ErrorResponse.of(ex.getCode(), ex.getMessage());

        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {

        log.error("Unexpected error", ex);

        ErrorResponse error = ErrorResponse.of(
                Code.INTERNAL_SERVER_ERROR.name(),
                "Внутренняя ошибка сервера"
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

    private ErrorDetail toErrorDetail(FieldError fieldError) {
        String field = fieldError.getField();
        String message = fieldError.getDefaultMessage();
        String code = "VALIDATION_" + field.toUpperCase();

        return ErrorDetail.of(code, field + ": " + message);
    }

    private ErrorDetail toErrorDetail(ConstraintViolation<?> violation) {
        String path = violation.getPropertyPath().toString();
        String field = path.substring(path.lastIndexOf('.') + 1);
        String message = violation.getMessage();
        String code = "VALIDATION_" + field.toUpperCase();

        return ErrorDetail.of(code, message);
    }

}
