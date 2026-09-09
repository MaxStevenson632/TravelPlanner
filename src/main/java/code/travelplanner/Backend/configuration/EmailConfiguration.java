package code.travelplanner.Backend.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Properties;

@Component
public class EmailConfiguration {

    @Bean
    public RestClient brevoRestClient() {
        // Brevo url
        return RestClient.builder()
                .baseUrl("https://api.brevo.com")
                .build();
    }
}
