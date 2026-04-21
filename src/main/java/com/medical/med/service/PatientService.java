package com.medical.med.service;

import com.medical.med.model.Patient;
import org.springframework.context.annotation.Configuration;

@Configuration
public interface PatientService {
    Patient findPatientById(Long patientId);
}
