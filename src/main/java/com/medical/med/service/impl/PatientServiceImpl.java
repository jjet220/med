package com.medical.med.service.impl;

import com.medical.med.exeption.ConflictException;
import com.medical.med.exeption.ResourceNotFoundException;
import com.medical.med.model.Patient;
import com.medical.med.model.PolicyOMS;
import com.medical.med.model.SexType;
import com.medical.med.repository.PatientRepository;
import com.medical.med.repository.PolicyRepository;
import com.medical.med.service.PatientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final PolicyRepository policyRepository;

    public PatientServiceImpl(PatientRepository patientRepository, PolicyRepository policyRepository) {
        this.patientRepository = patientRepository;
        this.policyRepository = policyRepository;
    }

    @Override
    public Patient findPatientById(Long patientId) {
        return patientRepository.findPatientById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", patientId));
    }

    @Override
    public Patient createPatient(Patient patient, Long policyId) {

        if (patient.getEmail() != null
                && patientRepository.existsByEmail(patient.getEmail())) {
            throw new ConflictException(
                    "email",
                    patient.getEmail()
                    );
        }

        if (patient.getPhoneNumber() != null
                && patientRepository.existsByPhoneNumber(patient.getPhoneNumber())) {
            throw new ConflictException(
                    "phone",
                    patient.getPhoneNumber()
            );

        }

        if (patient.getSNILS() != null && patientRepository.existsBySNILS(patient.getSNILS())) {
            throw new ConflictException(
                    "snils",
                    patient.getSNILS()
            );

        }

        if (policyId != null) {
            PolicyOMS policy = policyRepository.findById(policyId)
                    .orElseThrow(() -> new ResourceNotFoundException("Policy", policyId));
            policy.setPatient(patient);
            patient.setPolicyOMS(policy);
        }

        return patientRepository.save(patient);
    }

    @Override
    public Page<Patient> findPatientByFIO(Pageable pageable, String fio) {
        return patientRepository.findPatientByFIO(pageable, fio);
    }

    @Override
    public Page<Patient> findPatientByDateOfBirth(Pageable pageable, LocalDate dateOfBirth) {
        return patientRepository.findPatientByDateOfBirth(pageable, dateOfBirth);
    }

    @Override
    public Page<Patient> findPatientBySex(Pageable pageable, SexType sexType) {
        return patientRepository.findPatientBySex(pageable, sexType);
    }

    @Override
    public Optional<Patient> findPatientByPhoneNumber(String phoneNumber) {
        return patientRepository.findPatientByPhoneNumber(phoneNumber);
    }

    @Override
    public Optional<Patient> findPatientByEmail(String email) {
        return patientRepository.findPatientByEmail(email);
    }

    @Override
    public Optional<Patient> findPatientBySNILS(String snils) {
        return patientRepository.findPatientBySNILS(snils);
    }

    @Override
    public Optional<Patient> findPatientByPolicyOMS(PolicyOMS policyOMS) {
        return patientRepository.findByPolicyOMS(policyOMS);
    }
}
