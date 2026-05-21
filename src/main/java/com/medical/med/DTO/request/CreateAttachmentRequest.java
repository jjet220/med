package com.medical.med.DTO.request;

import com.medical.med.annotation.validation.ValidAttachmentDate;
import com.medical.med.annotation.validation.ValidAttachmentType;
import com.medical.med.model.enums.AttachmentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ValidAttachmentDate
public class CreateAttachmentRequest {

    @NotNull(message = "Тип прикрепления обязательно")
    private AttachmentType type;

    @NotNull(message = "Дата создания обязательна")
    @PastOrPresent(message = "Дата начала не может быть в будущем")
    private LocalDate dateOfBegin;

    private LocalDate dateOfEnd;

    private Long patientId;

    @NotBlank(message = "Код МО обязателен")
    @Size(min = 3, max = 50, message = "Код МО от 3 до 50 символов")
    private String moCode;
}
