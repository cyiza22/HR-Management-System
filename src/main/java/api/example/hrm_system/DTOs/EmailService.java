package api.example.hrm_system.DTOs; // Changed package to service

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {


    private final JavaMailSender javaMailSender;

    public void sendOtpEmail(String toEmail, String otp) throws MailException {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Your OTP Code");
            message.setText("Your OTP is: " + otp + "\n\nThis code will expire in 10 minutes.");
            message.setFrom("no-reply@yourdomain.com");

            javaMailSender.send(message);
        } catch (MailException e) {
            throw new MailException("Failed to send OTP email") {};
        }
    }
}