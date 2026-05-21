package com.medical.med.controller;

import com.medical.med.DTO.request.CreateMedicalOrganizationRequest;
import com.medical.med.DTO.response.MedicalOrganizationResponse;
import com.medical.med.service.MedicalOrganizationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/organizations")
@AllArgsConstructor
@Slf4j
public class MedicalOrganizationController {
    private final MedicalOrganizationService medicalOrganizationService;

    @PostMapping
    public ResponseEntity<MedicalOrganizationResponse> createMedicalOrganization(
            @RequestBody CreateMedicalOrganizationRequest request) {
        log.info("Создание новой медицинской организации: {} {}", request.getName(), request.getCode());

        MedicalOrganizationResponse created = medicalOrganizationService.createMedicalOrganization(request);

        log.info("Организация создана с id: {}", created.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(created);
    }
}
