package com.medical.med.DTO.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessageResponse {

    private Long id;
    private String messageText;
    private String shippingAddress;
    private String exceptionText;
    private LocalDateTime shippingTime;
}
