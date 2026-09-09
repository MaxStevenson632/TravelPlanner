package code.travelplanner.Backend.user.Service;

import code.travelplanner.Backend.configuration.EmailConfiguration;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    @Value("${brevo.api-key}")
    private String brevoApiKey;

    @Autowired
    private EmailConfiguration emailConfiguration;
    private final RestClient brevoRestClient;

    public EmailService(JavaMailSender mailSender, RestClient brevoRestClient) {
        this.brevoRestClient = brevoRestClient;
    }

    public void sendVerificationEmail(String toEmail, String token) throws MessagingException {

        String verificationUrl = "https://travelplanner-backend-vd34.onrender.com/travelplanner/verify?token=" + token;

        String htmlContent =
                "<h3>Welcome to Travel Planner!</h3>"
                        + "<p>Please click the link below to verify your account:</p>"
                        + "<p><a href='" + verificationUrl + "'>Verify Account</a></p>";

        Map<String, Object> sender = Map.of(
                "name", "TravelPlanner",
                "email", "wayvelapp@gmail.com"
        );

        Map<String, Object> recipient = Map.of(
                "email", toEmail
        );

        Map<String, Object> requestBody = Map.of(
                "sender", sender,
                "to", List.of(recipient),
                "subject", "Verify your TravelPlanner account",
                "htmlContent", htmlContent
        );

        // HTTP POST request to https://api.brevo.com/v3/smtp/email
        // Content is JSON
        brevoRestClient.post()
                .uri("/v3/smtp/email")
                .header("api-key", brevoApiKey)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .retrieve()
                .toBodilessEntity();

    }
}
