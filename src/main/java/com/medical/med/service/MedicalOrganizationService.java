package com.medical.med.service;

import com.medical.med.DTO.request.CreateMedicalOrganizationRequest;
import com.medical.med.DTO.response.MedicalOrganizationResponse;

public interface MedicalOrganizationService {
    MedicalOrganizationResponse createMedicalOrganization(CreateMedicalOrganizationRequest medicalOrganization);
}
