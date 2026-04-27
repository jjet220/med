package com.medical.med.DTO;

import com.medical.med.model.Patient;
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

    public static PatientResponse fromEntity(Patient patient) {
        PatientResponseBuilder builder = PatientResponse.builder()
                .id(patient.getId())
                .surname(patient.getSurname())
                .name(patient.getName())
                .patronymic(patient.getPatronymic())
                .dateOfBirth(patient.getDateOfBirth())
                .sex(patient.getSex())
                .phoneNumber(patient.getPhoneNumber())
                .email(patient.getEmail())
                .SNILS(patient.getSNILS());

        if (patient.getPolicyOMS() != null) {
            builder.policy(PolicyOMSDTO.fromEntity(patient.getPolicyOMS()));
        }

        return builder.build();
    }
}
