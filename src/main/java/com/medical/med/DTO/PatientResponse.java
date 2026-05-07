package com.medical.med.DTO;

import com.medical.med.model.SexType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponse {

    private Long id;
    private String surname;
    private String name;
    private String patronymic;
    private LocalDate dateOfBirth;
    private SexType sex;
    private String phoneNumber;
    private String email;
    private String SNILS;
    private PolicyOMSDTO policy;
}
