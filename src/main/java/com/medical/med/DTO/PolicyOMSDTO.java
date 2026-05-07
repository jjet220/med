package com.medical.med.DTO;

import com.medical.med.annotation.validation.ValidSinglePolicyNumber;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyOMSDTO {

    private Long id;

    private Long patientId;

    @NotNull(message = "У полиса должна быть дата создания")
    private LocalDateTime dateAndTimeOfCreation;

    @NotBlank(message = "Номер единого медицинского полиса обязателно (16 цифр)")
    @ValidSinglePolicyNumber
    private String singlePolicyNumber;
}
