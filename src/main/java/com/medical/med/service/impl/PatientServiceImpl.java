package com.medical.med.service.impl;

import com.medical.med.DTO.*;
import com.medical.med.DTO.request.CreatePatientRequest;
import com.medical.med.DTO.response.AttachmentResponse;
import com.medical.med.DTO.response.PatientResponse;
import com.medical.med.DTO.response.PatientWithActiveAttachmentResponse;
import com.medical.med.exeption.ConflictException;
import com.medical.med.exeption.ResourceNotFoundException;
import com.medical.med.mapper.AttachmentMapper;
import com.medical.med.mapper.PatientMapper;
import com.medical.med.model.Attachment;
import com.medical.med.model.Patient;
import com.medical.med.model.PolicyOMS;
import com.medical.med.model.enums.SexType;
import com.medical.med.repository.AttachmentRepository;
import com.medical.med.repository.PatientRepository;
import com.medical.med.repository.PolicyRepository;
import com.medical.med.service.PatientService;
import com.medical.med.service.PolicyService;
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
    private final PolicyService policyService;
    private final PatientMapper patientMapper;
    private final AttachmentMapper attachmentMapper;
    private final AttachmentRepository attachmentRepository;

    @Override
    public PatientResponse updatePatient(CreatePatientRequest patient, Long patientId) {
        log.debug("Обнволение информации пациента: {}", patientId);

        Patient existingPatient = patientRepository.findById(patientId).orElseThrow(
                () -> {
                    log.error("Пациент не найден с id: {}", patientId);
                    return new ResourceNotFoundException("Patient", patientId);
                }
        );

        Patient updated = patientRepository.save(existingPatient);
        return patientMapper.toResponse(updated);
    }

    @Override
    public void deletedPatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
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
    public PatientWithActiveAttachmentResponse findPatientById(Long patientId) {

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> {
                    log.error("Пациент не найден с id: {}", patientId);
                    return new ResourceNotFoundException("Patient", patientId);
                });

        return patientMapper.toActiveAttachmentResponse(patient);
    }

    @Override
    public PatientResponse createPatient(CreatePatientRequest request, Long policyId) {
        log.debug("Создание пациента: {} {}, полис ID: {}",
                request.getSurname(), request.getName(), policyId);

        if (request.getEmail() != null
                && patientRepository.existsByEmail(request.getEmail())) {
            log.warn("Попытка создать пациента с существующим email: {}", request.getEmail());
            throw new ConflictException(
                    "email",
                    request.getEmail()
                    );
        }

        if (request.getPhoneNumber() != null
                && patientRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            log.warn("Попытка создать пациента с существующим телефоном: {}", request.getPhoneNumber());
            throw new ConflictException(
                    "phone",
                    request.getPhoneNumber()
            );

        }

        if (request.getSnils() != null && patientRepository.existsBySnils(request.getSnils())) {
            log.warn("Попытка создать пациента с существующим SNILS: {}", request.getSnils());
            throw new ConflictException(
                    "snils",
                    request.getSnils()
            );

        }

        Patient patient = patientMapper.toEntity(request);

        if (policyId != null) {
            PolicyOMSDTO policyDTO = policyService.findPolicyById(policyId)
                    .orElseThrow(() -> new ResourceNotFoundException("Policy", policyId));

            PolicyOMS policyEntity = patientMapper.toPolicyEntity(policyDTO);

            policyEntity.setPatient(patient);
            patient.setPolicyOMS(policyEntity);
        }

        Patient savedPatient = patientRepository.save(patient);

        log.info("Создан новый пациент с id: {}", savedPatient.getId());
        return patientMapper.toResponse(savedPatient);
    }

    @Override
    public Page<PatientWithActiveAttachmentResponse> findPatientByFIO(Pageable pageable, String fio) {
        log.debug("Поиск пациента по ФИО: {}", fio);

        Page<Patient> patientPage = patientRepository.findPatientByFIO(pageable, fio);

        return patientPage.map(patientMapper::toActiveAttachmentResponse);
    }

    @Override
    public Page<PatientWithActiveAttachmentResponse> findPatientByDateOfBirth(Pageable pageable, LocalDate dateOfBirth) {
        log.debug("Поиск пациента по дате рождения: {}", dateOfBirth);

        Page<Patient> patientPage = patientRepository.findPatientByDateOfBirth(pageable, dateOfBirth);

        return patientPage.map(patientMapper::toActiveAttachmentResponse);
    }

    @Override
    public Page<PatientWithActiveAttachmentResponse> findPatientBySex(Pageable pageable, SexType sexType) {
        log.debug("Поиск пациента по полу: {}", sexType);

        Page<Patient> patientPage = patientRepository.findPatientBySex(pageable, sexType);

        return patientPage.map(patientMapper::toActiveAttachmentResponse);
    }

    @Override
    public Optional<PatientWithActiveAttachmentResponse> findPatientByPhoneNumber(String phoneNumber) {
        log.debug("Поиск пациента по номеру телефона: {}", phoneNumber);

        Optional<Patient> patient = patientRepository.findPatientByPhoneNumber(phoneNumber);

        return patient.map(patientMapper::toActiveAttachmentResponse);
    }

    @Override
    public Optional<PatientWithActiveAttachmentResponse> findPatientByEmail(String email) {
        log.debug("Поиск пациента по электронгой почте: {}", email);

        Optional<Patient> patient = patientRepository.findPatientByEmail(email);

        return patient.map(patientMapper::toActiveAttachmentResponse);
    }

    @Override
    public Optional<PatientWithActiveAttachmentResponse> findPatientBySNILS(String snils) {
        log.debug("Поиск пациента по СНИЛСу: {}", snils);

        Optional<Patient> patient = patientRepository.findPatientBySnils(snils);

        return patient.map(patientMapper::toActiveAttachmentResponse);
    }

    @Override
    public Optional<PatientWithActiveAttachmentResponse> findPatientByPolicyOMS(PolicyOMSDTO policyOMS) {
        log.debug("Поиск пациента по полису: {}", policyOMS);

        if (policyOMS == null || policyOMS.getId() == null) {
            log.warn("PolicyOMS или его ID равен null");
            return Optional.empty();
        }

        return policyRepository.findById(policyOMS.getId())
                .map(PolicyOMS::getPatient)
                .map(patientMapper::toActiveAttachmentResponse);
    }

    @Override
    public Optional<AttachmentResponse> findLastAttachment(PatientResponse patientResponse) {
        log.debug("Поиск последнего прикрепления пациента: {}", patientResponse);

        Patient patient = patientMapper.toEntity(patientResponse);

        Optional<Attachment> attachment = attachmentRepository.
                findTopByPatientIdOrderByDateOfBeginDesc(patient.getId());

        return attachment.map(attachmentMapper::toResponse);
    }
}
