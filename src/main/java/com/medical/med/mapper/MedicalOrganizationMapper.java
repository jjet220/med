package com.medical.med.mapper;

import com.medical.med.DTO.request.CreateMedicalOrganizationRequest;
import com.medical.med.DTO.response.MedicalOrganizationResponse;
import com.medical.med.model.MedicalOrganization;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MedicalOrganizationMapper {

    MedicalOrganization toEntity(CreateMedicalOrganizationRequest request);

    MedicalOrganizationResponse toResponse(MedicalOrganization medicalOrganization);
}
