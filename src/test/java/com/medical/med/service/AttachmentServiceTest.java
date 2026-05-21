package com.medical.med.service;

import com.medical.med.DTO.request.CreateAttachmentRequest;
import com.medical.med.DTO.request.CreateMedicalOrganizationRequest;
import com.medical.med.DTO.request.CreatePatientRequest;
import com.medical.med.DTO.response.AttachmentResponse;
import com.medical.med.mapper.AttachmentMapper;
import com.medical.med.mapper.MedicalOrganizationMapper;
import com.medical.med.mapper.PatientMapper;
import com.medical.med.model.Attachment;
import com.medical.med.model.MedicalOrganization;
import com.medical.med.model.Patient;
import com.medical.med.model.enums.AttachmentType;
import com.medical.med.model.enums.SexType;
import com.medical.med.repository.AttachmentRepository;
import com.medical.med.repository.MedicalOrganizationRepository;
import com.medical.med.repository.PatientRepository;
import com.medical.med.service.impl.AttachmentServiceImpl;
import com.medical.med.service.impl.MedicalOrganizationServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@Import({AttachmentServiceImpl.class, MedicalOrganizationServiceImpl.class})
@ExtendWith(SpringExtension.class)
@Transactional
@Rollback
public class AttachmentServiceTest {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private MedicalOrganizationRepository medicalOrganizationRepository;

    @Autowired
    private PatientMapper patientMapper;

    @Autowired
    private MedicalOrganizationMapper medicalOrganizationMapper;

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private AttachmentService attachmentService;

    CreatePatientRequest testPatient;

    CreateMedicalOrganizationRequest testOrganization;

    CreateAttachmentRequest testAttachment;

    @BeforeEach
    void setUp() {

        patientRepository.deleteAll();

        testPatient = CreatePatientRequest.builder()
                .surname("Иванов")
                .name("Иван")
                .patronymic("Иванович")
                .dateOfBirth(LocalDate.of(1999, 1, 23))
                .sex(SexType.MALE)
                .phoneNumber("+79123547831")
                .email("ivan@test.com")
                .snils("12345678901")
                .build();

        testOrganization = CreateMedicalOrganizationRequest.builder()
                .code("12345678")
                .name("Medical")
                .build();

        testAttachment = CreateAttachmentRequest.builder()
                .type(AttachmentType.OUTPATIENT)
                .moCode(testOrganization.getCode())
                .patientId(testPatient.getId())
                .dateOfBegin(LocalDate.now().minusYears(1))
                .dateOfEnd(LocalDate.now().plusYears(1))
                .build();
    }

    @Test
    public void createAttachment_shouldSaveAndReturnAttachment_whenValidData() {
        Patient patient = patientRepository.save(patientMapper.toEntity(testPatient));

        MedicalOrganization medicalOrganization = medicalOrganizationRepository.
                save(medicalOrganizationMapper.toEntity(testOrganization));

        CreateAttachmentRequest request = CreateAttachmentRequest.builder()
                .type(AttachmentType.OUTPATIENT)
                .moCode(medicalOrganization.getCode())
                .patientId(patient.getId())
                .dateOfBegin(LocalDate.now().minusYears(1))
                .dateOfEnd(LocalDate.now().plusYears(1))
                .build();

        AttachmentResponse saved = attachmentService.createAttachment(request, patient.getId());

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals(AttachmentType.OUTPATIENT, saved.getType());
        assertEquals(patient.getId(), saved.getPatient().getId());

        Optional<Attachment> savedFromDb = attachmentRepository.findById(saved.getId());
        Assertions.assertTrue(savedFromDb.isPresent());
        assertEquals(patient.getId(), savedFromDb.get().getPatient().getId());
    }

    @Test
    public void findAllAttachment_shouldReturnAttachmentList_whenValidData() {
        Patient patient = patientRepository.save(patientMapper.toEntity(testPatient));

        MedicalOrganization outpatientClinic = medicalOrganizationRepository
                .save(medicalOrganizationMapper.toEntity(testOrganization));

        MedicalOrganization dentalClinic = medicalOrganizationRepository
                .save(medicalOrganizationMapper.toEntity(
                        CreateMedicalOrganizationRequest.builder()
                                .code("87654321")
                                .name("Стоматологическая клиника")
                                .build()
                ));

        CreateAttachmentRequest outpatientRequest = CreateAttachmentRequest.builder()
                .type(AttachmentType.OUTPATIENT)
                .moCode(outpatientClinic.getCode())
                .dateOfBegin(LocalDate.now().minusYears(1))
                .dateOfEnd(LocalDate.now().plusYears(1))
                .build();

        CreateAttachmentRequest dentalRequest = CreateAttachmentRequest.builder()
                .type(AttachmentType.DENTAL)
                .moCode(dentalClinic.getCode())
                .dateOfBegin(LocalDate.now().minusMonths(6))
                .dateOfEnd(LocalDate.now().plusMonths(6))
                .build();

        AttachmentResponse firstSaved = attachmentService.createAttachment(outpatientRequest, patient.getId());
        AttachmentResponse secondSaved = attachmentService.createAttachment(dentalRequest, patient.getId());

        Pageable pageable = PageRequest.of(0, 20);
        Page<AttachmentResponse> allAttachments = attachmentService.findAllAttachment(pageable, patient.getId());

        assertNotNull(allAttachments);
        assertEquals(2, allAttachments.getTotalElements());
        assertEquals(2, allAttachments.getContent().size());

        List<Long> attachmentIds = allAttachments.getContent().stream()
                .map(AttachmentResponse::getId)
                .toList();

        Assertions.assertTrue(attachmentIds.contains(firstSaved.getId()));
        Assertions.assertTrue(attachmentIds.contains(secondSaved.getId()));

        List<AttachmentType> attachmentTypes = allAttachments.getContent().stream()
                .map(AttachmentResponse::getType)
                .toList();

        Assertions.assertTrue(attachmentTypes.contains(AttachmentType.OUTPATIENT));
        Assertions.assertTrue(attachmentTypes.contains(AttachmentType.DENTAL));
    }
}
