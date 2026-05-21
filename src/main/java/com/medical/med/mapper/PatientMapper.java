package com.medical.med.mapper;

import com.medical.med.DTO.*;
import com.medical.med.DTO.request.CreatePatientRequest;
import com.medical.med.DTO.response.AttachmentResponse;
import com.medical.med.DTO.response.PatientResponse;
import com.medical.med.DTO.response.PatientWithActiveAttachmentResponse;
import com.medical.med.model.Attachment;
import com.medical.med.model.Patient;
import com.medical.med.model.PolicyOMS;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.time.LocalDate;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PatientMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "policyOMS", ignore = true)
    Patient toEntity(CreatePatientRequest request);

    @Mapping(source = "policyOMS", target = "policy", qualifiedByName = "policyToDto")
    PatientResponse toResponse(Patient patient);

    @Mapping(target = "activeAttachment", expression = "java(getActiveAttachment(patient))")
    @Mapping(source = "policyOMS", target = "policy")
    PatientWithActiveAttachmentResponse toActiveAttachmentResponse(Patient patient);

    default AttachmentResponse getActiveAttachment(Patient patient) {
        if (patient.getAttachments() == null) return null;

        return patient.getAttachments().stream()
                .filter(a -> a.getDateOfEnd() == null || a.getDateOfEnd().isAfter(LocalDate.now()))
                .findFirst()
                .map(this::toAttachmentResponse)
                .orElse(null);
    }

    default Patient toEntity(PatientResponse response) {
        if (response == null) return null;
        return Patient.builder()
                .id(response.getId())
                .surname(response.getSurname())
                .name(response.getName())
                .patronymic(response.getPatronymic())
                .dateOfBirth(response.getDateOfBirth())
                .sex(response.getSex())
                .phoneNumber(response.getPhoneNumber())
                .email(response.getEmail())
                .snils(response.getSNILS())
                .build();
    }

    AttachmentResponse toAttachmentResponse(Attachment attachment);

    @Named("policyToDto")
    default PolicyOMSDTO policyToDto(PolicyOMS policy) {
        if (policy == null) return null;
        return PolicyOMSDTO.builder()
                .id(policy.getId())
                .singlePolicyNumber(policy.getSinglePolicyNumber())
                .dateAndTimeOfCreation(policy.getDateAndTimeOfCreation())
                .patientId(policy.getPatient() != null ? policy.getPatient().getId() : null)
                .build();
    }

    default PolicyOMS toPolicyEntity(PolicyOMSDTO dto) {
        if (dto == null) return null;
        return PolicyOMS.builder()
                .id(dto.getId())
                .singlePolicyNumber(dto.getSinglePolicyNumber())
                .dateAndTimeOfCreation(dto.getDateAndTimeOfCreation())
                .build();
    }
}
