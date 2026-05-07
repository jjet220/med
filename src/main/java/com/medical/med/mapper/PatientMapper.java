package com.medical.med.mapper;

import com.medical.med.DTO.CreatePatientRequest;
import com.medical.med.DTO.PatientResponse;
import com.medical.med.DTO.PolicyOMSDTO;
import com.medical.med.model.Patient;
import com.medical.med.model.PolicyOMS;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PatientMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "policyOMS", ignore = true)
    Patient toEntity(CreatePatientRequest request);

    @Mapping(source = "policyOMS", target = "policy", qualifiedByName = "policyToDto")
    PatientResponse toResponse(Patient patient);

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

}
