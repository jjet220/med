package com.medical.med.mapper;

import com.medical.med.DTO.response.AttachmentResponse;
import com.medical.med.DTO.request.CreateAttachmentRequest;
import com.medical.med.model.Attachment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AttachmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "medicalOrganization", ignore = true)
    Attachment toEntity(CreateAttachmentRequest request);

    @Mapping(source = "medicalOrganization.code", target = "moCode")
    @Mapping(source = "patient", target = "patient")
    AttachmentResponse toResponse(Attachment attachment);
}
