package com.medical.med.service;

import com.medical.med.model.Patient;

public interface PatientService {
    Patient findPatientById(Long patientId);
}
