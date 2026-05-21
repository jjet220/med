package com.medical.med.mapper;

import com.medical.med.DTO.request.CreateIdentityDocumentRequest;
import com.medical.med.DTO.response.IdentityDocumentResponse;
import com.medical.med.model.IdentityDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IdentityDocumentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patient", ignore = true)
    IdentityDocument toEntity(CreateIdentityDocumentRequest request);

    IdentityDocumentResponse toResponse(IdentityDocument identityDocument);
}
