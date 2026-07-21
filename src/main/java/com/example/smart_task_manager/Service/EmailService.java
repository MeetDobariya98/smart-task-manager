package com.example.smart_task_manager.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger =
            LoggerFactory.getLogger(EmailService.class);

    public void sendEmail(String email) {

        logger.info("Sending email to {}", email);

        try {

            Thread.sleep(5000);

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }

        logger.info("Email sent to {}", email);
    }
}