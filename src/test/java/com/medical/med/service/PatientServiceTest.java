package com.medical.med.service;

import com.medical.med.DTO.request.CreatePatientRequest;
import com.medical.med.DTO.response.AttachmentResponse;
import com.medical.med.DTO.response.PatientResponse;
import com.medical.med.DTO.response.PatientWithActiveAttachmentResponse;
import com.medical.med.DTO.PolicyOMSDTO;
import com.medical.med.exeption.ConflictException;
import com.medical.med.exeption.ResourceNotFoundException;
import com.medical.med.mapper.PatientMapper;
import com.medical.med.model.Attachment;
import com.medical.med.model.MedicalOrganization;
import com.medical.med.model.Patient;
import com.medical.med.model.enums.AttachmentType;
import com.medical.med.model.enums.SexType;
import com.medical.med.repository.AttachmentRepository;
import com.medical.med.repository.MedicalOrganizationRepository;
import com.medical.med.repository.PatientRepository;
import com.medical.med.service.impl.PatientServiceImpl;
import com.medical.med.service.impl.PolicyServiceImpl;
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
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
@Import({PatientServiceImpl.class, PolicyServiceImpl.class})
@ExtendWith(SpringExtension.class)
@Transactional
@Rollback
public class PatientServiceTest {

    @Autowired
    private PolicyService policyService;

    @Autowired
    private PatientMapper patientMapper;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientService patientService;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private MedicalOrganizationRepository medicalOrganizationRepository;

    private CreatePatientRequest testPatient;

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
    }

    @Test
    public void createPatient_shouldSaveAndReturnPatient_whenValidData() {
        PatientResponse saved = patientService.createPatient(testPatient, null);

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals("Иванов", saved.getSurname());
        assertEquals("Иван", saved.getName());
        assertEquals("ivan@test.com", saved.getEmail());
    }

    @Test
    void createPatient_shouldThrowConflictException_whenEmailAlreadyExists() {

        patientRepository.save(patientMapper.toEntity(testPatient));

        CreatePatientRequest duplicatePatient = new CreatePatientRequest();
        duplicatePatient.setSurname("Петров");
        duplicatePatient.setName("Петр");
        duplicatePatient.setDateOfBirth(LocalDate.of(1995, 5, 10));
        duplicatePatient.setSex(SexType.MALE);
        duplicatePatient.setPhoneNumber("+79998887766");
        duplicatePatient.setEmail("ivan@test.com");
        duplicatePatient.setSnils("98765432109");

        ConflictException exception = assertThrows(ConflictException.class,
                () -> patientService.createPatient(duplicatePatient, null));

        assertTrue(exception.getMessage().contains("email"));
        assertTrue(exception.getMessage().contains("ivan@test.com"));
    }

    @Test
    void createPatient_shouldThrowConflictException_whenPhoneNumberAlreadyExists() {

        patientRepository.save(patientMapper.toEntity(testPatient));

        CreatePatientRequest duplicatePatient = new CreatePatientRequest();
        duplicatePatient.setSurname("Петров");
        duplicatePatient.setName("Петр");
        duplicatePatient.setDateOfBirth(LocalDate.of(1995, 5, 10));
        duplicatePatient.setSex(SexType.MALE);
        duplicatePatient.setPhoneNumber("+79123547831");
        duplicatePatient.setEmail("petr@test.com");
        duplicatePatient.setSnils("98765432109");

        ConflictException exception = assertThrows(ConflictException.class,
                () -> patientService.createPatient(duplicatePatient, null));

        assertTrue(exception.getMessage().contains("phone"));
        assertTrue(exception.getMessage().contains("+79123547831"));
    }

    @Test
    void createPatient_shouldThrowConflictException_whenSnilsAlreadyExists() {

        patientRepository.save(patientMapper.toEntity(testPatient));

        CreatePatientRequest duplicatePatient = new CreatePatientRequest();
        duplicatePatient.setSurname("Петров");
        duplicatePatient.setName("Петр");
        duplicatePatient.setDateOfBirth(LocalDate.of(1995, 5, 10));
        duplicatePatient.setSex(SexType.MALE);
        duplicatePatient.setPhoneNumber("+79225549125");
        duplicatePatient.setEmail("petr@test.com");
        duplicatePatient.setSnils("12345678901");

        ConflictException exception = assertThrows(ConflictException.class,
                () -> patientService.createPatient(duplicatePatient, null));

        assertTrue(exception.getMessage().contains("snils"));
        assertTrue(exception.getMessage().contains("12345678901"));
    }

    @Test
    void findPatientById_shouldReturnPatient_whenIdExists() {

        Patient saved = patientRepository.save(patientMapper.toEntity(testPatient));

        PatientWithActiveAttachmentResponse found = patientService.findPatientById(saved.getId());

        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
        assertEquals(saved.getEmail(), found.getEmail());
    }

    @Test
    void findPatientByEmail_shouldReturnPatient_whenIdExists() {

        Patient saved = patientRepository.save(patientMapper.toEntity(testPatient));

        Optional<PatientWithActiveAttachmentResponse> found = patientService.findPatientByEmail("ivan@test.com");

        assertNotNull(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals(saved.getEmail(), found.get().getEmail());
    }

    @Test
    void findPatientByPhoneNumber_shouldReturnPatient_whenIdExists() {

        Patient saved = patientRepository.save(patientMapper.toEntity(testPatient));

        Optional<PatientWithActiveAttachmentResponse> found = patientService.findPatientByPhoneNumber("+79123547831");

        assertNotNull(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals(saved.getPhoneNumber(), found.get().getPhoneNumber());
    }

    @Test
    void findPatientBySNILS_shouldReturnPatient_whenIdExists() {

        Patient saved = patientRepository.save(patientMapper.toEntity(testPatient));

        Optional<PatientWithActiveAttachmentResponse> found = patientService.findPatientBySNILS("12345678901");

        assertNotNull(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals(saved.getSnils(), found.get().getSNILS());
    }


    @Test
    void findPatientByFIO_shouldReturnPatient_whenIdExists() {

        Patient saved = patientRepository.save(patientMapper.toEntity(testPatient));

        Pageable pageable = PageRequest.of(0, 20);
        String fullFIO = "Иванов Иван Иванович";

        Page<PatientWithActiveAttachmentResponse> foundPage = patientService.findPatientByFIO(pageable, fullFIO);

        assertNotNull(foundPage);
        assertFalse(foundPage.isEmpty());
        assertEquals(1, foundPage.getTotalElements());
        assertEquals(1, foundPage.getContent().size());

        PatientWithActiveAttachmentResponse found = foundPage.getContent().get(0);
        assertEquals(saved.getId(), found.getId());
        assertEquals(saved.getSurname(), found.getSurname());
        assertEquals(saved.getName(), found.getName());
        assertEquals(saved.getPatronymic(), found.getPatronymic());
    }

    @Test
    void findPatientBySexType_shouldReturnPatient_whenIdExists() {

        Patient saved = patientRepository.save(patientMapper.toEntity(testPatient));

        Pageable pageable = PageRequest.of(0, 20);
        SexType type = SexType.MALE;

        Page<PatientWithActiveAttachmentResponse> foundPage = patientService.findPatientBySex(pageable, type);

        assertNotNull(foundPage);
        assertFalse(foundPage.isEmpty());
        assertEquals(1, foundPage.getTotalElements());
        assertEquals(1, foundPage.getContent().size());

        PatientWithActiveAttachmentResponse found = foundPage.getContent().get(0);
        assertEquals(saved.getId(), found.getId());
        assertEquals(saved.getSex(), found.getSex());
    }

    @Test
    void findPatientByDateOfBirth_shouldReturnPatient_whenIdExists() {

        Patient saved = patientRepository.save(patientMapper.toEntity(testPatient));

        Pageable pageable = PageRequest.of(0, 20);
        LocalDate date = LocalDate.of(1999, 1, 23);

        Page<PatientWithActiveAttachmentResponse> foundPage = patientService.findPatientByDateOfBirth(pageable, date);

        assertNotNull(foundPage);
        assertFalse(foundPage.isEmpty());
        assertEquals(1, foundPage.getTotalElements());
        assertEquals(1, foundPage.getContent().size());

        PatientWithActiveAttachmentResponse found = foundPage.getContent().get(0);
        assertEquals(saved.getId(), found.getId());
        assertEquals(saved.getDateOfBirth(), found.getDateOfBirth());
    }

    @Test
    void findPatientByPolicy_shouldReturnPatient_whenIdExists() {
        Patient saved = patientRepository.save(patientMapper.toEntity(testPatient));

        PolicyOMSDTO policyDTO = PolicyOMSDTO.builder()
                .patientId(saved.getId())
                .singlePolicyNumber("1234567891234567")
                .dateAndTimeOfCreation(LocalDateTime.of(2000, 5, 12, 13, 30))
                .build();

        PolicyOMSDTO createdPolicy = policyService.createPolicy(policyDTO, saved.getId());

        saved.setPolicyOMS(patientMapper.toPolicyEntity(createdPolicy));
        patientRepository.save(saved);

        System.out.println("Created policy ID: " + createdPolicy.getId());

        Optional<PatientWithActiveAttachmentResponse> found = patientService.findPatientByPolicyOMS(createdPolicy);

        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
    }

    @Test
    void findPatientById_shouldThrowResourceNotFoundException_whenIdDoesNotExist() {

        Long nonExistentId = 999L;

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> patientService.findPatientById(nonExistentId));

        assertTrue(exception.getMessage().contains("Patient"));
        assertTrue(exception.getMessage().contains(String.valueOf(nonExistentId)));
    }

    @Test
    void findLastAttachment_shouldReturnPatient_whenLastAttachmentExist() {
        Patient saved = patientRepository.save(patientMapper.toEntity(testPatient));

        MedicalOrganization medicalOrganizationOne = MedicalOrganization.builder()
                .code("1233344")
                .name("Organ")
                .build();

        MedicalOrganization medicalOrganizationSecond = MedicalOrganization.builder()
                .code("6454454")
                .name("Medical")
                .build();

        medicalOrganizationOne = medicalOrganizationRepository.save(medicalOrganizationOne);
        medicalOrganizationSecond = medicalOrganizationRepository.save(medicalOrganizationSecond);

        Attachment attachmentOne = Attachment.builder()
                .patient(saved)
                .dateOfBegin(LocalDate.now().minusDays(100))
                .dateOfEnd(LocalDate.now().minusDays(1))
                .medicalOrganization(medicalOrganizationOne)
                .type(AttachmentType.OUTPATIENT)
                .build();

        Attachment attachmentSecond = Attachment.builder()
                .patient(saved)
                .dateOfBegin(LocalDate.now())
                .dateOfEnd(LocalDate.now().plusYears(1))
                .medicalOrganization(medicalOrganizationSecond)
                .type(AttachmentType.OUTPATIENT)
                .build();

        attachmentRepository.save(attachmentOne);
        attachmentRepository.save(attachmentSecond);

        medicalOrganizationOne.setAttachments(attachmentOne);
        medicalOrganizationSecond.setAttachments(attachmentSecond);

        medicalOrganizationRepository.save(medicalOrganizationOne);
        medicalOrganizationRepository.save(medicalOrganizationSecond);

        Optional<AttachmentResponse> lastAttachment =
                patientService.findLastAttachment(patientMapper.toResponse(saved));

        assertTrue(lastAttachment.isPresent());
        assertEquals(lastAttachment.get().getId(), attachmentSecond.getId());
    }
}
