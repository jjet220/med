package com.medical.med.repository;

import com.medical.med.model.Attachment;
import com.medical.med.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    Page<Attachment> findByPatientId(Pageable pageable, Long patientId);
    Optional<Attachment> findTopByPatientIdOrderByDateOfBeginDesc(Long patientId);
}
