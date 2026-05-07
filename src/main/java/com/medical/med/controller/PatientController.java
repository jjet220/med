package com.medical.med.controller;

import com.medical.med.DTO.CreatePatientRequest;
import com.medical.med.DTO.PatientResponse;
import com.medical.med.annotation.validation.ValidPhone;
import com.medical.med.annotation.validation.ValidSinglePolicyNumber;
import com.medical.med.exeption.ResourceNotFoundException;
import com.medical.med.mapper.PatientMapper;
import com.medical.med.model.Patient;
import com.medical.med.model.PolicyOMS;
import com.medical.med.model.SexType;
import com.medical.med.service.PatientService;
import com.medical.med.service.PolicyService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/patients")
@Validated
@AllArgsConstructor
@Slf4j
public class PatientController {
    private final PatientService patientService;
    private final PolicyService policyService;
    private final PatientMapper patientMapper;

    @PutMapping("/update/{patientId}")
    public ResponseEntity<PatientResponse> updatePatient(@Valid @RequestBody CreatePatientRequest request,
                                                         @PathVariable Long patientId) {
        log.info("Обновление данных пациента с id: {}", patientId);

        Patient patient = patientService.findPatientById(patientId);
        patient = patientService.updatePatient(patientMapper.toEntity(request));

        log.info("Данные пациента с id: {}, обновленны", patient.getId());
        return ResponseEntity.ok(patientMapper.toResponse(patient));
    }

    @DeleteMapping("/deleted/{patientId}")
    public ResponseEntity<PatientResponse> deletedPatient(@PathVariable Long patineId) {
        log.info("Удаление пациента с id: {}", patineId);

        Patient patient = patientService.findPatientById(patineId);

        patientService.deletedPatient(patineId);

        log.info("Пациент с id: {}, удалён", patineId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<PatientResponse> getPatientById(@PathVariable Long patientId) {
        log.info("Нахождение пациента по id: {}", patientId);

        Patient patient = patientService.findPatientById(patientId);

        log.info("Пациент найден с id: {}", patient.getId());
        return ResponseEntity.ok(patientMapper.toResponse(patient));
    }

    @PostMapping
    public ResponseEntity<PatientResponse> createPatient(@Valid @RequestBody CreatePatientRequest request) {
        log.info("Создание нового пациента: {} {}", request.getSurname(), request.getName());

        Patient patient = patientMapper.toEntity(request);
        Patient created = patientService.createPatient(patient, request.getPolicyId());

        log.info("Пациен создан с id: {}", created.getId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(patientMapper.toResponse(created));
    }

    @GetMapping("/email")
    public ResponseEntity<PatientResponse> getPatientByEmail(@Email @RequestParam String email) {
        log.info("Нахождение пациента по эл.почте: {}", email);

        Patient patient = patientService.findPatientByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email", email));

        log.info("Пациент найден с эл.почтой: {}", patient.getEmail());
        return ResponseEntity.ok(patientMapper.toResponse(patient));
    }

    @GetMapping("/sex-type")
    public ResponseEntity<Page<PatientResponse>> getPatientBySexType(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @NotNull @RequestParam SexType sexType){
        log.info("Нахождение пациента(ов) по полу: {}", sexType);

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Patient> patients = patientService.findPatientBySex(pageRequest, sexType);
        Page<PatientResponse> response = patients.map(patientMapper::toResponse);

        log.info("Пациенты найдены по полу: {}", sexType);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/fio")
    public ResponseEntity<Page<PatientResponse>> getPatientByFIO(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,@NotBlank @RequestParam String fio){
        log.info("Нахождение пациента(ов) по ФИО: {}", fio);

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Patient> patients = patientService.findPatientByFIO(pageRequest, fio);
        Page<PatientResponse> response = patients.map(patientMapper::toResponse);

        log.info("Пациент(ы) найден(ы) по ФИО: {}", fio);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/date-of-birth")
    public ResponseEntity<Page<PatientResponse>> getPatientByDateOfBirth(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,@NotNull @RequestParam LocalDate dateOfBirth){
        log.info("Нахождение пациента(ов) по дате рождения: {}", dateOfBirth);

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Patient> patients = patientService.findPatientByDateOfBirth(pageRequest, dateOfBirth);
        Page<PatientResponse> response = patients.map(patientMapper::toResponse);

        log.info("Пациент(ы) найден(ы) по дате рождения: {}", dateOfBirth);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/snils")
    public ResponseEntity<PatientResponse> getPatientBySNILS(@RequestParam String snils) {
        log.info("Нахождение пациента по СНИЛСу: {}", snils);

        Patient patient = patientService.findPatientBySNILS(snils)
                .orElseThrow(() -> new ResourceNotFoundException("SNILS", snils));

        log.info("Пациент найден со СНИЛС: {}", patient.getSNILS());
        return ResponseEntity.ok(patientMapper.toResponse(patient));
    }

    @GetMapping("/phone")
    public ResponseEntity<PatientResponse> getPatientByPhoneNumber(@ValidPhone @RequestParam String phoneNumber) {
        log.info("Нахождение пациента по телефону: {}", phoneNumber);

        Patient patient = patientService.findPatientByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Phone", phoneNumber));

        log.info("Пациент найден с телефоном: {}", patient.getPhoneNumber());
        return ResponseEntity.ok(patientMapper.toResponse(patient));
    }

    @GetMapping("/policy")
    public ResponseEntity<PatientResponse> getPatientByPolicyOMS(@ValidSinglePolicyNumber @RequestParam String policyNumber) {
        log.info("Нахождение пациента по полису: {}", policyNumber);

        PolicyOMS policyOMS = policyService.findPolicyBySinglePolicyNumber(policyNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Policy", policyNumber));

        Patient patient = patientService.findPatientByPolicyOMS(policyOMS)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", policyOMS));

        log.info("Пациент найден с полисом: {}", patient.getPolicyOMS().getSinglePolicyNumber());
        return ResponseEntity.ok(patientMapper.toResponse(patient));
    }
}
