package code.travelplanner.Backend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TravelAppConfiguration {

    @Bean
    public RestTemplate restTemplate() {

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            // Automatically assign header and email to every request, as per rules of Nominatim
            request.getHeaders().set("User-Agent", "TravelPlanner/1.0 (github.com/MaxStevenson632)");
            return execution.execute(request, body);
        });

                return restTemplate;
    }
}
