package com.medical.med.service.impl;

import com.medical.med.DTO.request.CreateIdentityDocumentRequest;
import com.medical.med.DTO.response.IdentityDocumentResponse;
import com.medical.med.exeption.BusinessException;
import com.medical.med.exeption.ResourceNotFoundException;
import com.medical.med.mapper.IdentityDocumentMapper;
import com.medical.med.model.IdentityDocument;
import com.medical.med.model.Patient;
import com.medical.med.model.enums.DocumentType;
import com.medical.med.repository.IdentityDocumentRepository;
import com.medical.med.repository.PatientRepository;
import com.medical.med.service.IdentityDocumentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class IdentityDocumentServiceImpl implements IdentityDocumentService {

    private final IdentityDocumentRepository identityDocumentRepository;
    private final PatientRepository patientRepository;
    private final IdentityDocumentMapper identityDocumentMapper;

    @Override
    public IdentityDocumentResponse createIdentityDocument(CreateIdentityDocumentRequest request, Long patientId) {
        log.debug("Создание ДУЛ: {}, у пациента с id: {}", request, patientId);

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> {
                    log.error("Пациент не найден с id: {}", patientId);
                    return new ResourceNotFoundException("Patient", patientId);
                });

        if (Period.between(patient.getDateOfBirth(), LocalDate.now()).getYears() < 14
                && request.getType().getValue() == 1) {
            log.warn("Попытка создать ДУЛ пациента не достигшенго 14 лет: {}", patient.getId());
            throw new BusinessException(
                    "age_restriction",
                    "Паспорт можно оформить только с 14 лет. Возраст пациента: " +
                            Period.between(patient.getDateOfBirth(), LocalDate.now()).getYears() + " лет",
                    HttpStatus.CONFLICT
            );
        }

        List<IdentityDocument> existingDocuments = identityDocumentRepository.findByPatientId(patient.getId());

        for (IdentityDocument existingDoc : existingDocuments) {
            if (request.getType() == DocumentType.PASSPORT && existingDoc.getType() == DocumentType.PASSPORT) {
                if (existingDoc.getIssuedAt().isAfter(request.getIssuedAt())) {
                    throw new BusinessException(
                            "outdated_document",
                            String.format("У пациента уже есть более актуальный паспорт от %s. " +
                                            "Нельзя добавить паспорт от %s",
                                    existingDoc.getIssuedAt(), request.getIssuedAt()),
                            HttpStatus.CONFLICT
                    );
                }

                if (existingDoc.getIssuedAt().equals(request.getIssuedAt())) {
                    throw new BusinessException(
                            "duplicate_document",
                            "Паспорт с такой датой выдачи уже существует",
                            HttpStatus.CONFLICT
                    );
                }

                if (existingDoc.getIssuedAt().isBefore(request.getIssuedAt())) {
                    identityDocumentRepository.delete(existingDoc);
                }
            }

            if (request.getType() == DocumentType.CERTIFICATE_OF_BIRTH &&
                    existingDoc.getType() == DocumentType.CERTIFICATE_OF_BIRTH) {
                if (existingDoc.getIssuedAt().isAfter(request.getIssuedAt())) {
                    throw new BusinessException(
                            "outdated_document",
                            "У пациента уже есть более актуальное свидетельство о рождении",
                            HttpStatus.CONFLICT
                    );
                }

                if (existingDoc.getIssuedAt().equals(request.getIssuedAt())) {
                    throw new BusinessException(
                            "duplicate_document",
                            "Свидетельство о рождении с такой датой выдачи уже существует",
                            HttpStatus.CONFLICT
                    );
                }
            }
        }

        IdentityDocument identityDocument = IdentityDocument.builder()
                .type(request.getType())
                .series(request.getSeries())
                .issuedBy(request.getIssuedBy())
                .issuedAt(request.getIssuedAt())
                .number(request.getNumber())
                .patient(patient)
                .build();

        IdentityDocument saved = identityDocumentRepository.save(identityDocument);
        log.info("Создан новый ДУЛ с id: {}", saved.getId());

        return identityDocumentMapper.toResponse(saved);
    }

    @Override
    public List<IdentityDocumentResponse> getIdentityDocuments(Long patientId) {
        log.debug("Поиск ДУЛ у пациента с id: {}", patientId);

        List<IdentityDocument> response = identityDocumentRepository.findByPatientId(patientId);

        return response.stream()
                .map(identityDocumentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteIdentityDocument(Long patientId, Long documentId) {
        log.debug("Удаление ДУЛ пациента с id: {}, документа с id: {}", patientId, documentId);
        patientRepository.findById(patientId);

        IdentityDocument identityDocument = identityDocumentRepository.findById(documentId).orElseThrow(() -> {
                    log.error("У данного пациента нет ДУЛ с таким идентификатором: {}", documentId);
                    return new ResourceNotFoundException("IdentityDocument", documentId);
                }
        );
        identityDocumentRepository.delete(identityDocument);
        log.debug("Документ удалён: {}", identityDocument);
    }
}
