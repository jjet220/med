package com.medical.med.service;

public interface SftpService {

    void uploadMessageFile(String toEmail, String subject, String text);

    String buildMessageContent(String toEmail, String subject, String text);
}
