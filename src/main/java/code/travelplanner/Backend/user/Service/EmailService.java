package code.travelplanner.Backend.user.Service;

import code.travelplanner.Backend.configuration.EmailConfiguration;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private EmailConfiguration emailConfiguration;
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String toEmail, String token) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        // Enable HTML elements
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        // Email address and subject
        helper.setTo(toEmail);
        helper.setSubject("Verify your TravelPlanner account");
        helper.setFrom("wayvelapp@gmail.com");

        // Link to backend endpoint
        String verificationUrl = "http://localhost:8080/travelplanner/verify?token=" + token;

        // HTML code for a clickable hyperlink
        String htmlContent = "<h3>Welcome to Travel Planner!</h3>"
                + "<p>Please click the link below to verify your account:</p>"
                + "<a href='" + verificationUrl + "'>Verify Account</a>";

        // Spring Boot renders this text as html
        helper.setText(htmlContent, true);

        mailSender.send(mimeMessage);
    }
}
