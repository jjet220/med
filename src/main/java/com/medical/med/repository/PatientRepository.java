package com.medical.med.repository;

import com.medical.med.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Patient findPatientById(Long patientId);
}
