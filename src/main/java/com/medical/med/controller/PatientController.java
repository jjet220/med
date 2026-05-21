package com.medical.med.controller;

import com.medical.med.DTO.*;
import com.medical.med.DTO.request.CreateAddressRequest;
import com.medical.med.DTO.request.CreateAttachmentRequest;
import com.medical.med.DTO.request.CreateIdentityDocumentRequest;
import com.medical.med.DTO.request.CreatePatientRequest;
import com.medical.med.DTO.response.*;
import com.medical.med.annotation.validation.ValidPhone;
import com.medical.med.annotation.validation.ValidSinglePolicyNumber;
import com.medical.med.exeption.ResourceNotFoundException;
import com.medical.med.model.enums.SexType;
import com.medical.med.service.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/patients")
@Validated
@AllArgsConstructor
@Slf4j
public class PatientController {
    private final PatientService patientService;
    private final PolicyService policyService;
    private final AttachmentService attachmentService;
    private final IdentityDocumentService identityDocumentService;
    private final AddressService addressService;

    @DeleteMapping("/{patientId}/addresses/{addressId}")
    public ResponseEntity<AddressResponse> deletedAddress(
            @PathVariable Long patientId,
            @PathVariable Long addressId) {
        log.info("Удаление адреса с id: {}, у пациента с id: {}", addressId, patientId);

        addressService.deleteAddress(patientId, addressId);

        log.info("Адрес с id: {}, удалён", addressId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{patientId}/addresses")
    public ResponseEntity<List<AddressResponse>> getAddresses(@PathVariable Long patientId) {
        log.info("Получение всех адресов у пациента с id {}", patientId);

        List<AddressResponse> responses = addressService.getAddressOfPatient(patientId);

        log.info("Адреса получены");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responses);
    }

    @PostMapping("/{patientId}/addresses")
    public ResponseEntity<AddressResponse> createAddress(
            @PathVariable Long patientId,
            @Valid @RequestBody CreateAddressRequest request) {
        log.info("Создание адреса у пациента с id: {}", patientId);

        AddressResponse created = addressService.createAddress(request, patientId);

        log.info("Адрес создан с id: {}", created.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(created);
    }

    @DeleteMapping("/{patientId}/identity-documents/{documentId}")
    public ResponseEntity<IdentityDocumentResponse> deletedIdentityDocument(
            @PathVariable Long patientId,
            @PathVariable Long documentId) {
        log.info("Удаление документа с id: {}, у пациента с id: {}", documentId, patientId);

        identityDocumentService.deleteIdentityDocument(patientId, documentId);

        log.info("Документ с id: {}, удалён", documentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{patientId}/identity-documents")
    public ResponseEntity<List<IdentityDocumentResponse>> getIdentityDocuments(
            @PathVariable Long patientId) {
        log.info("Получение всех ДУЛ у пациента с id {}", patientId);

        List<IdentityDocumentResponse> responses = identityDocumentService.getIdentityDocuments(patientId);

        log.info("Документы получены");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responses);
    }

    @PostMapping("/{patientId}/identity-documents")
    public ResponseEntity<IdentityDocumentResponse> createIdentityDocument(
            @PathVariable Long patientId,
            @Valid @RequestBody CreateIdentityDocumentRequest request) {
        log.info("Создание нового ДУЛ {} у пациента с id {}"
                , request.getType(), patientId);

        IdentityDocumentResponse created = identityDocumentService
                .createIdentityDocument(request, patientId);

        log.info("ДУЛ создан с id: {}", created.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(created);
    }

    @PostMapping("/{patientId}/attachments")
    public ResponseEntity<AttachmentResponse> createAttachment(
            @PathVariable Long patientId,
            @Valid @RequestBody CreateAttachmentRequest request) {
        log.info("Создание нового прикрепления {} {} у пациента с id {}"
                , request.getDateOfBegin(), request.getType(), patientId);

        AttachmentResponse created = attachmentService.createAttachment(request, patientId);

        log.info("Прикрепление создано с id: {}", created.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(created);
    }

    @GetMapping("/{patientId}/attachments")
    public ResponseEntity<Page<AttachmentResponse>> findAllAttachmentsOfPatient(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @PathVariable Long patientId) {
        log.info("Поиск все прикреплений пациента с id: {}", patientId);

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<AttachmentResponse> responses = attachmentService.findAllAttachment(pageRequest, patientId);

        log.info("Прикрепления найдены по id: {}", patientId);
        return ResponseEntity.ok(responses);
    }


    @PutMapping("/update/{patientId}")
    public ResponseEntity<PatientResponse> updatePatient(@Valid @RequestBody CreatePatientRequest request,
                                                         @PathVariable Long patientId) {
        log.info("Обновление данных пациента с id: {}", patientId);

        PatientResponse updated = patientService.updatePatient(request, patientId);

        log.info("Данные пациента с id: {}, обновленны", updated.getId());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/deleted/{patientId}")
    public ResponseEntity<PatientResponse> deletedPatient(@PathVariable Long patineId) {
        log.info("Удаление пациента с id: {}", patineId);

        patientService.deletedPatient(patineId);

        log.info("Пациент с id: {}, удалён", patineId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<PatientWithActiveAttachmentResponse> getPatientById(@PathVariable Long patientId) {
        log.info("Нахождение пациента по id: {}", patientId);

        PatientWithActiveAttachmentResponse patient = patientService.findPatientById(patientId);

        log.info("Пациент найден с id: {}", patient.getId());
        return ResponseEntity.ok(patient);
    }

    @PostMapping
    public ResponseEntity<PatientResponse> createPatient(@Valid @RequestBody CreatePatientRequest request) {
        log.info("Создание нового пациента: {} {}", request.getSurname(), request.getName());

        PatientResponse created = patientService.createPatient(request, request.getPolicyId());

        log.info("Пациен создан с id: {}", created.getId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @GetMapping("/email")
    public ResponseEntity<PatientWithActiveAttachmentResponse> getPatientByEmail(@Email @RequestParam String email) {
        log.info("Нахождение пациента по эл.почте: {}", email);

        PatientWithActiveAttachmentResponse patient = patientService.findPatientByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email", email));

        log.info("Пациент найден с эл.почтой: {}", patient.getEmail());
        return ResponseEntity.ok(patient);
    }

    @GetMapping("/sex-type")
    public ResponseEntity<Page<PatientWithActiveAttachmentResponse>> getPatientBySexType(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @NotNull @RequestParam SexType sexType){
        log.info("Нахождение пациента(ов) по полу: {}", sexType);

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<PatientWithActiveAttachmentResponse> response = patientService.findPatientBySex(pageRequest, sexType);

        log.info("Пациенты найдены по полу: {}", sexType);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/fio")
    public ResponseEntity<Page<PatientWithActiveAttachmentResponse>> getPatientByFIO(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,@NotBlank @RequestParam String fio){
        log.info("Нахождение пациента(ов) по ФИО: {}", fio);

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<PatientWithActiveAttachmentResponse> response = patientService.findPatientByFIO(pageRequest, fio);

        log.info("Пациент(ы) найден(ы) по ФИО: {}", fio);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/date-of-birth")
    public ResponseEntity<Page<PatientWithActiveAttachmentResponse>> getPatientByDateOfBirth(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,@NotNull @RequestParam LocalDate dateOfBirth){
        log.info("Нахождение пациента(ов) по дате рождения: {}", dateOfBirth);

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<PatientWithActiveAttachmentResponse> response = patientService.
                findPatientByDateOfBirth(pageRequest, dateOfBirth);

        log.info("Пациент(ы) найден(ы) по дате рождения: {}", dateOfBirth);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/snils")
    public ResponseEntity<PatientWithActiveAttachmentResponse> getPatientBySNILS(@RequestParam String snils) {
        log.info("Нахождение пациента по СНИЛСу: {}", snils);

        PatientWithActiveAttachmentResponse patient = patientService.findPatientBySNILS(snils)
                .orElseThrow(() -> new ResourceNotFoundException("SNILS", snils));

        log.info("Пациент найден со СНИЛС: {}", patient.getSNILS());
        return ResponseEntity.ok(patient);
    }

    @GetMapping("/phone")
    public ResponseEntity<PatientWithActiveAttachmentResponse> getPatientByPhoneNumber(
            @ValidPhone @RequestParam String phoneNumber) {
        log.info("Нахождение пациента по телефону: {}", phoneNumber);

        PatientWithActiveAttachmentResponse patient = patientService.findPatientByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Phone", phoneNumber));

        log.info("Пациент найден с телефоном: {}", patient.getPhoneNumber());
        return ResponseEntity.ok(patient);
    }

    @GetMapping("/policy")
    public ResponseEntity<PatientWithActiveAttachmentResponse> getPatientByPolicyOMS(
            @ValidSinglePolicyNumber @RequestParam String policyNumber) {
        log.info("Нахождение пациента по полису: {}", policyNumber);

        PolicyOMSDTO policyOMS = policyService.findPolicyBySinglePolicyNumber(policyNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Policy", policyNumber));

        PatientWithActiveAttachmentResponse patient = patientService.findPatientByPolicyOMS(policyOMS)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", policyOMS));

        log.info("Пациент найден с полисом: {}", patient.getPolicy().getSinglePolicyNumber());
        return ResponseEntity.ok(patient);
    }
}
