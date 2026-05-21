package com.medical.med.DTO.response;

import com.medical.med.model.enums.DocumentType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdentityDocumentResponse {

    private Long id;
    private DocumentType type;
    private String series;
    private String number;
    private LocalDate issuedAt;
    private String issuedBy;
    private PatientResponse patient;
}
