package pl.apap.account.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationEmailService {

    @Autowired
    private JavaMailSender sender;

    private final String email = "pawel.mail.sender@gmail.com";

    public void sendConfirmationEmail(String toEmail, String subject, String messageBody){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(email);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(messageBody);

        sender.send(message);
    }

}
