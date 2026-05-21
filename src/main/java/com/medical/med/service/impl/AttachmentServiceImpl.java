package com.medical.med.service.impl;

import com.medical.med.DTO.response.AttachmentResponse;
import com.medical.med.DTO.request.CreateAttachmentRequest;
import com.medical.med.DTO.response.PatientResponse;
import com.medical.med.exeption.BusinessException;
import com.medical.med.exeption.ResourceNotFoundException;
import com.medical.med.mapper.AttachmentMapper;
import com.medical.med.mapper.PatientMapper;
import com.medical.med.model.Attachment;
import com.medical.med.model.MedicalOrganization;
import com.medical.med.model.Patient;
import com.medical.med.repository.AttachmentRepository;
import com.medical.med.repository.MedicalOrganizationRepository;
import com.medical.med.repository.PatientRepository;
import com.medical.med.service.AttachmentService;
import com.medical.med.service.EmailNotificationService;
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
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final PatientService patientService;
    private final MedicalOrganizationRepository medicalOrganizationRepository;
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final AttachmentMapper attachmentMapper;
    private final EmailNotificationService emailNotificationService;

    @Override
    public AttachmentResponse createAttachment(CreateAttachmentRequest request, Long patientId) {
        log.debug("Создание прикрепления для пациента: {}, МО: {}",
                patientId, request.getMoCode());

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> {
                    log.error("Пациент не найден с id: {}", patientId);
                    return new ResourceNotFoundException("Patient", patientId);
                });

        MedicalOrganization organization = medicalOrganizationRepository
                .findByCode(request.getMoCode())
                .orElseThrow(() -> {
                    log.error("МО с кодом {} не найдена", request.getMoCode());
                    return new BusinessException("Медицинская организация с кодом " + request.getMoCode() + " не существует");
                });

        PatientResponse patientResponse = patientMapper.toResponse(patient);

        Optional<AttachmentResponse> lastAttachmentOptResponse = patientService.findLastAttachment(patientResponse);

        if (lastAttachmentOptResponse.isPresent()) {
            AttachmentResponse lastAttachmentResponse  = lastAttachmentOptResponse.get();
            Attachment lastAttachment = attachmentRepository.findById(lastAttachmentResponse.getId())
                    .orElseThrow(() -> new BusinessException("Прикрепление не найдено"));

            LocalDate newEndDate = request.getDateOfBegin().minusDays(1);
            lastAttachment.setDateOfEnd(newEndDate);
            attachmentRepository.save(lastAttachment);
            log.debug("Обновлено предыдущее прикрепление id: {}, дата окончания: {}",
                    lastAttachment.getId(), newEndDate);
        }

        Attachment attachment = Attachment.builder()
                .type(request.getType())
                .dateOfBegin(request.getDateOfBegin())
                .dateOfEnd(request.getDateOfEnd())
                .patient(patient)
                .medicalOrganization(organization)
                .build();

        Attachment savedAttachment = attachmentRepository.save(attachment);
        log.info("Создано новое прикрепление с id: {}", savedAttachment.getId());

        String subject = "Новое прикрепление";
        String text = String.format("""
                        Уважаемый %s %s %s, для вас зарегистрировано новое прикрепление\
                         к медицинской организации.
                        Тип прикрепления: %s\s
                        Медицинская организация: %s
                        Дата регистрации: %s
                        Если вы обнаружили неточность в данных, просьба обратиться в вашу поликлинику.
                        """,
                patient.getSurname(),
                patient.getName(),
                patient.getPatronymic() != null ? patient.getPatronymic() : "",
                savedAttachment.getType().toString(),
                savedAttachment.getMedicalOrganization().getName(),
                LocalDate.now());

        emailNotificationService.sendNotification(patientResponse.getEmail(), subject, text);

        return attachmentMapper.toResponse(savedAttachment);
    }

//    @Override
//    public Optional<AttachmentResponse> findAttachmentById(Long attachmentId) {
//        log.debug("Поиск прикрепления по id: {}", attachmentId);
//
//        return attachmentRepository.findById(attachmentId)
//                .map(attachmentMapper::toResponse)
//                .or(() -> {
//                    log.error("Прикрепление не найдено с id: {}", attachmentId);
//                    return Optional.empty();
//                });
//    }

    @Override
    public Page<AttachmentResponse> findAllAttachment(Pageable pageable, Long patientId) {
        log.debug("Поиск всех прикреплений пациента с id: {}", patientId);

        patientService.findPatientById(patientId);

        return attachmentRepository.findByPatientId(pageable, patientId)
                .map(attachmentMapper::toResponse);
    }
}
