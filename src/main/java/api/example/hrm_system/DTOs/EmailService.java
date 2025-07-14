package api.example.hrm_system.DTOs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendOtpEmail(String toEmail, String otp) {
        try {
            log.info("Attempting to send OTP email to: {}", toEmail);
            log.debug("Using sender email: {}", fromEmail);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Your Verification Code");
            message.setText("Your OTP code is: " + otp + "\nThis code expires in 10 minutes.");

            javaMailSender.send(message);
            log.info("OTP email sent successfully to: {}", toEmail);
        } catch (MailAuthenticationException e) {
            log.error("Mail authentication failed. Check your mail server credentials.", e);
            throw new RuntimeException("Mail server authentication failed. Please check server credentials.", e);
        } catch (MailSendException e) {
            log.error("Failed to send email to: {}. Mail server connection issue.", toEmail, e);
            throw new RuntimeException("Failed to connect to mail server. Please try again later.", e);
        } catch (Exception e) {
            log.error("Unexpected error while sending OTP email to: {}", toEmail, e);
            throw new RuntimeException("Failed to send OTP email due to unexpected error.", e);
        }
    }

    public void sendPasswordResetEmail(String toEmail, String resetLink) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Password Reset Request");
            message.setText("To reset your password, click the link below:\n" + resetLink +
                    "\nThis link will expire in 24 hours.");

            javaMailSender.send(message);
            log.info("Password reset email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send password reset email to: {}", toEmail, e);
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }
}