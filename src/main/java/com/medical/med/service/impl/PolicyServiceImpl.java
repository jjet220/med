package com.medical.med.service.impl;

import com.medical.med.DTO.PolicyOMSDTO;
import com.medical.med.exeption.ConflictException;
import com.medical.med.exeption.ResourceNotFoundException;
import com.medical.med.mapper.PatientMapper;
import com.medical.med.model.Patient;
import com.medical.med.model.PolicyOMS;
import com.medical.med.repository.PatientRepository;
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
    private final PatientMapper patientMapper;
    private final PatientRepository patientRepository;

    @Override
    public Optional<PolicyOMSDTO> findPolicyBySinglePolicyNumber(String policyNumber) {
        log.debug("Поиск полиса по номеру: {}", policyNumber);

        Optional<PolicyOMS> policyOMS = policyRepository.findBySinglePolicyNumber(policyNumber);

        return policyOMS.map(patientMapper::policyToDto);
    }

    @Override
    public PolicyOMSDTO createPolicy(PolicyOMSDTO request, Long patientId) {
        log.debug("Создание полиса у пациента с id: {}", patientId);

        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> {
            log.error("Пациента с такми id {}, не найдено", patientId);
            return new ResourceNotFoundException("Patient", patientId);
        });

        if (policyRepository.findBySinglePolicyNumber(request.getSinglePolicyNumber()).isPresent()) {
            log.error("Такой полис уже существует: {}", request);
            throw new ConflictException("policyNumber", request.getSinglePolicyNumber());
        }

        PolicyOMS policy = PolicyOMS.builder()
                .patient(patient)
                .singlePolicyNumber(request.getSinglePolicyNumber())
                .dateAndTimeOfCreation(request.getDateAndTimeOfCreation())
                .build();

        PolicyOMS savedPolicy = policyRepository.save(policy);

        log.debug("Полис с id: {} создан", savedPolicy.getId());

        return patientMapper.policyToDto(savedPolicy);
    }

    @Override
    public Optional<PolicyOMSDTO> findPolicyById(Long id) {
        log.debug("Поиск полиса по id: {}", id);

        PolicyOMS policyOMS = policyRepository.findById(id).orElseThrow(() -> {
            log.error("Полис не найден с id: {}", id);
            return new ResourceNotFoundException("Policy", id);
        });

        return Optional.of(patientMapper.policyToDto(policyOMS));
    }
}
