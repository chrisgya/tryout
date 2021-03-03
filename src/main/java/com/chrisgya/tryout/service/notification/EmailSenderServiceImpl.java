package com.chrisgya.tryout.service.notification;


import com.chrisgya.tryout.config.properties.email.EmailProperties;
import com.chrisgya.tryout.model.Mail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;
    private final EmailProperties emailProperties;

    @Override
    @Async
    public void emailSender(String templateName, String subject, String mailTo, Map<String, Object> model) throws MessagingException {
        Mail mail = new Mail();
        mail.setFrom(emailProperties.getDefaults().getSender());

        mail.setMailTo(mailTo);

        mail.setSubject(subject);
        if (model != null && !model.isEmpty()) {
            mail.setProps(model);
        }

        sendEmail(mail, templateName);
    }


    private void sendEmail(Mail mail, String templateName) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        Context context = new Context();
        context.setVariables(mail.getProps());

        String html = templateEngine.process(templateName, context);
        helper.setTo(InternetAddress.parse(mail.getMailTo()));
        helper.setText(html, true);
        helper.setSubject(mail.getSubject());
        helper.setFrom(mail.getFrom());
        emailSender.send(message);

    }
}
