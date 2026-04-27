package com.medical.med.service;

import com.medical.med.model.Patient;
import com.medical.med.model.PolicyOMS;
import com.medical.med.model.SexType;
import com.medical.med.repository.PatientRepository;
import com.medical.med.service.impl.PatientServiceImpl;
import com.medical.med.service.impl.PolicyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import({PatientServiceImpl.class, PolicyServiceImpl.class})
@ExtendWith(SpringExtension.class)
public class PolicyServiceTest {

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PolicyService policyService;

    private PolicyOMS testPolicyOMS;

    private Patient testPatient;

    @BeforeEach
    void setUp() {
        testPatient = Patient.builder()
                .surname("Иванов")
                .name("Иван")
                .patronymic("Иванович")
                .dateOfBirth(LocalDate.of(1999, 1, 23))
                .sex(SexType.MALE)
                .phoneNumber("+79123547831")
                .email("ivan@test.com")
                .SNILS("12345678901")
                .build();

        testPolicyOMS = PolicyOMS.builder()
                .patient(testPatient)
                .dateAndTimeOfCreation(LocalDateTime.now())
                .singlePolicyNumber("1234567890123456").build();
    }

    @Test
    public void createPolicy_shouldSaveAndReturnPolicy_whenValidData() {

        Patient savedPatient = patientRepository.save(testPatient);
        testPolicyOMS.setPatient(savedPatient);

        PolicyOMS savedPolicy = policyService.createPolicy(testPolicyOMS);

        assertNotNull(savedPolicy);
        assertNotNull(savedPolicy.getId());
        assertEquals("1234567890123456", savedPolicy.getSinglePolicyNumber());
        assertEquals(savedPatient.getId(), savedPolicy.getPatient().getId());
    }

    @Test
    void createPolicy_shouldThrowException_whenPolicyNumberAlreadyExists() {

        Patient savedPatient = patientRepository.save(testPatient);
        testPolicyOMS.setPatient(savedPatient);
        policyService.createPolicy(testPolicyOMS);

        Patient anotherPatient = Patient.builder()
                .surname("Петров")
                .name("Петр")
                .dateOfBirth(LocalDate.of(1990, 5, 10))
                .sex(SexType.MALE)
                .phoneNumber("+79998887766")
                .email("petr@test.com")
                .SNILS("98765432109")
                .build();
        Patient savedAnother = patientRepository.save(anotherPatient);

        PolicyOMS duplicatePolicy = PolicyOMS.builder()
                .patient(savedAnother)
                .dateAndTimeOfCreation(LocalDateTime.now())
                .singlePolicyNumber("1234567890123456")
                .build();

        Exception exception = assertThrows(RuntimeException.class, () -> {
            policyService.createPolicy(duplicatePolicy);
        });

        assertTrue(exception.getMessage().contains("уже существует"));
    }

    @Test
    public void findPolicyBySinglePolicyNumber_shouldReturnPatient_whenNumberExists() {

        Patient savedPatient = patientRepository.save(testPatient);
        testPolicyOMS.setPatient(savedPatient);
        PolicyOMS savedPolicy = policyService.createPolicy(testPolicyOMS);

        Optional<PolicyOMS> found = policyService.findPolicyBySinglePolicyNumber("1234567890123456");

        assertTrue(found.isPresent());
        assertEquals(savedPolicy.getId(), found.get().getId());
        assertEquals("1234567890123456", found.get().getSinglePolicyNumber());
    }

    @Test
    void findPolicyBySinglePolicyNumber_shouldReturnEmpty_whenNumberDoesNotExist() {

        Optional<PolicyOMS> found = policyService.findPolicyBySinglePolicyNumber("9999999999999999");

        assertTrue(found.isEmpty());
    }

    @Test
    public void findPolicyById_shouldReturnPatient_whenIdExists() {

        Patient savedPatient = patientRepository.save(testPatient);
        testPolicyOMS.setPatient(savedPatient);
        PolicyOMS savedPolicy = policyService.createPolicy(testPolicyOMS);

        Optional<PolicyOMS> found = policyService.findPolicyById(savedPolicy.getId());

        assertTrue(found.isPresent());
        assertEquals(savedPolicy.getId(), found.get().getId());
        assertEquals(savedPolicy.getSinglePolicyNumber(), found.get().getSinglePolicyNumber());
    }

    @Test
    void findPolicyById_shouldReturnEmpty_whenIdDoesNotExist() {

        Optional<PolicyOMS> found = policyService.findPolicyById(999L);

        assertTrue(found.isEmpty());
    }
}
