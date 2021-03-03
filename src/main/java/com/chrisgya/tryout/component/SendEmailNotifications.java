package com.chrisgya.tryout.component;

import com.chrisgya.tryout.config.RabbitTopicConfig;
import com.chrisgya.tryout.config.properties.email.EmailProperties;
import com.chrisgya.tryout.model.request.MessageUserDeactivationRequest;
import com.chrisgya.tryout.model.request.MessageUserVerificationRequest;
import com.chrisgya.tryout.service.notification.EmailSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Component
public class SendEmailNotifications {
    private final EmailProperties emailProperties;
    private final EmailSenderService emailSenderService;

    @RabbitListener(queues = RabbitTopicConfig.SEND_USER_VERIFICATION_EMAIL_QUEUE)
    public void sendVerificationCode(MessageUserVerificationRequest messageUserVerificationRequest) throws MessagingException {
        Map<String, Object> model = new HashMap<>();
        model.put("name", messageUserVerificationRequest.getName());
        model.put("verificationCode", messageUserVerificationRequest.getVerificationToken());

        emailSenderService.emailSender(emailProperties.getTemplates().getUserVerification(), emailProperties.getSubject().getUserVerification(), messageUserVerificationRequest.getEmail(), model);
        log.debug("**************************** VERIFICATION CODE SENT SUCCESSFULLY TO {} *************************************", messageUserVerificationRequest.getEmail());
    }

    @RabbitListener(queues = RabbitTopicConfig.SEND_USER_DEACTIVATION_EMAIL_QUEUE)
    public void sendUserDeactivationNotification(MessageUserDeactivationRequest messageUserDeactivationRequest) throws MessagingException {
        Map<String, Object> model = new HashMap<>();
        model.put("name", messageUserDeactivationRequest.getName());

        emailSenderService.emailSender(emailProperties.getTemplates().getUserDeactivation(), emailProperties.getSubject().getUserDeactivation(), messageUserDeactivationRequest.getEmail(), model);
        log.debug("**************************** DEACTIVATION NOTIFICATION SENT SUCCESSFULLY TO {} *************************************", messageUserDeactivationRequest.getEmail());
    }

}
