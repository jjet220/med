package com.medical.med.DTO.response;

import com.medical.med.DTO.PolicyOMSDTO;
import com.medical.med.model.Address;
import com.medical.med.model.IdentityDocument;
import com.medical.med.model.enums.SexType;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponse {

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
    private List<AttachmentResponse> attachments;
    private List<IdentityDocumentResponse> identityDocument;
    private List<AddressResponse> address;
}
