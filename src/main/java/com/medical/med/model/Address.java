package com.medical.med.model;

import com.medical.med.model.enums.AddressType;
import com.medical.med.model.enums.LocalityType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AddressType addressType;

    private String country;

    private String region;

    @Enumerated(EnumType.STRING)
    private LocalityType localityType;

    private String localityName;

    private String street;

    private String houseNumber;

    private boolean isPrivateHouse;

    private String apartmentNumber;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
}
