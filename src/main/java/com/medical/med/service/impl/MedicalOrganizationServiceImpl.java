package com.medical.med.service.impl;


import com.medical.med.DTO.request.CreateMedicalOrganizationRequest;
import com.medical.med.DTO.response.MedicalOrganizationResponse;
import com.medical.med.mapper.MedicalOrganizationMapper;
import com.medical.med.model.MedicalOrganization;
import com.medical.med.repository.MedicalOrganizationRepository;
import com.medical.med.service.MedicalOrganizationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MedicalOrganizationServiceImpl implements MedicalOrganizationService {

    private final MedicalOrganizationRepository medicalOrganizationRepository;
    private final MedicalOrganizationMapper medicalOrganizationMapper;

    @Override
    public MedicalOrganizationResponse createMedicalOrganization(CreateMedicalOrganizationRequest request) {

        MedicalOrganization medicalOrganization = medicalOrganizationMapper.toEntity(request);

        MedicalOrganization savedOrganization = medicalOrganizationRepository.save(medicalOrganization);

        return medicalOrganizationMapper.toResponse(savedOrganization);
    }
}
