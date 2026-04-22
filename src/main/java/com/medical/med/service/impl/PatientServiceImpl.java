package com.medical.med.service.impl;

import com.medical.med.model.Patient;
import com.medical.med.repository.PatientRepository;
import com.medical.med.service.PatientService;
import org.springframework.stereotype.Service;

@Service
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient findPatientById(Long patientId) {
        return patientRepository.findPatientById(patientId);
    }
}
