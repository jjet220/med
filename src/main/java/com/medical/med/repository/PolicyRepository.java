package com.medical.med.repository;

import com.medical.med.model.PolicyOMS;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PolicyRepository extends JpaRepository<PolicyOMS, Long> {
    Optional<PolicyOMS> findBySinglePolicyNumber(String policyNumber);
}
