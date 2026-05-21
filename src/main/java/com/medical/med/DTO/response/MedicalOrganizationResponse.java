package com.medical.med.DTO.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalOrganizationResponse {
    private Long id;
    private String name;
    private String code;
    private AttachmentResponse attachment;
}
