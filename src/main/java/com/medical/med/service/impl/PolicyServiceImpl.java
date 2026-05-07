package com.medical.med.service.impl;

import com.medical.med.exeption.ConflictException;
import com.medical.med.model.PolicyOMS;
import com.medical.med.repository.PolicyRepository;
import com.medical.med.service.PolicyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class PolicyServiceImpl implements PolicyService {

    private final PolicyRepository policyRepository;

    @Override
    public Optional<PolicyOMS> findPolicyBySinglePolicyNumber(String policyNumber) {
        log.debug("Поиск полиса по номеру: {}", policyNumber);
        return policyRepository.findBySinglePolicyNumber(policyNumber);
    }

    @Override
    public PolicyOMS createPolicy(PolicyOMS policyOMS) {
        if (policyRepository.findBySinglePolicyNumber(policyOMS.getSinglePolicyNumber()).isPresent()) {
            log.error("Такой полис уже существует: {}", policyOMS);
            throw new ConflictException("policyNumber", policyOMS.getSinglePolicyNumber());
        }
        return policyRepository.save(policyOMS);
    }

    @Override
    public Optional<PolicyOMS> findPolicyById(Long id) {
        log.debug("Поиск полиса по id: {}", id);
        return policyRepository.findById(id);
    }
}
