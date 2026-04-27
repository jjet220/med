package com.medical.med.DTO;

import com.medical.med.annotation.validation.ValidPhone;
import com.medical.med.annotation.validation.ValidSNILS;
import com.medical.med.model.Patient;
import com.medical.med.model.SexType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePatientRequest {
    private Long id;

    @NotBlank(message = "Фамилия обязательна")
    @Size(min = 1, max = 80, message = "Фамилия от 1 до 80 символов")
    private String surname;

    @NotBlank(message = "Имя обязательно")
    @Size(min = 2, max = 80, message = "Имя от 2 до 80 символов")
    private String name;

    private String patronymic;

    @NotNull(message = "Дата рождения обязательна")
    private LocalDate dateOfBirth;

    @NotNull(message = "Пол обязательно")
    private SexType sex;

    @NotBlank(message = "Номер телефона обязательно")
    @ValidPhone
    private String phoneNumber;

    @Email(message = "Неверный формат email")
    private String email;

    @ValidSNILS
    private String SNILS;

    private Long policyId;

    public Patient toEntity() {
        return Patient.builder()
                .id(this.id)
                .surname(this.surname)
                .name(this.name)
                .patronymic(this.patronymic)
                .dateOfBirth(this.dateOfBirth)
                .sex(this.sex)
                .phoneNumber(this.phoneNumber)
                .email(this.email)
                .SNILS(this.SNILS)
                .build();
    }

}
