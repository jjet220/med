package com.medical.med.mapper;

import com.medical.med.DTO.request.CreateEmailMessageRequest;
import com.medical.med.DTO.response.EmailMessageResponse;
import com.medical.med.model.EmailMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmailMessageMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "shippingTime", expression = "java(java.time.LocalDateTime.now())")
    EmailMessage toEntity(CreateEmailMessageRequest request);

    EmailMessageResponse toResponse(EmailMessage entity);
}
