package com.medical.med.service;

import com.medical.med.DTO.request.CreateIdentityDocumentRequest;
import com.medical.med.DTO.response.IdentityDocumentResponse;

import java.util.List;

public interface IdentityDocumentService {
    IdentityDocumentResponse createIdentityDocument(CreateIdentityDocumentRequest request, Long patientId);
    List<IdentityDocumentResponse> getIdentityDocuments(Long patientId);
    void deleteIdentityDocument(Long patientId, Long documentId);
}
