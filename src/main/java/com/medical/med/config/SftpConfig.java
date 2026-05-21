package com.medical.med.config;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class SftpConfig {

    @Value("${sftp.host:localhost}")
    private String host;

    @Value("${sftp.port:2222}")
    private int port;

    @Value("${sftp.user:user}")
    private String user;

    @Value("${sftp.password:password}")
    private String password;

    @Bean
    public Session createSftpSession() {
        try {
            JSch jSch = new JSch();
            Session session = jSch.getSession(user, host, port);
            session.setPassword(password);

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            config.put("PreferredAuthentications", "password");
            session.setConfig(config);

            session.connect(50000);

            return session;
        } catch (Exception e) {
            throw new RuntimeException("Не удалось подключиться к SFTP: " + e.getMessage(), e);
        }
    }
}