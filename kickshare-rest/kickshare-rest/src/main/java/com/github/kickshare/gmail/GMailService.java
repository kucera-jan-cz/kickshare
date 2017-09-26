package com.github.kickshare.gmail;


import lombok.AllArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

/**
 * @author Jan.Kucera
 * @since 11.8.2017
 */
@Component
@AllArgsConstructor
public class GMailService {

    private MailSender mailSender;

    public void send(SimpleMailMessage message) {
        mailSender.send(message);
    }

}
