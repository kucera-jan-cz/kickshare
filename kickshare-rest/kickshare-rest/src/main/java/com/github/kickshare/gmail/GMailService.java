package com.github.kickshare.gmail;


import java.io.IOException;
import java.text.MessageFormat;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.github.kickshare.common.io.ResourceUtil;
import com.github.kickshare.service.impl.MailService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * @author Jan.Kucera
 * @since 11.8.2017
 */
@Component
@AllArgsConstructor
public class GMailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);
    private static final String DOMAIN = "http://localhost:9000";
    private static final String REGISTRATION_SUBJECT = "Kickshare: Account activation";
    private static final String PASSWORD_RESET_SUBJECT = "Kickshare: Password reset";
    private static final String NEW_PASSWORD_SUBJECT = "Kickshare: Password generated";
    private JavaMailSender mailSender;

    public void sendActivationMail(final String email, final String token) {
        final String text = createAccountActivationTextMessage(email, token);
        final SimpleMailMessage message = message(email, REGISTRATION_SUBJECT, text);
        this.send(message);
    }

    public void sendPasswordResetMail(final String email, final String token) {
        final String text = createPasswordResetTextMessage(email, token);
        final SimpleMailMessage message = message(email, PASSWORD_RESET_SUBJECT, text);
        this.send(message);
    }

    public void sendPasswordGeneratedMail(final String email, final String password) {
        final String text = createPasswordResetTextMessage(email, password);
        final SimpleMailMessage message = message(email, NEW_PASSWORD_SUBJECT, text);
        this.send(message);
    }

    private SimpleMailMessage message(String email, String subject, String text) {
        final SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);
        return message;
    }

    private MimeMessage htmlMessage(String email, String subject, String html) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
            mimeMessage.setContent(html, "text/html");
            helper.setTo(email);
            helper.setSubject(subject);
        } catch (MessagingException e) {
            throw new IllegalStateException("Failed to create mime message");
        }
        return mimeMessage;
    }

    private String createPasswordResetTextMessage(String email, String token) {
        final String url = MessageFormat.format(DOMAIN + "/accounts/reset?email={0}&token={1}", email, token);
        final String text = template("email/password_reset.txt", email, url);
        return text;
    }

    private String createPasswordGeneratedTextMessage(String userName, String password) {
        final String url = DOMAIN + "/login";
        final String text = template("email/password_generated.txt", userName, password, url);
        return text;
    }

    private String createAccountActivationTextMessage(String userName, String token) {
        final String url = MessageFormat.format(DOMAIN + "/accounts/activate/{0}", token);
        final String text = template("email/account_activation.txt", userName, url);
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

    public void send(SimpleMailMessage message) {
        mailSender.send(message);
    }

    public void send(MimeMessage message) {
        mailSender.send(message);
    }
}
