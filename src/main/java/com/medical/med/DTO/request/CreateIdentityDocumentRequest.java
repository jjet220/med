package com.medical.med.DTO.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.medical.med.annotation.validation.ValidIdentityDocumentType;
import com.medical.med.model.enums.DocumentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateIdentityDocumentRequest {

    @NotNull(message = "Тип обязательно")
    private DocumentType type;

    @NotBlank(message = "Серия обязательно")
    @Size(min = 4, max = 4, message = "Серия документа не соответствует формату")
    private String series;

    @NotBlank(message = "Номер паспорта обязательно")
    @Size(min = 6, max = 6, message = "Номер документа не соответствует формату")
    private String number;

    @NotNull(message = "Дата выдачи обязательна")
    private LocalDate issuedAt;

    @NotBlank(message = "Кем выдан паспорта обязательно")
    @Size(min = 4, max = 100, message = "От 4 до 100 символов")
    private String issuedBy;

    @JsonProperty("patientId")
    private Long patientId;
}
