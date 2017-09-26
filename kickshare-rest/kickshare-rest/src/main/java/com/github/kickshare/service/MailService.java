package com.github.kickshare.service;

import java.io.IOException;
import java.text.MessageFormat;

import com.github.kickshare.common.io.ResourceUtil;
import com.github.kickshare.gmail.GMailService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

/**
 * @author Jan.Kucera
 * @since 23.9.2017
 */
@Service
@AllArgsConstructor
public class MailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);
    private static final String REGISTRATION_SUBJECT = "Kickshare: Account activation";
    private final GMailService gmail;

    public void sendActivationMail(final String email, final String token) {
        final SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(REGISTRATION_SUBJECT);
        final String text = createAccountActivationTextMessage(token);
        message.setText(text);
        gmail.send(message);
    }

    public void sendPasswordResetMail(final String email) {

    }

    private String createPasswordResetTextMessage(String token) {
        final String url = MessageFormat.format("http://kickshare.eu/users/reset/{0}", token);
        final String text = template("email/password_reset.txt", url);
        return text;
    }

    private String createAccountActivationTextMessage(String token) {
            final String url = MessageFormat.format("http://kickshare.eu/users/verify/{0}", token);
            final String text = template("email/account_activation.txt", url);
            return text;
    }

    private String template(String path, Object... params) {
        try {
            final String template = ResourceUtil.toString(path);
            final String text = MessageFormat.format(template, params);
            return text;
        } catch (IOException e) {
            LOGGER.error("Failed to load email message text template", e);
            throw new IllegalStateException("Failed to load email message text template");
        }
    }
}
