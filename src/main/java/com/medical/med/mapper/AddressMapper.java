package com.medical.med.mapper;

import com.medical.med.DTO.request.CreateAddressRequest;
import com.medical.med.DTO.response.AddressResponse;
import com.medical.med.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddressMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patient", ignore = true)
    Address toEntity(CreateAddressRequest request);

    AddressResponse toResponse(Address address);
}
