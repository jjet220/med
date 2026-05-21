package com.medical.med.service;

import com.medical.med.DTO.PolicyOMSDTO;
import com.medical.med.model.PolicyOMS;

import java.util.Optional;

public interface PolicyService {
    Optional<PolicyOMSDTO> findPolicyBySinglePolicyNumber(String policyNumber);
    PolicyOMSDTO createPolicy(PolicyOMSDTO policyOMS, Long patientId);
    Optional<PolicyOMSDTO> findPolicyById(Long id);
}
