package com.medical.med.service;

import com.medical.med.DTO.request.CreatePatientRequest;
import com.medical.med.DTO.response.PatientResponse;
import com.medical.med.DTO.PolicyOMSDTO;
import com.medical.med.exeption.ResourceNotFoundException;
import com.medical.med.model.enums.SexType;
import com.medical.med.repository.PatientRepository;
import com.medical.med.repository.PolicyRepository;
import com.medical.med.service.impl.PatientServiceImpl;
import com.medical.med.service.impl.PolicyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
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
public class PolicyServiceTest {

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private PolicyService policyService;

    private PolicyOMSDTO testPolicyOMS;

    private CreatePatientRequest testPatient;

    @BeforeEach
    void setUp() {

        patientRepository.deleteAll();
        policyRepository.deleteAll();

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

        testPolicyOMS = PolicyOMSDTO.builder()
                .patientId(testPatient.getId())
                .dateAndTimeOfCreation(LocalDateTime.now())
                .singlePolicyNumber("1234567890123456").build();
    }

    @Test
    public void createPolicy_shouldSaveAndReturnPolicy_whenValidData() {

        PatientResponse savedPatient = patientService.createPatient(testPatient, null);

        testPolicyOMS.setPatientId(savedPatient.getId());

        PolicyOMSDTO savedPolicy = policyService.createPolicy(testPolicyOMS, savedPatient.getId());

        assertNotNull(savedPolicy);
        assertNotNull(savedPolicy.getId());
        assertEquals("1234567890123456", savedPolicy.getSinglePolicyNumber());
        assertEquals(savedPatient.getId(), savedPolicy.getPatientId());
    }

    @Test
    void createPolicy_shouldThrowException_whenPolicyNumberAlreadyExists() {

        PatientResponse savedPatient = patientService.createPatient(testPatient, null);
        testPolicyOMS.setPatientId(savedPatient.getId());
        policyService.createPolicy(testPolicyOMS, savedPatient.getId());

        CreatePatientRequest anotherPatient = CreatePatientRequest.builder()
                .surname("Петров")
                .name("Петр")
                .dateOfBirth(LocalDate.of(1990, 5, 10))
                .sex(SexType.MALE)
                .phoneNumber("+79998887766")
                .email("petr@test.com")
                .snils("98765432109")
                .build();
        PatientResponse savedAnother = patientService.createPatient(anotherPatient, null);

        PolicyOMSDTO duplicatePolicy = PolicyOMSDTO.builder()
                .patientId(savedAnother.getId())
                .dateAndTimeOfCreation(LocalDateTime.now())
                .singlePolicyNumber("1234567890123456")
                .build();

        Exception exception = assertThrows(RuntimeException.class, () -> {
            policyService.createPolicy(duplicatePolicy, savedPatient.getId());
        });

        assertTrue(exception.getMessage().contains("уже существует"));
    }

    @Test
    public void findPolicyBySinglePolicyNumber_shouldReturnPatient_whenNumberExists() {

        PatientResponse savedPatient = patientService.createPatient(testPatient, null);
        testPolicyOMS.setPatientId(savedPatient.getId());
        PolicyOMSDTO savedPolicy = policyService.createPolicy(testPolicyOMS, savedPatient.getId());

        Optional<PolicyOMSDTO> found = policyService.findPolicyBySinglePolicyNumber("1234567890123456");

        assertTrue(found.isPresent());
        assertEquals(savedPolicy.getId(), found.get().getId());
        assertEquals("1234567890123456", found.get().getSinglePolicyNumber());
    }

    @Test
    void findPolicyBySinglePolicyNumber_shouldReturnEmpty_whenNumberDoesNotExist() {

        Optional<PolicyOMSDTO> found = policyService.findPolicyBySinglePolicyNumber("9999999999999999");

        assertTrue(found.isEmpty());
    }

    @Test
    public void findPolicyById_shouldReturnPatient_whenIdExists() {

        PatientResponse savedPatient = patientService.createPatient(testPatient, null);
        testPolicyOMS.setPatientId(savedPatient.getId());
        PolicyOMSDTO savedPolicy = policyService.createPolicy(testPolicyOMS, savedPatient.getId());

        Optional<PolicyOMSDTO> found = policyService.findPolicyById(savedPolicy.getId());

        assertTrue(found.isPresent());
        assertEquals(savedPolicy.getId(), found.get().getId());
        assertEquals(savedPolicy.getSinglePolicyNumber(), found.get().getSinglePolicyNumber());
    }

    @Test
    void findPolicyById_shouldReturnEmpty_whenIdDoesNotExist() {
        assertThrows(ResourceNotFoundException.class, () -> {
            policyService.findPolicyById(999L);
        });
    }
}
