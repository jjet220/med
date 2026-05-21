package com.medical.med.repository;

import com.medical.med.model.MedicalOrganization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicalOrganizationRepository extends JpaRepository<MedicalOrganization, Long> {
    boolean existsByCode(String code);
    Optional<MedicalOrganization> findByCode(String code);
}
