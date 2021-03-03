package com.chrisgya.tryout.service.notification;

import org.springframework.scheduling.annotation.Async;

import javax.mail.MessagingException;
import java.util.Map;

public interface EmailSenderService {
    @Async
    void emailSender(String templateName, String subject, String mailTo, Map<String, Object> model) throws MessagingException;
}
