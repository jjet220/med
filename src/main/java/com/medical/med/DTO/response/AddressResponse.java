package com.medical.med.DTO.response;

import com.medical.med.model.enums.AddressType;
import com.medical.med.model.enums.LocalityType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {

    private Long id;
    private AddressType addressType;
    private PatientResponse patient;
    private String country;
    private String region;
    private LocalityType localityType;
    private String localityName;
    private String street;
    private String houseNumber;
    private boolean isPrivateHouse;
    private String apartmentNumber;
}
