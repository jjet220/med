package com.medical.med.repository;

import com.medical.med.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByPatientId(Long patientId);
}
