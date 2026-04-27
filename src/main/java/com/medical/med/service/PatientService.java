package com.medical.med.service;

import com.medical.med.model.Patient;
import com.medical.med.model.PolicyOMS;
import com.medical.med.model.SexType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

public interface PatientService {

    Patient findPatientById(Long patientId);

    Patient createPatient(Patient patient, Long policyId);

    Page<Patient> findPatientByFIO(Pageable pageable, String FIO);

    Page<Patient> findPatientByDateOfBirth(Pageable pageable, LocalDate dateOfBirth);

    Page<Patient> findPatientBySex(Pageable pageable, SexType sexType);

    Optional<Patient> findPatientByPhoneNumber(String phoneNumber);

    Optional<Patient> findPatientByEmail(String email);

    Optional<Patient> findPatientBySNILS(String SNILS);

    Optional<Patient> findPatientByPolicyOMS(PolicyOMS policyOMS);
}
