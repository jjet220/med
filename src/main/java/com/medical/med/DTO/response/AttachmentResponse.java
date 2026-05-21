package com.medical.med.DTO.response;

import com.medical.med.model.enums.AttachmentType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentResponse {
    private Long id;
    private AttachmentType type;
    private LocalDate dateOfBegin;
    private LocalDate dateOfEnd;
    private PatientResponse patient;
    private String moCode;
}
