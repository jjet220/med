package com.medical.med.service.impl;

import com.medical.med.exeption.ConflictException;
import com.medical.med.exeption.ResourceNotFoundException;
import com.medical.med.model.Patient;
import com.medical.med.model.PolicyOMS;
import com.medical.med.model.SexType;
import com.medical.med.repository.PatientRepository;
import com.medical.med.repository.PolicyRepository;
import com.medical.med.service.PatientService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final PolicyRepository policyRepository;

    @Override
    public Patient updatePatient(Patient patient) {
        log.debug("Обнволение информации пациента: {}", patient);
        return patientRepository.save(patient);
    }

    @Override
    public void deletedPatient(Long patientId) {
        Patient patient = patientRepository.findPatientById(patientId)
                .orElseThrow(() -> {
                    log.error("Пациент не найден с id: {}", patientId);
                    return new ResourceNotFoundException("Patient", patientId);
                    });

        policyRepository.delete(policyRepository.findById(patient.getPolicyOMS().getId())
                .orElseThrow(() -> {
                    log.error("Полис не найден у пациента: {}", patient);
                    return new ResourceNotFoundException("Patient", patientId);
                }));

        patientRepository.delete(patient);

        patientRepository.flush();
    }

    @Override
    public Patient findPatientById(Long patientId) {
        return patientRepository.findPatientById(patientId)
                .orElseThrow(() -> {
                    log.error("Пациент не найден с id: {}", patientId);
                    return new ResourceNotFoundException("Patient", patientId);
                });
    }

    @Override
    public Patient createPatient(Patient patient, Long policyId) {
        log.debug("Создание пациента: {} {}, полис ID: {}",
                patient.getSurname(), patient.getName(), policyId);

        if (patient.getEmail() != null
                && patientRepository.existsByEmail(patient.getEmail())) {
            log.warn("Попытка создать пациента с существующим email: {}", patient.getEmail());
            throw new ConflictException(
                    "email",
                    patient.getEmail()
                    );
        }

        if (patient.getPhoneNumber() != null
                && patientRepository.existsByPhoneNumber(patient.getPhoneNumber())) {
            log.warn("Попытка создать пациента с существующим телефоном: {}", patient.getPhoneNumber());
            throw new ConflictException(
                    "phone",
                    patient.getPhoneNumber()
            );

        }

        if (patient.getSNILS() != null && patientRepository.existsBySNILS(patient.getSNILS())) {
            log.warn("Попытка создать пациента с существующим SNILS: {}", patient.getSNILS());
            throw new ConflictException(
                    "snils",
                    patient.getSNILS()
            );

        }

        if (policyId != null) {
            PolicyOMS policy = policyRepository.findById(policyId)
                    .orElseThrow(() -> {
                        log.error("Полис не найден с id: {}", policyId);
                        return new ResourceNotFoundException("Policy", policyId);
                    });
            policy.setPatient(patient);
            patient.setPolicyOMS(policy);
        }

        log.info("Создан новый пациент с id: {}", patient.getId());
        return patientRepository.save(patient);
    }

    @Override
    public Page<Patient> findPatientByFIO(Pageable pageable, String fio) {
        log.debug("Поиск пациента по ФИО: {}", fio);
        return patientRepository.findPatientByFIO(pageable, fio);
    }

    @Override
    public Page<Patient> findPatientByDateOfBirth(Pageable pageable, LocalDate dateOfBirth) {
        log.debug("Поиск пациента по дате рождения: {}", dateOfBirth);
        return patientRepository.findPatientByDateOfBirth(pageable, dateOfBirth);
    }

    @Override
    public Page<Patient> findPatientBySex(Pageable pageable, SexType sexType) {
        log.debug("Поиск пациента по полу: {}", sexType);
        return patientRepository.findPatientBySex(pageable, sexType);
    }

    @Override
    public Optional<Patient> findPatientByPhoneNumber(String phoneNumber) {
        log.debug("Поиск пациента по номеру телефона: {}", phoneNumber);
        return patientRepository.findPatientByPhoneNumber(phoneNumber);
    }

    @Override
    public Optional<Patient> findPatientByEmail(String email) {
        log.debug("Поиск пациента по электронгой почте: {}", email);
        return patientRepository.findPatientByEmail(email);
    }

    @Override
    public Optional<Patient> findPatientBySNILS(String snils) {
        log.debug("Поиск пациента по СНИЛСу: {}", snils);
        return patientRepository.findPatientBySNILS(snils);
    }

    @Override
    public Optional<Patient> findPatientByPolicyOMS(PolicyOMS policyOMS) {
        log.debug("Поиск пациента по полису: {}", policyOMS);
        return patientRepository.findByPolicyOMS(policyOMS);
    }
}
