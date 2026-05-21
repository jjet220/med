package com.medical.med.service;

import com.medical.med.DTO.request.CreateAddressRequest;
import com.medical.med.DTO.response.AddressResponse;

import java.util.List;

public interface AddressService {

    List<AddressResponse> getAddressOfPatient(Long patientId);

    AddressResponse createAddress(CreateAddressRequest request, Long patientId);

    void deleteAddress(Long patientId, Long addressId);
}
