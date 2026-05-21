package com.medical.med.scheduler;

import com.medical.med.model.EmailMessage;
import com.medical.med.repository.MessageEmailRepository;
import com.medical.med.service.SftpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailMessageScheduler {

    private final MessageEmailRepository emailRepository;
    private final SftpService sftpService;

    @Scheduled(fixedDelay = 10000)
    public void processRetryQueue() {
        log.debug("Запуск обработки очереди повторной отправки сообщений");

        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(5);
        Pageable pageable = PageRequest.of(0, 100);

        Page<EmailMessage> messages = emailRepository.findMessageForRetry(cutoffTime, pageable);

        if (messages.isEmpty()) {
            log.debug("Нет записей для повторной отправки");
            return;
        }

        List<Long> successIds = new ArrayList<>();

        for (EmailMessage message : messages.getContent()) {
            try {
                sftpService.uploadMessageFile(
                        message.getShippingAddress(),
                        "Повторная отправка",
                        message.getMessageText()
                );
                successIds.add(message.getId());
                log.info("Успешно создан файл на SFTP для: {}", message.getShippingAddress());
            } catch (Exception e) {
                message.setShippingTime(LocalDateTime.now());
                message.setExceptionText(e.getMessage());
                emailRepository.save(message);
                log.error("Ошибка повторной отправки письма ID {}: {}", message.getId(), e.getMessage());
            }
        }
        if (!successIds.isEmpty()) {
            emailRepository.deleteByIdIn(successIds);
            log.info("Удалено {} записей из очереди", successIds.size());
        }
    }
}
