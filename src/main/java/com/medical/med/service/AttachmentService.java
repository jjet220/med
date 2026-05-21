package com.medical.med.service;

import com.medical.med.DTO.response.AttachmentResponse;
import com.medical.med.DTO.request.CreateAttachmentRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AttachmentService {
    AttachmentResponse createAttachment(CreateAttachmentRequest attachment, Long patientId);
//    Optional<AttachmentResponse> findAttachmentById(Long attachmentId);
    Page<AttachmentResponse> findAllAttachment(Pageable pageable, Long patientId);
}
