package com.medical.med.service.impl;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.medical.med.service.SftpService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@AllArgsConstructor
@Slf4j
public class SftpServiceImpl implements SftpService {

    private final Session sftpSession;


    @Override
    @Async
    public void uploadMessageFile(String toEmail, String subject, String text) {

        ChannelSftp channelSftp = null;
        try {
            channelSftp = (ChannelSftp) sftpSession.openChannel("sftp");
            channelSftp.connect();

            String remoteDir = "/outgoing";

            try {
                channelSftp.lstat(remoteDir);
            } catch (SftpException e) {
                channelSftp.mkdir(remoteDir);
            }

            channelSftp.cd(remoteDir);

            String fileName = String.format("message_%d_%s.txt",
                    System.currentTimeMillis(),
                    toEmail.replace("@", "_at_").replace(".", "_"));

            String content = buildMessageContent(toEmail, subject, text);

            try (InputStream is = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8))) {
                channelSftp.put(is, fileName);
                log.debug("Файл сообщения {} загружен на SFTP для {}", fileName, toEmail);
            }
        } catch (Exception e) {
            log.error("Ошибка загрузки файла на SFTP: {}", e.getMessage());
            throw new RuntimeException("SFTP upload failed", e);
        } finally {
            if (channelSftp != null && channelSftp.isConnected()) {
                channelSftp.disconnect();
            }
        }
    }

    @Override
    public String buildMessageContent(String toEmail, String subject, String text) {
        return String.format("""
            === ИНФОРМАЦИЯ О СООБЩЕНИИ ===
            Кому: %s
            Тема: %s
            Время создания: %s
            ================================
            
            %s
            """,
                toEmail,
                subject,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")),
                text);
    }

}
