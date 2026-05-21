package com.medical.med.service.impl;

import com.medical.med.DTO.request.CreateEmailMessageRequest;
import com.medical.med.mapper.EmailMessageMapper;
import com.medical.med.model.EmailMessage;
import com.medical.med.repository.MessageEmailRepository;
import com.medical.med.service.EmailNotificationService;
import com.medical.med.service.SftpService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class EmailNotificationServiceImpl implements EmailNotificationService {

    private final SftpService sftpService;
    private final MessageEmailRepository emailRepository;
    private final EmailMessageMapper messageMapper;

    @Override
    @Async
    public void sendNotification(String toEmail, String subject, String text) {
        try {
            sftpService.uploadMessageFile(toEmail, subject, text);
            log.debug("Файл сообщения успешно создан на SFTP для: {}", toEmail);
        } catch (Exception e) {
            log.error("Ошибка создания файла на SFTP: {}", e.getMessage());
            saveUnsuccessfulMessage(toEmail, text, e.getMessage());
        }
    }

    @Override
    public void saveUnsuccessfulMessage(String shippingAddress,
                                        String messageText,
                                        String exceptionText) {
        CreateEmailMessageRequest request = CreateEmailMessageRequest.builder()
                .shippingAddress(shippingAddress)
                .messageText(messageText)
                .exceptionText(exceptionText)
                .build();

        EmailMessage emailMessage = messageMapper.toEntity(request);
        emailRepository.save(emailMessage);
        log.debug("Неудачное сообщение сохранено в БД для повторной отправки");
    }
}