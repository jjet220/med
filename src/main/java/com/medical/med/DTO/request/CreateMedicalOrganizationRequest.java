package com.medical.med.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMedicalOrganizationRequest {

    @NotBlank(message = "Название организации обязательно")
    @Size(min = 1, max = 80, message = "Название организации от 1 до 80 символов")
    private String name;

    @NotBlank(message = "Код организации обязательно")
    @Size(min = 1, max = 80, message = "Код от 1 до 80 символов")
    private String code;
}
