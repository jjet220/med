package com.medical.med.DTO.response;

import com.medical.med.DTO.PolicyOMSDTO;
import com.medical.med.model.enums.SexType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientWithActiveAttachmentResponse {
    private Long id;
    private String surname;
    private String name;
    private String patronymic;
    private LocalDate dateOfBirth;
    private SexType sex;
    private String phoneNumber;
    private String email;
    private String SNILS;
    private PolicyOMSDTO policy;
    private AttachmentResponse activeAttachment;
}
