package com.medical.med.service;

import com.medical.med.model.PolicyOMS;

import java.util.Optional;

public interface PolicyService {
    Optional<PolicyOMS> findPolicyBySinglePolicyNumber(String policyNumber);
    PolicyOMS createPolicy(PolicyOMS policyOMS);
    Optional<PolicyOMS> findPolicyById(Long id);
}
