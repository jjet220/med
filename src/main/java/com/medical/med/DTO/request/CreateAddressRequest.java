package com.medical.med.DTO.request;

import com.medical.med.model.enums.AddressType;
import com.medical.med.model.enums.LocalityType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAddressRequest {

    @NotNull(message = "Тип адреса обязательно")
    private AddressType addressType;

    private Long patientId;

    @NotBlank(message = "Страна обязательна")
    @Size(min = 2, max = 80, message = "Название страны от 2 до 80 символов")
    private String country;

    @NotBlank(message = "Регион обязательно")
    @Size(min = 2, max = 80, message = "Название региона от 2 до 80 символов")
    private String region;

    @NotNull(message = "Тип местоположения обязательно")
    private LocalityType localityType;

    @NotBlank(message = "Населенный пункта обязательно")
    @Size(min = 2, max = 80, message = "Название населенного пункта от 2 до 80 символов")
    private String localityName;

    @NotBlank(message = "Улица обязательна")
    @Size(min = 2, max = 80, message = "Название улицы от 2 до 80 символов")
    private String street;

    @NotBlank(message = "Номер дома обязательно")
    @Size(min = 2, max = 80, message = "Номер дома от 2 до 80 символов")
    private String houseNumber;

    private boolean isPrivateHouse;

    @Size(min = 2, max = 80, message = "Номер квартиры от 2 до 80 символов")
    private String apartmentNumber;
}
