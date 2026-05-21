package com.medical.med.DTO.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmailMessageRequest {

    @NotBlank(message = "Текст сообщения обязательно")
    private String messageText;

    @NotBlank(message = "Адрес получателя обязательно")
    private String shippingAddress;

    private String exceptionText;
}
