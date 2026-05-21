package com.medical.med.service;

public interface EmailNotificationService {
    void sendNotification(String toEmail, String subject, String text);
    void saveUnsuccessfulMessage(String shippingAddress, String messageText, String exceptionText);
}
