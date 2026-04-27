package com.medical.med.exeption;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private List<ErrorDetail> errors;

    public static ErrorResponse of(String code, String message) {
        return ErrorResponse.builder()
                .errors(List.of(ErrorDetail.of(code, message)))
                .build();
    }

    public static ErrorResponse of(List<ErrorDetail> errors) {
        return ErrorResponse.builder()
                .errors(errors)
                .build();
    }
}
