package com.medical.med.repository;

import com.medical.med.model.IdentityDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IdentityDocumentRepository extends JpaRepository<IdentityDocument, Long> {
    List<IdentityDocument> findByPatientId(Long patientId);
}
