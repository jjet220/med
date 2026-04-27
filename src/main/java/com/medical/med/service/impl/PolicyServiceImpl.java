package com.medical.med.service.impl;

import com.medical.med.exeption.ConflictException;
import com.medical.med.model.PolicyOMS;
import com.medical.med.repository.PolicyRepository;
import com.medical.med.service.PolicyService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PolicyServiceImpl implements PolicyService {
    private final PolicyRepository policyRepository;

    public PolicyServiceImpl(PolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    @Override
    public Optional<PolicyOMS> findPolicyBySinglePolicyNumber(String policyNumber) {
        return policyRepository.findBySinglePolicyNumber(policyNumber);
    }

    @Override
    public PolicyOMS createPolicy(PolicyOMS policyOMS) {
        if (policyRepository.findBySinglePolicyNumber(policyOMS.getSinglePolicyNumber()).isPresent()) {
            throw new ConflictException("policyNumber", policyOMS.getSinglePolicyNumber());
        }
        return policyRepository.save(policyOMS);
    }

    @Override
    public Optional<PolicyOMS> findPolicyById(Long id) {
        return policyRepository.findById(id);
    }
}
