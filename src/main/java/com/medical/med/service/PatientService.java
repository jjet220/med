package com.medical.med.service;

import com.medical.med.DTO.*;
import com.medical.med.DTO.request.CreatePatientRequest;
import com.medical.med.DTO.response.AttachmentResponse;
import com.medical.med.DTO.response.PatientResponse;
import com.medical.med.DTO.response.PatientWithActiveAttachmentResponse;
import com.medical.med.model.enums.SexType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

public interface PatientService {

    PatientResponse updatePatient(CreatePatientRequest patient, Long patientId );

    void deletedPatient(Long patientId);

    PatientWithActiveAttachmentResponse findPatientById(Long patientId);

    PatientResponse createPatient(CreatePatientRequest patient, Long policyId);

    Page<PatientWithActiveAttachmentResponse> findPatientByFIO(Pageable pageable, String FIO);

    Page<PatientWithActiveAttachmentResponse> findPatientByDateOfBirth(Pageable pageable, LocalDate dateOfBirth);

    Page<PatientWithActiveAttachmentResponse> findPatientBySex(Pageable pageable, SexType sexType);

    Optional<PatientWithActiveAttachmentResponse> findPatientByPhoneNumber(String phoneNumber);

    Optional<PatientWithActiveAttachmentResponse> findPatientByEmail(String email);

    Optional<PatientWithActiveAttachmentResponse> findPatientBySNILS(String SNILS);

    Optional<PatientWithActiveAttachmentResponse> findPatientByPolicyOMS(PolicyOMSDTO policyOMS);

    Optional<AttachmentResponse> findLastAttachment(PatientResponse patient);
}
