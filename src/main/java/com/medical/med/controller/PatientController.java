package com.medical.med.controller;

import com.medical.med.DTO.CreatePatientRequest;
import com.medical.med.DTO.PatientResponse;
import com.medical.med.annotation.validation.ValidPhone;
import com.medical.med.annotation.validation.ValidSinglePolicyNumber;
import com.medical.med.exeption.ResourceNotFoundException;
import com.medical.med.model.Patient;
import com.medical.med.model.PolicyOMS;
import com.medical.med.model.SexType;
import com.medical.med.service.PatientService;
import com.medical.med.service.PolicyService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/patients")
@Validated
public class PatientController {
    private final PatientService patientService;
    private final PolicyService policyService;

    public PatientController(PatientService patientService, PolicyService policyService) {
        this.patientService = patientService;
        this.policyService = policyService;
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<PatientResponse> getPatientById(@PathVariable Long patientId) {
        Patient patient = patientService.findPatientById(patientId);

        return ResponseEntity.ok(PatientResponse.fromEntity(patient));
    }

    @PostMapping
    public ResponseEntity<PatientResponse> createPatient(@Valid @RequestBody CreatePatientRequest request) {

        Patient patient = request.toEntity();
        Patient created = patientService.createPatient(patient, request.getPolicyId());
        PatientResponse response = PatientResponse.fromEntity(created);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/find-by-email")
    public ResponseEntity<PatientResponse> getPatientByEmail(@Email @RequestParam String email) {

        Patient patient = patientService.findPatientByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email", email));

        return ResponseEntity.ok(PatientResponse.fromEntity(patient));
    }

    @GetMapping("/find-by-sex-type")
    public ResponseEntity<Page<PatientResponse>> getPatientBySexType(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,@NotNull @RequestParam SexType sexType){
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Patient> patients = patientService.findPatientBySex(pageRequest, sexType);
        Page<PatientResponse> response = patients.map(PatientResponse::fromEntity);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/find-by-fio")
    public ResponseEntity<Page<PatientResponse>> getPatientByFIO(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,@NotBlank @RequestParam String fio){
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Patient> patients = patientService.findPatientByFIO(pageRequest, fio);
        Page<PatientResponse> response = patients.map(PatientResponse::fromEntity);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/find-by-date-of-birth")
    public ResponseEntity<Page<PatientResponse>> getPatientByDateOfBirth(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,@NotNull @RequestParam LocalDate dateOfBirth){
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Patient> patients = patientService.findPatientByDateOfBirth(pageRequest, dateOfBirth);
        Page<PatientResponse> response = patients.map(PatientResponse::fromEntity);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/find-by-snils")
    public ResponseEntity<PatientResponse> getPatientBySNILS(@RequestParam String snils) {
        Patient patient = patientService.findPatientBySNILS(snils)
                .orElseThrow(() -> new ResourceNotFoundException("SNILS", snils));

        return ResponseEntity.ok(PatientResponse.fromEntity(patient));
    }

    @GetMapping("/fine-by-phone-number")
    public ResponseEntity<PatientResponse> getPatientByPhoneNumber(@ValidPhone @RequestParam String phoneNumber) {
        Patient patient = patientService.findPatientByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Phone", phoneNumber));

        return ResponseEntity.ok(PatientResponse.fromEntity(patient));
    }

    @GetMapping("/find-by-policy-number")
    public ResponseEntity<PatientResponse> getPatientByPolicyOMS(@ValidSinglePolicyNumber @RequestParam String policyNumber) {
        PolicyOMS policyOMS = policyService.findPolicyBySinglePolicyNumber(policyNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Policy", policyNumber));

        Patient patient = patientService.findPatientByPolicyOMS(policyOMS)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", policyOMS));

        return ResponseEntity.ok(PatientResponse.fromEntity(patient));
    }
}
