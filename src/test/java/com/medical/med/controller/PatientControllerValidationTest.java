package com.medical.med.controller;

import com.medical.med.DTO.CreatePatientRequest;
import com.medical.med.model.Patient;
import com.medical.med.model.PolicyOMS;
import com.medical.med.model.SexType;
import com.medical.med.repository.PatientRepository;
import com.medical.med.repository.PolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class PatientControllerValidationTest {

   @Autowired
   private MockMvc mockMvc;

   @Autowired
   private ObjectMapper objectMapper;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @BeforeEach
    void setUp() {
        policyRepository.deleteAll();
        patientRepository.deleteAll();
    }

    private CreatePatientRequest.CreatePatientRequestBuilder validRequestBuilder() {
        return CreatePatientRequest.builder()
                .surname("Иванов")
                .name("Иван")
                .patronymic("Иванович")
                .dateOfBirth(LocalDate.of(1999, 1, 23))
                .sex(SexType.MALE)
                .phoneNumber("+79123547831")
                .email("ivan@test.com")
                .SNILS("14860360769");
    }

    @Test
    void CreatePatient_shouldReturnCreated_whenRequestIsValid() throws Exception {

        CreatePatientRequest request = validRequestBuilder().build();

        MvcResult result = MockResultCreated(request);

        String responseBody = result.getResponse().getContentAsString();

        assertNotNull(responseBody);
        assertTrue(responseBody.contains("id"));
        assertTrue(responseBody.contains("Иванов"));
    }


    @Test
    void createPatient_shouldReturnBadRequest_whenSurnameIsNull() throws Exception {

        CreatePatientRequest request = validRequestBuilder()
                .surname(null)
                .build();

        MvcResult result = MockResultBadRequest(request);

        String responseBody = result.getResponse().getContentAsString();

        assertTrue(responseBody.contains("VALIDATION_SURNAME"));
        assertTrue(responseBody.contains("Фамилия обязательна"));
    }

    @Test
    void createPatient_shouldReturnBadRequest_whenNameIsNull() throws Exception {

        CreatePatientRequest request = validRequestBuilder()
                .name(null)
                .build();

        MvcResult result = MockResultBadRequest(request);

        String responseBody = result.getResponse().getContentAsString();

        assertTrue(responseBody.contains("VALIDATION_NAME"));
        assertTrue(responseBody.contains("Имя обязательно"));
    }

    @Test
    void createPatient_shouldReturnBadRequest_whenDateOfBirthIsNull() throws Exception {

        CreatePatientRequest request = validRequestBuilder()
                .dateOfBirth(null)
                .build();

        MvcResult result = MockResultBadRequest(request);

        String responseBody = result.getResponse().getContentAsString();

        assertTrue(responseBody.contains("VALIDATION_DATEOFBIRTH"));
        assertTrue(responseBody.contains("Дата рождения обязательна"));
    }

    @Test
    void createPatient_shouldReturnBadRequest_whenSexTypeIsNull() throws Exception {

        CreatePatientRequest request = validRequestBuilder()
                .sex(null)
                .build();

        MvcResult result = MockResultBadRequest(request);

        String responseBody = result.getResponse().getContentAsString();

        assertTrue(responseBody.contains("VALIDATION_SEX"));
        assertTrue(responseBody.contains("Пол обязательно"));
    }

    @Test
    void createPatient_shouldReturnBadRequest_whenSurnameIsBlank() throws Exception {

        CreatePatientRequest request = validRequestBuilder()
                .surname("")
                .build();

        MvcResult result = MockResultBadRequest(request);

        String responseBody = result.getResponse().getContentAsString();

        assertTrue(responseBody.contains("VALIDATION_SURNAME"));
    }

    @Test
    void createPatient_shouldReturnBadRequest_whenNameIsBlank() throws Exception {

        CreatePatientRequest request = validRequestBuilder()
                .name("")
                .build();

        MvcResult result = MockResultBadRequest(request);

        String responseBody = result.getResponse().getContentAsString();

        assertTrue(responseBody.contains("VALIDATION_NAME"));
    }

    @Test
    void createPatient_shouldReturnBadRequest_whenSnilsIsNotCorrect() throws Exception {

        CreatePatientRequest request = validRequestBuilder()
                .SNILS("33033356384")
                .build();

        MvcResult result = MockResultBadRequest(request);

        String responseBody = result.getResponse().getContentAsString();

        assertTrue(responseBody.contains("VALIDATION_SNILS"));
    }

    @Test
    void createPatient_shouldReturnBadRequest_whenEmailIsNotCorrect() throws Exception {

        CreatePatientRequest request = validRequestBuilder()
                .email("ivantestcom")
                .build();

        MvcResult result = MockResultBadRequest(request);

        String responseBody = result.getResponse().getContentAsString();

        assertTrue(responseBody.contains("VALIDATION_EMAIL"));
    }

    @Test
    void createPatient_shouldReturnBadRequest_whenPhoneNumberIsBlank() throws Exception {

        CreatePatientRequest request = validRequestBuilder()
                .phoneNumber(null)
                .build();

        MvcResult result = MockResultBadRequest(request);

        String responseBody = result.getResponse().getContentAsString();

        assertTrue(responseBody.contains("VALIDATION_PHONE"));
    }

    @Test
    void createPatient_shouldReturnBadRequest_whenPhoneNumberIsNotCorrect() throws Exception {

        CreatePatientRequest request = validRequestBuilder()
                .phoneNumber("384857571")
                .build();

        MvcResult result = MockResultBadRequest(request);

        String responseBody = result.getResponse().getContentAsString();

        assertTrue(responseBody.contains("VALIDATION_PHONE"));
    }

    @Test
    void getPatientById_shouldReturnPatient_whenRequestIsValid() throws Exception {

        CreatePatientRequest request = validRequestBuilder().build();

        MvcResult result = MockResultCreated(request);

        String createBody = result.getResponse().getContentAsString();
        Long patientId = objectMapper.readTree(createBody).get("id").asLong();

        MvcResult getResult = mockMvc.perform(get("/api/v1/patients/{patientId}", patientId)
                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                                .andReturn();

        String responseBody = getResult.getResponse().getContentAsString();

        assertNotNull(responseBody);
        assertTrue(responseBody.contains("id"));
        assertTrue(responseBody.contains(String.valueOf(patientId)));
        assertTrue(responseBody.contains("Иванов"));
    }

    @Test
    void getPatientByEmail_shouldReturnPatient_whenRequestIsValid() throws Exception {

        CreatePatientRequest request = validRequestBuilder().build();

        MvcResult result = MockResultCreated(request);

        String createBody = result.getResponse().getContentAsString();
        String email = objectMapper.readTree(createBody).get("email").asString();

        MvcResult getResult = mockMvc.perform(get("/api/v1/patients/find-by-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("email", email))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = getResult.getResponse().getContentAsString();

        assertNotNull(responseBody);
        assertTrue(responseBody.contains("id"));
        assertTrue(responseBody.contains("email"));
        assertTrue(responseBody.contains("Иванов"));
    }

    @Test
    void getPatientBySexType_shouldReturnPatient_whenRequestIsValid() throws Exception {

        CreatePatientRequest request = validRequestBuilder().build();

        MvcResult result = MockResultCreated(request);

        String createBody = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(createBody);
        String sexType = jsonNode.get("sex").asText();

        MvcResult getResult = mockMvc.perform(get("/api/v1/patients/find-by-sex-type")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("sexType", sexType)
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = getResult.getResponse().getContentAsString();

        assertNotNull(responseBody);
        assertTrue(responseBody.contains("Иванов"));
    }

    @Test
    void getPatientByFIO_shouldReturnPatient_whenRequestIsValid() throws Exception {

        CreatePatientRequest request = validRequestBuilder().build();

        MvcResult result = MockResultCreated(request);

        String createBody = result.getResponse().getContentAsString();
        JsonNode patientNode = objectMapper.readTree(createBody);
        String surname = patientNode.get("surname").asText();
        String name = patientNode.get("name").asText();
        String patronymic = patientNode.has("patronymic") ? patientNode.get("patronymic").asText() : "";
        String fio = surname + " " + name + (patronymic.isEmpty() ? "" : " " + patronymic);

        MvcResult getResult = mockMvc.perform(get("/api/v1/patients/find-by-fio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("fio", fio)
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = getResult.getResponse().getContentAsString();

        assertNotNull(responseBody);
        assertTrue(responseBody.contains("Иванов"));
    }

    @Test
    void getPatientByDateOfBirth_shouldReturnPatient_whenRequestIsValid() throws Exception {

        CreatePatientRequest request = validRequestBuilder().build();

        MvcResult result = MockResultCreated(request);

        String createBody = result.getResponse().getContentAsString();
        String date = objectMapper.readTree(createBody).get("dateOfBirth").asString();

        MvcResult getResult = mockMvc.perform(get("/api/v1/patients/find-by-date-of-birth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("dateOfBirth", date)
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = getResult.getResponse().getContentAsString();

        assertNotNull(responseBody);
        assertTrue(responseBody.contains("Иванов"));
    }

    @Test
    void getPatientBySnils_shouldReturnPatient_whenRequestIsValid() throws Exception {

        CreatePatientRequest request = validRequestBuilder().build();

        MvcResult result = MockResultCreated(request);

        String createBody = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(createBody);
        String snils = jsonNode.get("SNILS").asText();

        assertNotNull(snils);
        assertFalse(snils.isEmpty());

        MvcResult getResult = mockMvc.perform(get("/api/v1/patients/find-by-snils")
                        .param("snils", snils))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = getResult.getResponse().getContentAsString();
        assertNotNull(responseBody);
        assertTrue(responseBody.contains("Иванов"));
        assertTrue(responseBody.contains(snils));
    }

    @Test
    void getPatientByPhoneNumber_shouldReturnPatient_whenRequestIsValid() throws Exception {

        CreatePatientRequest request = validRequestBuilder().build();

        MvcResult result = MockResultCreated(request);

        String createBody = result.getResponse().getContentAsString();
        String phoneNumber = objectMapper.readTree(createBody).get("phoneNumber").asString();

        MvcResult getResult = mockMvc.perform(get("/api/v1/patients/fine-by-phone-number")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("phoneNumber", phoneNumber))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = getResult.getResponse().getContentAsString();
        assertNotNull(responseBody);
        assertTrue(responseBody.contains("Иванов"));
        assertTrue(responseBody.contains(phoneNumber));
    }

    @Test
    void getPatientByPolicyOMS_shouldReturnPatient_whenRequestIsValid() throws Exception {

        CreatePatientRequest request = validRequestBuilder().build();

        MvcResult result = MockResultCreated(request);

        String createBody = result.getResponse().getContentAsString();
        Long patientId = objectMapper.readTree(createBody).get("id").asLong();

        Patient patient = patientRepository.findPatientById(patientId)
                .orElseThrow(() -> new AssertionError("Patient not found with id: " + patientId));

        PolicyOMS policy = PolicyOMS.builder()
                .patient(patient)
                .singlePolicyNumber("1234567890123456")
                .dateAndTimeOfCreation(LocalDateTime.now())
                .build();

        PolicyOMS savedPolicy = policyRepository.save(policy);

        patient.setPolicyOMS(savedPolicy);
        patientRepository.save(patient);
        assertNotNull(savedPolicy.getId());

        MvcResult getResult = mockMvc.perform(get("/api/v1/patients/find-by-policy-number")
                        .param("policyNumber", "1234567890123456"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = getResult.getResponse().getContentAsString();
        assertNotNull(responseBody);
        assertTrue(responseBody.contains("Иванов"));
        assertTrue(responseBody.contains("1234567890123456"));
    }

    MvcResult MockResultCreated(CreatePatientRequest request) throws Exception {
        return mockMvc.perform(post("/api/v1/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();
    }

    MvcResult MockResultBadRequest(CreatePatientRequest request) throws Exception {
        return mockMvc.perform(post("/api/v1/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}
