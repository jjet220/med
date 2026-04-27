package com.medical.med.repository;

import com.medical.med.model.Patient;
import com.medical.med.model.PolicyOMS;
import com.medical.med.model.SexType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findPatientById(Long patientId);

    @Query("SELECT p FROM Patient p WHERE CONCAT(p.surname, ' ', p.name, ' ', COALESCE(p.patronymic, '')) = :fio")
    Page<Patient> findPatientByFIO(Pageable pageable, @Param("fio") String fio);

    Page<Patient> findPatientByDateOfBirth(Pageable pageable, LocalDate dateOfBirth);

    Page<Patient> findPatientBySex(Pageable pageable, SexType sexType);

    Optional<Patient> findPatientByPhoneNumber(String phoneNumber);

    Optional<Patient> findPatientByEmail(String email);

    Optional<Patient> findPatientBySNILS(String snils);

    Optional<Patient> findByPolicyOMS(PolicyOMS policyOMS);

    Boolean existsByEmail(String email);

    Boolean existsByPhoneNumber(String phoneNumber);

    Boolean existsBySNILS(String snils);
}
