package com.medical.med.DTO;

import com.medical.med.annotation.validation.ValidSinglePolicyNumber;
import com.medical.med.model.Patient;
import com.medical.med.model.PolicyOMS;
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

    public static PolicyOMSDTO fromEntity(PolicyOMS policyOMS){
        return PolicyOMSDTO.builder()
                .id(policyOMS.getId())
                .patientId(policyOMS.getPatient() != null ? policyOMS.getPatient().getId() : null)
                .dateAndTimeOfCreation(policyOMS.getDateAndTimeOfCreation())
                .singlePolicyNumber(policyOMS.getSinglePolicyNumber())
                .build();
    }

    public PolicyOMS toEntity() {
        return PolicyOMS.builder()
                .id(this.id)
                .dateAndTimeOfCreation(this.dateAndTimeOfCreation)
                .singlePolicyNumber(this.singlePolicyNumber)
                .build();
    }
}
