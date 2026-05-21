package com.medical.med.service.impl;

import com.medical.med.DTO.request.CreateAddressRequest;
import com.medical.med.DTO.response.AddressResponse;
import com.medical.med.exeption.BusinessException;
import com.medical.med.exeption.ResourceNotFoundException;
import com.medical.med.mapper.AddressMapper;
import com.medical.med.model.Address;
import com.medical.med.model.Patient;
import com.medical.med.repository.AddressRepository;
import com.medical.med.repository.PatientRepository;
import com.medical.med.service.AddressService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class AddressServiceImpl implements AddressService {

    private final PatientRepository patientRepository;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Override
    public List<AddressResponse> getAddressOfPatient(Long patientId) {
        log.debug("Поиск адреса у пациента с id: {}", patientId);

        List<Address> response = addressRepository.findByPatientId(patientId);

        return response.stream()
                .map(addressMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AddressResponse createAddress(CreateAddressRequest request, Long patientId) {
        log.debug("Создание адреса у пациента с id: {}, адрес: {}", patientId, request.toString());

        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> {
            log.error("Пациента с такми id {}, не найдено", patientId);
            return new ResourceNotFoundException("Patient", patientId);
        });

        List<Address> addressList = addressRepository.findByPatientId(patientId);

        for (Address address: addressList) {
            if (address.getAddressType().equals(request.getAddressType())) {
                throw new BusinessException(
                        "conflict_address",
                        "У указанного пациента уже есть адрес с таким типом",
                        HttpStatus.CONFLICT
                );
            }
        }

        Address created = Address.builder()
                .addressType(request.getAddressType())
                .country(request.getCountry())
                .apartmentNumber(request.getApartmentNumber())
                .houseNumber(request.getHouseNumber())
                .localityName(request.getLocalityName())
                .region(request.getRegion())
                .street(request.getStreet())
                .isPrivateHouse(request.isPrivateHouse())
                .localityType(request.getLocalityType())
                .patient(patient)
                .build();

        log.debug("Адрес создан с id: {}, у пациента с id: {}", created.getId(), patientId);

        return addressMapper.toResponse(created);
    }

    @Override
    public void deleteAddress(Long patientId, Long addressId) {
        log.debug("Удаление адреса пациента с id: {}, адрес с id: {}", patientId, addressId);

        patientRepository.findById(patientId);

        Address address = addressRepository.findById(addressId).orElseThrow(() -> {
                    log.error("У данного пациента нет адреса с таким идентификатором: {}", addressId);
                    return new ResourceNotFoundException("IdentityDocument", addressId);
                }
        );
        addressRepository.delete(address);
        log.debug("Документ удалён: {}", address);

    }
}
